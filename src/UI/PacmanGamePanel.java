package UI;

import Entities.Block;
import Entities.Ghost;
import Input.InputHandler;
<<<<<<< HEAD
import Logic.*;
import Map.GameMap;
import Map.MapData;
import Utils.Popup;
import Utils.Respawn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
=======
import Logic.GameEngine;
import Logic.GameState;
import Map.GameMap;
import Map.MapData;
import Utils.Popup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

public class PacmanGamePanel extends JPanel implements ActionListener {

    private final GameState gameState;
    private final GameEngine gameEngine;
<<<<<<< HEAD

    private GameMap gameMap;
    private final Timer gameLoop;

    // Images
    private Image wallImage, blueGhostImage, redGhostImage,
                  pinkGhostImage, orangeGhostImage,
                  cherryImage, scaredGhostImage;

    private Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;

    public PacmanGamePanel() {

        //UI 
        setPreferredSize(new Dimension(19 * 32, 21 * 32));
        setBackground(Color.BLACK);
        setFocusable(true);

        //STATE
        gameState = new GameState();

        //RESOURSE
        loadImages();

        //MAP
        gameMap = new GameMap(
                32,
                wallImage,
                redGhostImage,
                blueGhostImage,
                pinkGhostImage,
                orangeGhostImage,
                pacmanUp,
                pacmanDown,
                pacmanLeft,
                pacmanRight
        );

        CollisionDetector collisionDetector =
                new CollisionDetector(19 * 32, 21 * 32);

        GhostController ghostController =
                new GhostController(collisionDetector, 32);

        List<Respawn> pendingRespawns = new ArrayList<>();

        PacmanSystem pacmanSystem =
                new PacmanSystem(gameState, collisionDetector);

        GhostSystem ghostSystem =
                new GhostSystem(gameState, collisionDetector, ghostController, pendingRespawns);

        ItemSystem itemSystem =
                new ItemSystem(gameState, collisionDetector);

        RespawnSystem respawnSystem =
                new RespawnSystem(gameState, ghostController, pendingRespawns);

        gameEngine = new GameEngine(
                gameState,
                pacmanSystem,
                ghostSystem,
                itemSystem,
                respawnSystem
        );

        //INPUT
        addKeyListener(new InputHandler(gameState));

=======
    private GameMap gameMap;
    private Timer gameLoop;
    
    // Images
    private Image wallImage, blueGhostImage, redGhostImage, pinkGhostImage, orangeGhostImage, cherryImage, scaredGhostImage;
    // 4 ảnh Pacman
    private Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;

    public PacmanGamePanel() {
        setPreferredSize(new Dimension(19 * 32, 21 * 32));
        setBackground(Color.BLACK);
        
        // Fix lỗi focus bàn phím
        setFocusable(true);

        // Khởi tạo MVC
        gameState = new GameState();
        gameEngine = new GameEngine(gameState);
        
        // Input
        addKeyListener(new InputHandler(gameState));
        
        // Click chuột vào game để chắc chắn nhận bàn phím
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });

<<<<<<< HEAD
        //START GAME
        gameEngine.loadLevel(0, gameMap);

=======
        loadImages();
        
        // Truyền 4 ảnh vào GameMap để tạo Pacman
        gameMap = new GameMap(32, wallImage, redGhostImage, blueGhostImage, pinkGhostImage, orangeGhostImage, 
                              pacmanUp, pacmanDown, pacmanLeft, pacmanRight);
        
        // Bắt đầu game
        gameEngine.loadLevel(0, gameMap);
        
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

<<<<<<< HEAD
    // GAME LOOP
    @Override
    public void actionPerformed(ActionEvent e) {
        gameEngine.update();

        if (gameState.isGameOver()) {
            gameLoop.stop();
            new GameOverScreen(gameState.getScore()).setVisible(true);
        }
        else if (gameState.getFoods().isEmpty()) {
            gameLoop.stop();
            if (gameState.getCurrentLevel() < MapData.getTotalLevels() - 1) {
                new LevelWinScreen(
                        gameState.getScore(),
                        gameState.getCurrentLevel() + 1,
                        this
                ).setVisible(true);
            } else {
                new GameWinScreen(gameState.getScore()).setVisible(true);
            }
        }

        repaint();
    }

    public void goToNextLevel() {
        gameEngine.loadLevel(gameState.getCurrentLevel() + 1, gameMap);
        gameLoop.start();
        requestFocusInWindow();
    }

    //RENDER
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Walls
        for (Block wall : gameState.getWalls())
            g.drawImage(wall.image, wall.getX(), wall.getY(), 32, 32, null);

        // Foods
        g.setColor(Color.WHITE);
        for (Block food : gameState.getFoods())
            g.fillRect(food.getX(), food.getY(), 4, 4);

