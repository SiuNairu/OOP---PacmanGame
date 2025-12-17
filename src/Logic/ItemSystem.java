package Logic;

import Entities.Block;
import Entities.Ghost;
import java.util.*;

public class ItemSystem {

    private final GameState state;
    private final CollisionDetector collisionDetector;
    private final Random random = new Random();
    private static final int TILE_SIZE = 32;

    public ItemSystem(GameState state, CollisionDetector collisionDetector) {
        this.state = state;
        this.collisionDetector = collisionDetector;
    }

    public void update(long now) {

        if (state.isPowerMode() && now >= state.getPowerModeUntilMs()) {
            state.deactivatePowerMode();
            for (Ghost g : state.getGhosts()) g.resetImage();
        }

        eatFood();
        eatCherry(now);

        if (state.getCherry() == null && now >= state.getNextCherryAtMs()) {
            spawnCherry(now);
        }
    }


    private void eatFood() {
        Block eaten = null;

        for (Block f : state.getFoods()) {
            if (collisionDetector.checkCollision(state.getPacman(), f)) {
                eaten = f;
                state.addScore(10);
                break;
            }
        }

        if (eaten != null) {
            state.getFoods().remove(eaten);
        }
    }

    private void eatCherry(long now) {
        if (state.getCherry() != null &&
            collisionDetector.checkCollision(state.getPacman(), state.getCherry())) {

            state.activatePowerMode(now + 7000);
            state.setCherry(null);

            // set thời điểm spawn cherry tiếp theo
            state.setNextCherryAtMs(
                now + 15000 + random.nextInt(15000)
            );
        }
    }

    private void spawnCherry(long now) {
        if (state.getFoods().isEmpty()) return;

        Block spot = new ArrayList<>(state.getFoods())
                .get(random.nextInt(state.getFoods().size()));

        int x = (spot.getX() / TILE_SIZE) * TILE_SIZE;
        int y = (spot.getY() / TILE_SIZE) * TILE_SIZE;

        state.setCherry(new Block(null, x + 6, y + 6, 20, 20));
    }
}
