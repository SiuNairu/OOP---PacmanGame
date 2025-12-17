package UI;

import Entities.Block;
import Entities.Ghost;
import Input.InputHandler;

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

public class PacmanGamePanel extends JPanel implements ActionListener {

    private final GameState gameState;
    private final GameEngine gameEngine;

    private GameMap gameMap;
    private final Timer gameLoop;

    // Images
    private Image wallImage, blueGhostImage, redGhostImage,
                  pinkGhostImage, orangeGhostImage,
                  cherryImage, scaredGhostImage;

    private Image pacmanUp, pacmanDown, pacmanLeft, pacmanRight;

    public PacmanGamePanel() {

        // UI
        setPreferredSize(new Dimension(19 * 32, 21 * 32));
        setBackground(Color.BLACK);
        setFocusable(true);

        // STATE
        gameState = new GameState();

        // RESOURCE
        loadImages();

        // MAP
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

        // INPUT
        addKeyListener(new InputHandler(gameState));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        // START GAME
        gameEngine.loadLevel(0, gameMap);

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

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

    // RENDER
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

    // LOAD IMAGES
    private void loadImages() {
        wallImage = loadImage("/Image/wall.png");

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
        if (url == null)
            throw new RuntimeException("Cannot load image: " + path);
        return new ImageIcon(url).getImage();
    }
}
