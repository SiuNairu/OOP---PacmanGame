package Logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Entities.Block;
import Entities.Ghost;
import Entities.PacmanPlayer;
import Utils.Popup;

public class GameState {
    public PacmanPlayer pacman;
    public Set<Block> walls = new HashSet<>();
    public Set<Block> foods = new HashSet<>();
    public Set<Ghost> ghosts = new HashSet<>();
    public Block cherry = null;
    public List<Popup> popups = new ArrayList<>();

    public int score = 0;
    public int lives = 3;
    public boolean gameOver = false;
    public int currentLevel = 0;
    
    public boolean powerMode = false;
    public long powerModeUntilMs = 0;
    public long nextCherryAtMs = 0;
    
    public void resetForNewLevel() {
        powerMode = false;
        cherry = null;
        popups.clear();
        gameOver = false;
    }
}