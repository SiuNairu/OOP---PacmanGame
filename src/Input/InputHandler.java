package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Entities.PacmanPlayer;
import Logic.GameState;
import Utils.Direction;

public class InputHandler implements KeyListener {
    private final GameState state;

    public InputHandler(GameState state) {
        this.state = state;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (state.pacman == null) return;

        // Ép kiểu về PacmanPlayer
        PacmanPlayer player = state.pacman;

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) 
             player.setPendingDirection(Direction.UP);
        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) 
             player.setPendingDirection(Direction.DOWN);
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) 
             player.setPendingDirection(Direction.LEFT);
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) 
             player.setPendingDirection(Direction.RIGHT);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}