package Logic;

import Map.GameMap;

public class GameEngine {

    private final GameState state;

    private final PacmanSystem pacmanSystem;
    private final GhostSystem ghostSystem;
    private final ItemSystem itemSystem;
    private final RespawnSystem respawnSystem;

    public GameEngine(
            GameState state,
            PacmanSystem pacmanSystem,
            GhostSystem ghostSystem,
            ItemSystem itemSystem,
            RespawnSystem respawnSystem
    ) {
        this.state = state;
        this.pacmanSystem = pacmanSystem;
        this.ghostSystem = ghostSystem;
        this.itemSystem = itemSystem;
        this.respawnSystem = respawnSystem;
    }

    public void update() {
        if (state.isGameOver()) return;

        long now = System.currentTimeMillis();

        pacmanSystem.update(now);
        ghostSystem.update(now);
        itemSystem.update(now);
        respawnSystem.update(now);
        state.getPopups().removeIf(p -> p.isExpired(now));
    }

    public void loadLevel(int level, GameMap map) {
        // Reset toàn bộ state cho level mới
        state.reset();

        // Set level hiện tại (nên có setter)
        state.setCurrentLevel(level);

        // Load map tương ứng level
        map.loadMap(level, state);
        long now = System.currentTimeMillis();

        // ✅ Cherry lần đầu sau 15s
        state.setNextCherryAtMs(now + 15000);

        // Reset trạng thái game
        state.deactivatePowerMode();
    }
}
