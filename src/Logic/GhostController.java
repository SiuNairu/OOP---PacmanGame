package Logic;

import Entities.Ghost;
import Entities.Block;
import Entities.PacmanPlayer;
import Utils.Direction;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;

public class GhostController {

    private final Random random = new Random();
    private final CollisionDetector collisionDetector;

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
    }
}