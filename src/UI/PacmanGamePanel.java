package UI;

import Entities.Block;
import Entities.Ghost;
import Input.InputHandler;
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

public class PacmanGamePanel extends JPanel implements ActionListener {

    private final GameState gameState;
    private final GameEngine gameEngine;
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        loadImages();
        
        // Truyền 4 ảnh vào GameMap để tạo Pacman
        gameMap = new GameMap(32, wallImage, redGhostImage, blueGhostImage, pinkGhostImage, orangeGhostImage, 
                              pacmanUp, pacmanDown, pacmanLeft, pacmanRight);
        
        // Bắt đầu game
        gameEngine.loadLevel(0, gameMap);
        
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    private void loadImages() {
        wallImage = loadImage("/Image/wall.png");
        
        // Load 4 hướng Pacman
        pacmanUp    = loadImage("/Image/pacmanUp.png");
        pacmanDown  = loadImage("/Image/pacmanDown.png");
        pacmanLeft  = loadImage("/Image/pacmanLeft.png");
        pacmanRight = loadImage("/Image/pacmanRight.png");
        
        blueGhostImage   = loadImage("/Image/blueGhost.png");
        redGhostImage    = loadImage("/Image/redGhost.png");
        pinkGhostImage   = loadImage("/Image/pinkGhost.png");
        orangeGhostImage = loadImage("/Image/orangeGhost.png");
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