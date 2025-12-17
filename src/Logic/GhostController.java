package Logic;

import Entities.Ghost;
import Entities.Block;
import Entities.PacmanPlayer;
import Utils.Direction;

<<<<<<< HEAD
import java.util.*;
=======
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

public class GhostController {

    private final Random random = new Random();
    private final CollisionDetector collisionDetector;
<<<<<<< HEAD
    private final int TILE_SIZE; // no hardcoded 32

    public GhostController(CollisionDetector collisionDetector, int tileSize) {
        this.collisionDetector = collisionDetector;
        this.TILE_SIZE = tileSize;
    }

    // ========== PUBLIC API (one job: update ghost behavior) ==========

    public void updateGhostBehavior(Ghost g, PacmanPlayer pacman, long now, boolean powerMode, Set<Block> walls) {
        tryTurn(g, pacman, now, powerMode, walls);
        stepMove(g);
        recoverIfHitWall(g, powerMode, walls);
        collisionDetector.checkBoundaries(g);
    }

    // Optional: move your old Ghost.initializeRandomDirection() into controller (SRP)
    public void initGhostDirection(Ghost g, boolean powerMode, Set<Block> walls) {
        Direction d = pickRandomValidDirection(g, walls);
        if (d == null) d = Direction.UP;
        g.applyDirection(d, powerMode);
    }

    // ========== SRP helpers (small, single-purpose methods) ==========

    private void tryTurn(Ghost g, PacmanPlayer pacman, long now, boolean powerMode, Set<Block> walls) {
        if (!g.canTurnNow(now)) return;

        Direction chosen = chooseDirection(g, pacman, powerMode, walls);
        if (chosen != null) {
            g.applyDirection(chosen, powerMode);
        }
    }

    private void stepMove(Ghost g) {
        g.move();
    }

    private void recoverIfHitWall(Ghost g, boolean powerMode, Set<Block> walls) {
        if (!isCollidingWithAnyWall(g, walls)) return;

        g.undoMove();

        Direction fallback = pickRandomValidDirection(g, walls);
        if (fallback != null) {
            g.applyDirection(fallback, powerMode);
        }
    }

    private boolean isCollidingWithAnyWall(Ghost g, Set<Block> walls) {
        for (Block w : walls) {
            if (collisionDetector.checkCollision(g, w)) return true;
        }
        return false;
    }

    // ========== AI DECISION (still inside controller; SRP = “ghost behavior”) ==========

    private Direction chooseDirection(Ghost g, PacmanPlayer pacman, boolean powerMode, Set<Block> walls) {
        if (powerMode) return pickRandomValidDirection(g, walls);

        int chance = random.nextInt(100);
        if (chance < 60) return pickRandomValidDirection(g, walls);

        return pickTargetingDirection(g, pacman, walls);
    }

    private Direction pickTargetingDirection(Ghost g, PacmanPlayer target, Set<Block> walls) {
        Direction current = g.getDirection();
        Direction opposite = (current != null) ? current.opposite() : null;

        Direction best = null;
        double bestDist = Double.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            if (dir == opposite) continue;
            if (!collisionDetector.canMove(g, dir, walls)) continue;

            int nextX = g.getX();
            int nextY = g.getY();

            switch (dir) {
                case UP:    nextY -= TILE_SIZE; break;
                case DOWN:  nextY += TILE_SIZE; break;
                case LEFT:  nextX -= TILE_SIZE; break;
                case RIGHT: nextX += TILE_SIZE; break;
            }

            double distSq = Math.pow(nextX - target.getX(), 2) + Math.pow(nextY - target.getY(), 2);
            if (distSq < bestDist) {
                bestDist = distSq;
                best = dir;
            }
        }

        return (best != null) ? best : opposite;
    }

    private Direction pickRandomValidDirection(Ghost g, Set<Block> walls) {
        List<Direction> valid = new ArrayList<>();
        Direction opposite = (g.getDirection() != null) ? g.getDirection().opposite() : null;

        for (Direction dir : Direction.values()) {
            if (dir == opposite) continue;
            if (collisionDetector.canMove(g, dir, walls)) valid.add(dir);
        }

        if (valid.isEmpty()) return opposite;
        return valid.get(random.nextInt(valid.size()));
=======

    public GhostController(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    // ===== DECISION LOGIC =====

//    public Direction pickRandomValidDirection(Ghost g, Set<Block> walls) {
//        Direction[] dirs = Direction.values();
//
//        for (int i = 0; i < 10; i++) {
//            Direction d = dirs[random.nextInt(dirs.length)];
//            if (collisionDetector.canMove(g, d, walls)) {
//                return d;
//            }
//        }
//        return null;
//    }

    // ===== MAIN UPDATE =====

    public void updateGhostBehavior(Ghost g, PacmanPlayer pacman, long now, boolean powerMode, Set<Block> walls) {
        if (g.canTurnNow(now)) {
            Direction bestDir;

            if (powerMode) {

                bestDir = getRandomValidDirection(g, walls);
            } else {
                int chance = random.nextInt(100);
                if (chance < 60) {
                    bestDir = getRandomValidDirection(g, walls);
                } else {
                    bestDir = getTargetingDirection(g, pacman, walls);
                }
            }

            if (bestDir != null) {
                g.setDirectionNoStep(bestDir, powerMode);
            }
        }

        // 2. Di chuyển
        g.move();

        // 3. Va chạm tường → controller chọn hướng khác
        for (Block w : walls) {
            if (collisionDetector.checkCollision(g, w)) {
                g.undoMove();

                Direction next = getRandomValidDirection(g, walls);
                if (next != null) {
                    g.applyDirection(next, powerMode);
                }
                break;
            }
        }

        // 4. Kiểm tra biên
        collisionDetector.checkBoundaries(g);
    }

    private Direction getTargetingDirection(Ghost g, PacmanPlayer target, Set<Block> walls) {
        Direction currentDir = g.getDirection();
        Direction oppositeDir = (currentDir != null) ? currentDir.opposite() : null;

        Direction bestDir = null;
        double minDistance = Double.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            if (dir == oppositeDir) continue;

            if (collisionDetector.canMove(g, dir, walls)) {


                int nextX = g.getX();
                int nextY = g.getY();

                switch (dir) {
                    case UP:    nextY -= 32; break;
                    case DOWN:  nextY += 32; break;
                    case LEFT:  nextX -= 32; break;
                    case RIGHT: nextX += 32; break;
                }

                double distSq = Math.pow(nextX - target.getX(), 2) + Math.pow(nextY - target.getY(), 2);

                if (distSq < minDistance) {
                    minDistance = distSq;
                    bestDir = dir;
                }
            }
        }

        if (bestDir == null) {
            return oppositeDir;
        }

        return bestDir;
    }

    private Direction getRandomValidDirection(Ghost g, Set<Block> walls) {
        List<Direction> validDirs = new ArrayList<>();
        Direction opposite = (g.getDirection() != null) ? g.getDirection().opposite() : null;

        for (Direction dir : Direction.values()) {
            if (dir != opposite && collisionDetector.canMove(g, dir, walls)) {
                validDirs.add(dir);
            }
        }

        if (validDirs.isEmpty()) return opposite;

        return validDirs.get(random.nextInt(validDirs.size()));
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
    }
}