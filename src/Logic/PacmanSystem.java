package Logic;

import Entities.Block;
import Entities.PacmanPlayer;

public class PacmanSystem {

    private final GameState state;
    private final CollisionDetector collisionDetector;

    public PacmanSystem(GameState state, CollisionDetector collisionDetector) {
        this.state = state;
        this.collisionDetector = collisionDetector;
    }

    public void update(long now) {

        PacmanPlayer pacman = state.getPacman();
        if (pacman == null) return; // ✅ FIX NPE – defensive programming

        if (pacman.getPendingDirection() != null) {
            if (collisionDetector.canMove(
                    pacman,
                    pacman.getPendingDirection(),
                    state.getWalls()
            )) {
                pacman.setDirection(pacman.getPendingDirection());
                pacman.updateSpeed();
                pacman.setPendingDirection(null);
            }
        }

        pacman.move();
        collisionDetector.resolveWallCollision(pacman, state.getWalls());
        collisionDetector.checkBoundaries(pacman);
    }
}
