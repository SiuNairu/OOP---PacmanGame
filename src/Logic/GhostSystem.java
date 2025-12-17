package Logic;

import Entities.Ghost;
import Utils.Popup;
import Utils.Respawn;
import java.util.*;

public class GhostSystem {

    private final GameState state;
    private final CollisionDetector collisionDetector;
    private final GhostController ghostController;
    private final List<Respawn> pendingRespawns;

    public GhostSystem(
            GameState state,
            CollisionDetector collisionDetector,
            GhostController ghostController,
            List<Respawn> pendingRespawns
    ) {
        this.state = state;
        this.collisionDetector = collisionDetector;
        this.ghostController = ghostController;
        this.pendingRespawns = pendingRespawns;
    }

    public void update(long now) {
        Set<Ghost> eaten = new HashSet<>();

        for (Ghost g : state.getGhosts()) {

            // === Collision Pacman - Ghost ===
            if (collisionDetector.checkCollision(g, state.getPacman())) {

                if (state.isPowerMode()) {
                    eaten.add(g);
                    pendingRespawns.add(new Respawn(g, now + 5000));

                    state.addScore(20);
                    state.getPopups().add(
                            new Popup(g.getX(), g.getY(), "+20", now, 900)
                    );

                } else {
                    state.loseLife();

                    if (!state.isGameOver()) {
                        resetPositions();
                    }
                    return;
                }
            }

            // === Update ghost behavior ===
            ghostController.updateGhostBehavior(
                    g,
                    state.getPacman(),
                    now,
                    state.isPowerMode(),
                    state.getWalls()
            );
        }

        state.getGhosts().removeAll(eaten);
    }

    private void resetPositions() {
        state.getPacman().reset();

        for (Ghost g : state.getGhosts()) {
            g.reset();
            ghostController.initGhostDirection(
                    g,
                    state.isPowerMode(),
                    state.getWalls()
            );
        }
    }
}