        // Cherry
        if (gameState.getCherry() != null)
            g.drawImage(
                    cherryImage,
                    gameState.getCherry().getX(),
                    gameState.getCherry().getY(),
                    20,
                    20,
                    null
            );

        // Pacman
        if (gameState.getPacman() != null)
            g.drawImage(
                    gameState.getPacman().image,
                    gameState.getPacman().getX(),
                    gameState.getPacman().getY(),
                    32,
                    32,
                    null
            );

        // Ghosts
        for (Ghost ghost : gameState.getGhosts()) {
            Image img = gameState.isPowerMode() ? scaredGhostImage : ghost.image;
            if (img == null) img = ghost.image;
            g.drawImage(img, ghost.getX(), ghost.getY(), 32, 32, null);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(
                "Score: " + gameState.getScore() + "  Lives: " + gameState.getLives(),
                20,
                20
        );

        // Popup
        g.setColor(Color.YELLOW);
        for (Popup p : gameState.getPopups())
            g.drawString(p.getText(), p.getX(), p.getY());
    }
    
    //LOAD IMAGES
    private void loadImages() {
        wallImage = loadImage("/Image/wall.png");

=======
    private void loadImages() {
        wallImage = loadImage("/Image/wall.png");
        
        // Load 4 hướng Pacman
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        pacmanUp    = loadImage("/Image/pacmanUp.png");
        pacmanDown  = loadImage("/Image/pacmanDown.png");
        pacmanLeft  = loadImage("/Image/pacmanLeft.png");
        pacmanRight = loadImage("/Image/pacmanRight.png");
<<<<<<< HEAD

=======
        
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
        blueGhostImage   = loadImage("/Image/blueGhost.png");
        redGhostImage    = loadImage("/Image/redGhost.png");
        pinkGhostImage   = loadImage("/Image/pinkGhost.png");
        orangeGhostImage = loadImage("/Image/orangeGhost.png");
<<<<<<< HEAD

        cherryImage      = loadImage("/Image/cherry.png");
        scaredGhostImage = loadImage("/Image/scaredGhost.png");
    }

    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null)
            throw new RuntimeException("Cannot load image: " + path);
        return new ImageIcon(url).getImage();
    }
}
=======
        cherryImage      = loadImage("/Image/cherry.png");
        scaredGhostImage = loadImage("/Image/scaredGhost.png");
    }
    
    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Load Image Failed: " + path);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    // ĐÂY LÀ HÀM BỊ THIẾU TRƯỚC ĐÓ
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update logic
        gameEngine.update();
        
        // Kiểm tra trạng thái game để chuyển màn hình
        if (gameState.gameOver) {
            gameLoop.stop();
            new GameOverScreen(gameState.score).setVisible(true);
        } else if (gameState.foods.isEmpty()) {
             gameLoop.stop();
             if (gameState.currentLevel < MapData.getTotalLevels() - 1) {
                 new LevelWinScreen(gameState.score, gameState.currentLevel + 1, this).setVisible(true);
             } else {
                 new GameWinScreen(gameState.score).setVisible(true);
             }
        }
        
        // Vẽ lại
        repaint();
    }
    
    public void goToNextLevel() {
        gameEngine.loadLevel(gameState.currentLevel + 1, gameMap);
        gameState.lives = 3;
        gameLoop.start();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ Tường
        for (Block wall : gameState.walls)
            g.drawImage(wall.image, wall.getX(), wall.getY(), 32, 32, null);

        // Vẽ Thức ăn
        g.setColor(Color.WHITE);
        for (Block food : gameState.foods)
            g.fillRect(food.getX(), food.getY(), 4, 4);
            
        // Vẽ Cherry
        if (gameState.cherry != null) {
             g.drawImage(cherryImage, gameState.cherry.getX(), gameState.cherry.getY(), 20, 20, null);
        }

        // Vẽ Pacman (đã tự đổi hình ảnh bên trong PacmanPlayer)
        if (gameState.pacman != null)
            g.drawImage(gameState.pacman.image, gameState.pacman.getX(), gameState.pacman.getY(), 32, 32, null);

        // Vẽ Ghosts
        for (Ghost ghost : gameState.ghosts) {
            Image img = gameState.powerMode ? scaredGhostImage : ghost.image; 
            if(img == null) img = ghost.image; // fallback
            g.drawImage(img, ghost.getX(), ghost.getY(), 32, 32, null);
        }

        // Vẽ Điểm
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + gameState.score + "  Lives: " + gameState.lives, 20, 20);
        
        // Vẽ Popup
        g.setColor(Color.YELLOW);
        for(Popup p : gameState.popups) 
            g.drawString(p.getText(), p.getX(), p.getY());
    }
}
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
