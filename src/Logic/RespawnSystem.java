package Logic;

import Utils.Respawn;
import Entities.Ghost;
import java.util.*;

public class RespawnSystem {

    private final GameState state;
    private final GhostController ghostController;
    private final List<Respawn> pendingRespawns;

    public RespawnSystem(
            GameState state,
            GhostController ghostController,
            List<Respawn> pendingRespawns
    ) {
        this.state = state;
        this.ghostController = ghostController;
        this.pendingRespawns = pendingRespawns;
    }

    public void update(long now) {
        List<Respawn> done = new ArrayList<>();

        for (Respawn r : pendingRespawns) {
            if (now >= r.getAtMs()) {

                Ghost g = r.getGhost();
                g.reset();

                ghostController.initGhostDirection(
                        g,
                        state.isPowerMode(),
                        state.getWalls()
                );

                state.getGhosts().add(g);
                done.add(r);
            }
        }

        pendingRespawns.removeAll(done);
    }
}
