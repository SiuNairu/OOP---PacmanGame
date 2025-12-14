package pacman2;

import Enities.PacmanPlayer;
import Enities.Ghost;
import Enities.Block;
import fx.Respawn;
import fx.Popup;
import Map.MapData;
import Map.GameMap;
import UI.GameWinScreen;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PacmanGamePanel extends JPanel implements ActionListener, KeyListener {

    // 1. SIZE & MAP
    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int boardWidth = columnCount * tileSize;
    private final int boardHeight = rowCount * tileSize;
    private final int CHERRY_SIZE = 20;

    // 2. IMAGE
    private Image wallImage;
    private Image blueGhostImage;
    private Image redGhostImage;
    private Image pinkGhostImage;
    private Image orangeGhostImage;
    private Image scaredGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image cherryImage;

    // 3. OBJECTS & GAME STATE
    private GameMap gameMap;
    private Set<Block> walls;
    private Set<Block> foods;
    private Set<Ghost> ghosts;
    private PacmanPlayer pacman;

    private Timer gameLoop;
    private final char[] directions = {'U', 'D', 'L', 'R'};
    private final Random random = new Random();

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    private char pendingDirection = 0;

    // Power mode
    private boolean powerMode = false;
    private long powerModeUntilMs = 0L;
    private final int POWER_DURATION_MS = 7000;

    // Ghost respawn
    private final int GHOST_RESPAWN_MS = 5000;
    private final List<Respawn> pendingRespawns = new ArrayList<>();

    // Cherry
    private Block cherry = null;
    private long nextCherryAtMs; 
    
    
    // Popup 
    private List<Popup> popups = new ArrayList<>();

    private int currentLevel = 0; // 0 = map 1, 1 = map 2

    // ------------------ CONSTRUCTOR ------------------
    public PacmanGamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        MovableBlock.setTileSize(tileSize);

        loadImages();
        loadGameResources();

        gameLoop = new Timer(50, this);
        gameLoop.start();

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    // LOAD RESOURCE 

    private void loadImages() {
        this.wallImage        = loadImage("/Image/wall.png");

        this.pinkGhostImage   = loadImage("/Image/pinkGhost.png");
        this.orangeGhostImage = loadImage("/Image/orangeGhost.png");
        this.redGhostImage    = loadImage("/Image/redGhost.png");
        this.blueGhostImage   = loadImage("/Image/blueGhost.png");

        this.pacmanUpImage    = loadImage("/Image/pacmanUp.png");
        this.pacmanDownImage  = loadImage("/Image/pacmanDown.png");
        this.pacmanLeftImage  = loadImage("/Image/pacmanLeft.png");
        this.pacmanRightImage = loadImage("/Image/pacmanRight.png");

        this.cherryImage      = loadImage("/Image/cherry.png");
        this.scaredGhostImage = loadImage("/Image/scaredGhost.png");
    }

    // helper: tránh NPE, nếu sai path sẽ in lỗi ra console
    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    private void loadGameResources() {
        gameMap = new GameMap(
                tileSize,
                wallImage,
                redGhostImage,
                blueGhostImage,
                pinkGhostImage,
                orangeGhostImage,
                pacmanRightImage
        );

        gameMap.loadMap(currentLevel);

        this.pacman = gameMap.getPacman();
        this.walls = gameMap.getWalls();
        this.foods = gameMap.getFoods();
        this.ghosts = gameMap.getGhosts();

        for (Ghost ghost : ghosts) {
            ghost.initializeRandomDirection(directions, false);
        }

        powerMode = false;
        cherry = null;
        pendingRespawns.clear();
        popups.clear();

        // reset thời gian spawn cherry cho level mới
        nextCherryAtMs =
                System.currentTimeMillis() + 12000 + random.nextInt(8000);
    }

    // GAME LOOP

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    public void move() {
        if (gameOver) return;

        long now = System.currentTimeMillis();

        // 1. Cập nhật hướng Pacman nếu có pendingDirection hợp lệ
        if (pendingDirection != 0 && canMove(pacman, pendingDirection)) {
            pacman.setDirection(pendingDirection);
            pacman.updateSpeed();
            updatePacmanImage();
            pendingDirection = 0;
        }

        // 2. Di chuyển Pacman
        pacman.setX(pacman.getX() + pacman.velocityX);
        pacman.setY(pacman.getY() + pacman.velocityY);

        // 3. Va chạm tường với Pacman
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.velocityX);
                pacman.setY(pacman.getY() - pacman.velocityY);
                break;
            }
        }

        // Kiểm tra biên
        if (pacman.getX() < 0 || pacman.getX() + pacman.getWidth() > boardWidth
                || pacman.getY() < 0 || pacman.getY() + pacman.getHeight() > boardHeight) {
            pacman.setX(pacman.getX() - pacman.velocityX);
            pacman.setY(pacman.getY() - pacman.velocityY);
        }

        // 4. Power Mode & Cherry
        if (powerMode && now >= powerModeUntilMs) {
            powerMode = false;
            for (Ghost g : ghosts) {
                g.resetImage();
            }
        }

        if (!gameOver && cherry == null && now >= nextCherryAtMs) {
            spawnCherry();
        }

        // 5. Di chuyển & va chạm Ghost
        Set<Ghost> eatenGhosts = new HashSet<>();

        for (Ghost ghost : ghosts) {
            // Va chạm với Pacman
            if (collision(ghost, pacman)) {
                if (powerMode) {
                    // ăn ghost
                    pendingRespawns.add(new Respawn(ghost, now + GHOST_RESPAWN_MS));
                    int bonus = 20;
                    score += bonus;
                    popups.add(new Popup(
                            ghost.getX() + ghost.getWidth() / 2,
                            ghost.getY(),
                            "+" + bonus,
                            now,
                            900
                    ));
                    eatenGhosts.add(ghost);
                } else {
                    // mất mạng
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        gameLoop.stop();
                        // mở Game Over luôn tại đây
                        SwingUtilities.invokeLater(() ->
                                new GameOverScreen(score).setVisible(true)
                        );
                        return;
                    }
                    resetPositions();
                    break;
                }
            }

            // AI Ghost
            if (isNearPacman(ghost)) {
                char dir = powerMode
                        ? chooseFleeDirection(ghost)
                        : chooseChaseDirection(ghost);
                if (dir != 0 && canMove(ghost, dir) && ghost.canTurnNow(now)) {
                    ghost.setDirectionNoStep(dir, powerMode);
                }
            } else {
                if (ghost.canTurnNow(now)) {
                    char newDirection = chooseGhostDirectionSmart(ghost);
                    if (newDirection != 0 && canMove(ghost, newDirection)) {
                        ghost.setDirectionNoStep(newDirection, powerMode);
                    }
                }
            }

            // Di chuyển ghost
            ghost.setX(ghost.getX() + ghost.velocityX);
            ghost.setY(ghost.getY() + ghost.velocityY);

            // va tường / biên
            for (Block wall : walls) {
                if (collision(ghost, wall)
                        || ghost.getX() <= 0
                        || ghost.getX() + ghost.getWidth() >= boardWidth) {
                    ghost.setX(ghost.getX() - ghost.velocityX);
                    ghost.setY(ghost.getY() - ghost.velocityY);
                    char newDirection = chooseGhostDirectionSmart(ghost);
                    ghost.setDirectionNoStep(newDirection, powerMode);
                    break;
                }
            }
        }

        ghosts.removeAll(eatenGhosts);

        // 6. Respawn Ghost
        if (!pendingRespawns.isEmpty()) {
            List<Respawn> done = new ArrayList<>();
            for (Respawn r : pendingRespawns) {
                if (now >= r.getAtMs()) {
                    Ghost g = r.getGhost();
                    g.reset();
                    g.initializeRandomDirection(directions, powerMode);

                    if (powerMode && scaredGhostImage != null) {
                        g.setImage(scaredGhostImage);
                    } else if (g.normalImage != null) {
                        g.resetImage();
                    }

                    ghosts.add(g);
                    done.add(r);
                }
            }
            pendingRespawns.removeAll(done);
        }

        // 7. Popups
        popups.removeIf(p -> now - p.getCreatedMs() >= p.getDurationMs());

        // 8. Food
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        if (foodEaten != null) {
            foods.remove(foodEaten);
        }

        // 9. Cherry
        if (cherry != null && collision(pacman, cherry)) {
            powerMode = true;
            powerModeUntilMs = System.currentTimeMillis() + POWER_DURATION_MS;
            cherry = null;
            if (scaredGhostImage != null) {
                for (Ghost g : ghosts) {
                    g.setImage(scaredGhostImage);
                }
            }
            nextCherryAtMs =
                    System.currentTimeMillis() + (15000 + random.nextInt(15000));
        }

        // 10. Win level / Win game
        if (foods.isEmpty()) {
            // Ăn hết food của level hiện tại
            if (currentLevel < MapData.getTotalLevels() - 1) {
                // chưa phải level cuối → hiện bảng thắng màn hỏi có qua level mới không
                int levelNumber = currentLevel + 1; // hiển thị cho user (level 1, 2,...)
                gameLoop.stop();
                SwingUtilities.invokeLater(() ->
                        new LevelWinScreen(score, levelNumber, this).setVisible(true)
                );
            } else {
                // đây là level cuối → thắng game
                gameOver = true;
                gameLoop.stop();
                SwingUtilities.invokeLater(() ->
                        new GameWinScreen(score).setVisible(true)
                );
            }
            return;
        }
    }

    // ------------------ SUPPORT: CHERRY & AI ------------------

    private void spawnCherry() {
        String[] tileMap = MapData.getMap(currentLevel);

        // thử 100 vị trí random
        for (int i = 0; i < 100; i++) {
            int c = random.nextInt(columnCount);
            int r = random.nextInt(rowCount);
            char ch = tileMap[r].charAt(c);
            if (ch != 'X') {
                int x = c * tileSize + (tileSize - CHERRY_SIZE) / 2;
                int y = r * tileSize + (tileSize - CHERRY_SIZE) / 2;
                Block proposed = new Block(null, x, y, CHERRY_SIZE, CHERRY_SIZE);
                boolean overlaps = collision(proposed, pacman);
                if (!overlaps) {
                    for (Ghost g : ghosts) {
                        if (collision(proposed, g)) {
                            overlaps = true;
                            break;
                        }
                    }
                }
                if (!overlaps) {
                    cherry = proposed;
                    return;
                }
            }
        }

        // fallback: giữa bản đồ
        int x = (columnCount / 2) * tileSize + (tileSize - CHERRY_SIZE) / 2;
        int y = (rowCount / 2) * tileSize + (tileSize - CHERRY_SIZE) / 2;
        cherry = new Block(null, x, y, CHERRY_SIZE, CHERRY_SIZE);
    }

    private boolean isNearPacman(Ghost ghost) {
        int dxTiles = Math.abs(ghost.getX() - pacman.getX()) / tileSize;
        int dyTiles = Math.abs(ghost.getY() - pacman.getY()) / tileSize;
        int radius = powerMode ? 4 : 2;
        return dxTiles <= radius && dyTiles <= radius;
    }

    private char chooseChaseDirection(Ghost ghost) {
        ArrayList<Character> legal = new ArrayList<>();
        for (char d : directions) {
            if (canMove(ghost, d)) legal.add(d);
        }
        if (legal.isEmpty()) return 0;

        int bestDist = Integer.MAX_VALUE;
        ArrayList<Character> bests = new ArrayList<>();

        for (char d : legal) {
            int vx = (d == 'L') ? -tileSize / 4 : (d == 'R') ? tileSize / 4 : 0;
            int vy = (d == 'U') ? -tileSize / 4 : (d == 'D') ? tileSize / 4 : 0;
            int nx = ghost.getX() + vx;
            int ny = ghost.getY() + vy;
            int manhattan = Math.abs(nx - pacman.getX()) + Math.abs(ny - pacman.getY());
            manhattan -= random.nextInt(2);

            if (manhattan < bestDist) {
                bestDist = manhattan;
                bests.clear();
                bests.add(d);
            } else if (manhattan == bestDist) {
                bests.add(d);
            }
        }

        return pickSeparatedDirection(ghost, bests);
    }

    private char chooseFleeDirection(Ghost ghost) {
        ArrayList<Character> legal = new ArrayList<>();
        for (char d : directions) {
            if (canMove(ghost, d)) legal.add(d);
        }
        if (legal.isEmpty()) return 0;

        int bestDist = -1;
        ArrayList<Character> bests = new ArrayList<>();

        for (char d : legal) {
            int vx = (d == 'L') ? -tileSize / 4 : (d == 'R') ? tileSize / 4 : 0;
            int vy = (d == 'U') ? -tileSize / 4 : (d == 'D') ? tileSize / 4 : 0;
            int nx = ghost.getX() + vx;
            int ny = ghost.getY() + vy;
            int manhattan = Math.abs(nx - pacman.getX()) + Math.abs(ny - pacman.getY());

            if (manhattan > bestDist) {
                bestDist = manhattan;
                bests.clear();
                bests.add(d);
            } else if (manhattan == bestDist) {
                bests.add(d);
            }
        }

        return pickSeparatedDirection(ghost, bests);
    }

    private char chooseGhostDirectionSmart(Ghost ghost) {
        ArrayList<Character> legal = new ArrayList<>();

        // cố gắng random 8 lần tránh đảo chiều 180
        for (int i = 0; i < 8; i++) {
            char d = directions[random.nextInt(4)];
            if (canMove(ghost, d) && !Ghost.isOpposite(d, ghost.getDirection())) {
                legal.add(d);
            }
        }

        if (!legal.isEmpty()) return pickSeparatedDirection(ghost, legal);

        // fallback: bất kì hướng hợp lệ
        legal.clear();
        for (char d : directions) {
            if (canMove(ghost, d)) legal.add(d);
        }
        if (!legal.isEmpty()) return pickSeparatedDirection(ghost, legal);

        return directions[random.nextInt(4)];
    }

    private char pickSeparatedDirection(Ghost ghost, List<Character> candidates) {
        if (candidates.isEmpty()) return 0;
        int bestCount = Integer.MAX_VALUE;
        ArrayList<Character> bests = new ArrayList<>();

        for (char d : candidates) {
            int cnt = countNearbyGhostsSameDirection(ghost, d);
            if (cnt < bestCount) {
                bestCount = cnt;
                bests.clear();
                bests.add(d);
            } else if (cnt == bestCount) {
                bests.add(d);
            }
        }

        return bests.get(random.nextInt(bests.size()));
    }

    private int countNearbyGhostsSameDirection(Ghost me, char dir) {
        int count = 0;
        for (Ghost g : ghosts) {
            if (g == me) continue;
            int dxTiles = Math.abs(g.getX() - me.getX()) / tileSize;
            int dyTiles = Math.abs(g.getY() - me.getY()) / tileSize;
            if (dxTiles <= 2 && dyTiles <= 2 && g.getDirection() == dir) {
                count++;
            }
        }
        return count;
    }

    // ------------------ VẼ ------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Pacman
        g.drawImage(pacman.image, pacman.getX(), pacman.getY(),
                pacman.getWidth(), pacman.getHeight(), null);

        // Ghosts
        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.image, ghost.getX(), ghost.getY(),
                    ghost.getWidth(), ghost.getHeight(), null);
        }

        // Walls
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.getX(), wall.getY(),
                    wall.getWidth(), wall.getHeight(), null);
        }

        // Foods
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }

        // Cherry
        if (cherry != null) {
            if (cherryImage != null) {
                g.drawImage(cherryImage, cherry.getX(), cherry.getY(),
                        cherry.getWidth(), cherry.getHeight(), null);
            } else {
                g.setColor(Color.RED);
                g.fillOval(cherry.getX(), cherry.getY(),
                        cherry.getWidth(), cherry.getHeight());
                g.setColor(Color.WHITE);
            }
        }

        // Score / Lives
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String status = "x" + lives + "   Score: " + score;
        g.drawString(status, tileSize / 2, tileSize / 2);

        // Popups
        long nowDraw = System.currentTimeMillis();
        g.setColor(Color.YELLOW);
        for (Popup p : popups) {
            long elapsed = nowDraw - p.getCreatedMs();
            if (elapsed < 0) elapsed = 0;
            int rise = (int) Math.min(20, (elapsed * 20) / p.getDurationMs());
            g.drawString(p.getText(), p.getX(), p.getY() - rise);
        }
    }

    // ------------------ HỖ TRỢ LOGIC ------------------

    public boolean collision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth()
                && a.getX() + a.getWidth() > b.getX()
                && a.getY() < b.getY() + b.getHeight()
                && a.getY() + a.getHeight() > b.getY();
    }

    private boolean canMove(Block b, char direction) {
        int vx = 0;
        int vy = 0;
        int step = tileSize / 4;

        switch (direction) {
            case 'U': vy = -step; break;
            case 'D': vy = step;  break;
            case 'L': vx = -step; break;
            case 'R': vx = step;  break;
        }

        int nextX = b.getX() + vx;
        int nextY = b.getY() + vy;

        // giới hạn biên
        if (nextX < 0 || nextX + b.getWidth() > boardWidth
                || nextY < 0 || nextY + b.getHeight() > boardHeight) {
            return false;
        }

        // va tường
        for (Block wall : walls) {
            if (nextX < wall.getX() + wall.getWidth()
                    && nextX + b.getWidth() > wall.getX()
                    && nextY < wall.getY() + wall.getHeight()
                    && nextY + b.getHeight() > wall.getY()) {
                return false;
            }
        }

        return true;
    }

    private void resetPositions() {
        if (pacman != null) {
            pacman.reset();
            pacman.updateSpeed();
            updatePacmanImage();
        }
        for (Ghost g : ghosts) {
            g.reset();
            g.initializeRandomDirection(directions, powerMode);
        }
        powerMode = false;
        cherry = null;
        pendingRespawns.clear();
        popups.clear();
    }

    private void updatePacmanImage() {
        char dir = pacman.getDirection();
        if (dir == 'U') {
            pacman.image = pacmanUpImage;
        } else if (dir == 'D') {
            pacman.image = pacmanDownImage;
        } else if (dir == 'L') {
            pacman.image = pacmanLeftImage;
        } else {
            pacman.image = pacmanRightImage;
        }
    }

    // 🔹 Hàm public để LevelWinScreen gọi qua level mới
 public void goToNextLevel() {
    if (currentLevel < MapData.getTotalLevels() - 1) {
        currentLevel++;

        // 👉 Reset lại 3 mạng khi sang level mới
        lives = 3;

        loadGameResources();
        resetPositions();
        gameOver = false;
        gameLoop.start();
        requestFocusInWindow();
    }
}


    // ------------------ KEY LISTENER ------------------

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            pendingDirection = 'U';
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            pendingDirection = 'D';
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            pendingDirection = 'L';
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            pendingDirection = 'R';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
