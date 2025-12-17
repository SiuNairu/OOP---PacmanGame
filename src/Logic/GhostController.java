package Logic;

import Entities.Ghost;
import Entities.Block;
import Entities.PacmanPlayer;
import Utils.Direction;

import java.util.*;

public class GhostController {

    private final Random random = new Random();
    private final CollisionDetector collisionDetector;
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
    }
}
