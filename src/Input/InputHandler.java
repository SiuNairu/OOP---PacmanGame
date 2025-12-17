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
        PacmanPlayer player = state.getPacman();
        if (player == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.setPendingDirection(Direction.UP);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.setPendingDirection(Direction.DOWN);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.setPendingDirection(Direction.LEFT);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.setPendingDirection(Direction.RIGHT);
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
