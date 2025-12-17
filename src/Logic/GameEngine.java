package Logic;

import Map.GameMap;
import Utils.Popup;
import Utils.Respawn;

import java.util.*;

import Entities.Block;
import Entities.Ghost;


public class GameEngine {
    private final GameState state;
    private final Random random = new Random();
    private final List<Respawn> pendingRespawns = new ArrayList<>();
    
    private final CollisionDetector collisionDetector;
    private final GhostController ghostController;
    
    private final int TILE_SIZE = 32;
    private final int ROWS = 21;
    private final int COLS = 19;
    private final int BOARD_WIDTH = COLS * TILE_SIZE;
    private final int BOARD_HEIGHT = ROWS * TILE_SIZE;

    public GameEngine(GameState state) {
        this.state = state;
        this.collisionDetector = new CollisionDetector(BOARD_WIDTH, BOARD_HEIGHT);
        this.ghostController = new GhostController(this.collisionDetector);
    }

    public void loadLevel(int level, GameMap gameMap) {
        state.currentLevel = level;
        gameMap.loadMap(level);
        state.pacman = gameMap.getPacman();
        state.walls = gameMap.getWalls(); // Hàm này trả về Set<Block>
        state.foods = gameMap.getFoods();
        state.ghosts = gameMap.getGhosts();
        
        for (Ghost g : state.ghosts) {
            g.initializeRandomDirection(state.powerMode);
        }
        
        state.resetForNewLevel();
        state.nextCherryAtMs = System.currentTimeMillis() + 12000 + random.nextInt(8000);
    }

    public void update() {
        if (state.gameOver) return;
        long now = System.currentTimeMillis();

        // 1. Move Pacman
        if (state.pacman.getPendingDirection() != null) { 
             // state.walls là Set, giờ truyền vào canMove (nhận Set) là hợp lệ
             if(collisionDetector.canMove(state.pacman, state.pacman.getPendingDirection(), state.walls)) {
                  state.pacman.setDirection(state.pacman.getPendingDirection());
                  state.pacman.updateSpeed();
                  state.pacman.setPendingDirection(null);
             }
        }
        state.pacman.move();
        collisionDetector.resolveWallCollision(state.pacman, state.walls);
        collisionDetector.checkBoundaries(state.pacman);

        // 2. Power Mode
        if (state.powerMode && now >= state.powerModeUntilMs) {
            state.powerMode = false;
            for(Ghost g : state.ghosts) g.resetImage();
        }
        if (state.cherry == null && now >= state.nextCherryAtMs) spawnCherry();

        // 3. Move Ghosts
        updateGhosts(now);

        // 4. Eat Food
        Block eaten = null;
        for (Block f : state.foods) {
            if (collisionDetector.checkCollision(state.pacman, f)) {
                eaten = f;
                state.score += 10;
            }
        }
        if (eaten != null) state.foods.remove(eaten);
        
        // 5. Eat Cherry
        if (state.cherry != null && collisionDetector.checkCollision(state.pacman, state.cherry)) {
            state.powerMode = true;
            state.powerModeUntilMs = now + 7000;
            state.cherry = null;
            state.nextCherryAtMs = now + 15000 + random.nextInt(15000);
        }
        
        // 6. Cleanup
        handleRespawns(now);
        state.popups.removeIf(p -> now - p.getCreatedMs() >= p.getDurationMs());
    }

    private void updateGhosts(long now) {
        Set<Ghost> eatenGhosts = new HashSet<>();
        
        for (Ghost g : state.ghosts) {
            if (collisionDetector.checkCollision(g, state.pacman)) {
                if (state.powerMode) {
                    pendingRespawns.add(new Respawn(g, now + 5000));
                    state.score += 20;
                    state.popups.add(new Popup(g.getX(), g.getY(), "+20", now, 900));
                    eatenGhosts.add(g);
                } else {
                    state.lives--;
                    if (state.lives == 0) state.gameOver = true;
                    else resetPositions();
                    return; 
                }
            }
            
            // state.walls là Set, truyền vào updateGhostBehavior (nhận Set) là hợp lệ
            ghostController.updateGhostBehavior(g, state.pacman, now, state.powerMode, state.walls);
        }
        state.ghosts.removeAll(eatenGhosts);
    }
    
    private void spawnCherry() {
        if (state.foods.isEmpty()) return;
        List<Block> safeSpots = new ArrayList<>(state.foods);
        Block randomSpot = safeSpots.get(random.nextInt(safeSpots.size()));

        int gridX = (randomSpot.getX() / TILE_SIZE) * TILE_SIZE;
        int gridY = (randomSpot.getY() / TILE_SIZE) * TILE_SIZE;

        state.cherry = new Block(null, gridX + 6, gridY + 6, 20, 20);
    }

    private void resetPositions() {
        state.pacman.reset();
        for(Ghost g : state.ghosts) {
            g.reset();
            g.initializeRandomDirection(state.powerMode);
        }
    }
    
    private void handleRespawns(long now) {
        List<Respawn> done = new ArrayList<>();
        for (Respawn r : pendingRespawns) {
            if (now >= r.getAtMs()) {
                Ghost g = r.getGhost();
                g.reset();
                g.initializeRandomDirection(state.powerMode);
                state.ghosts.add(g);
                done.add(r);
            }
        }
        pendingRespawns.removeAll(done);
    }
}