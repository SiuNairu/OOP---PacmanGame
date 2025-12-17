package Logic;

    import java.util.*;

    import Entities.Block;
    import Entities.Ghost;
    import Entities.PacmanPlayer;
    import Utils.Popup;

    public class GameState {

        // === Entities ===
        private PacmanPlayer pacman;
        private final Set<Block> walls = new HashSet<>();
        private final Set<Block> foods = new HashSet<>();
        private final Set<Ghost> ghosts = new HashSet<>();
        private Block cherry;
        private final List<Popup> popups = new ArrayList<>();

        // === Game info ===
        private int score = 0;
        private int lives = 3;
        private boolean gameOver = false;
        private int currentLevel = 0;

        // === Power mode ===
        private boolean powerMode = false;
        private long powerModeUntilMs = 0;
        private long nextCherryAtMs = 0;

        // ================= GETTERS =================
        public PacmanPlayer getPacman() {
            return pacman;
        }

        public Set<Block> getWalls() {
            return walls;
        }

        public Set<Block> getFoods() {
            return foods;
        }

        public Set<Ghost> getGhosts() {
            return ghosts;
        }

        public Block getCherry() {
            return cherry;
        }

        public List<Popup> getPopups() {
            return popups;
        }

        public int getScore() {
            return score;
        }

        public int getLives() {
            return lives;
        }

        public boolean isGameOver() {
            return gameOver;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        public boolean isPowerMode() {
            return powerMode;
        }

        public long getPowerModeUntilMs() {
            return powerModeUntilMs;
        }

        public long getNextCherryAtMs() {
            return nextCherryAtMs;
        }

        // ================= SETTERS / ACTIONS =================
        public void setPacman(PacmanPlayer pacman) {
            this.pacman = pacman;
        }

        public void setCurrentLevel(int currentLevel) {
            this.currentLevel = currentLevel;
        }

        public void setCherry(Block cherry) {
            this.cherry = cherry;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setLives(int lives) {
            this.lives = lives;
        }

        public void setGameOver(boolean gameOver) {
            this.gameOver = gameOver;
        }

        public void setPowerMode(boolean powerMode) {
            this.powerMode = powerMode;
        }

        public void setPowerModeUntilMs(long powerModeUntilMs) {
            this.powerModeUntilMs = powerModeUntilMs;
        }

        public void setNextCherryAtMs(long nextCherryAtMs) {
            this.nextCherryAtMs = nextCherryAtMs;
        }

        public void addScore(int amount) {
            score += amount;
        }

        public void loseLife() {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            }
        }

        public void activatePowerMode(long untilMs) {
            powerMode = true;
            powerModeUntilMs = untilMs;
        }

        public void deactivatePowerMode() {
            powerMode = false;
            powerModeUntilMs = 0;
        }

        // ================= RESET =================
        public void resetForNewLevel() {
            powerMode = false;
            cherry = null;
            popups.clear();
            gameOver = false;
        }

        public void reset() {
            walls.clear();
            foods.clear();
            ghosts.clear();
            popups.clear();

            pacman = null;
            cherry = null;

            score = 0;
            lives = 3;
            gameOver = false;
            powerMode = false;
            nextCherryAtMs = Long.MAX_VALUE;
        }
    }
