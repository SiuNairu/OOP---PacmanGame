package Enities;

import java.awt.Image;
import pacman2.MovableBlock;

public class PacmanPlayer extends MovableBlock {

    private char pendingDirection = 0;
    // Speed
    private final int speedFactor = 4;

    public PacmanPlayer(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    public char getPendingDirection() {
        return pendingDirection;
    }

    public void setPendingDirection(char pendingDirection) {
        this.pendingDirection = pendingDirection;
    }

    // Update speed
    public void updateSpeed() {
        int step = MovableBlock.getTileSize() / speedFactor;
        updateVelocity(step);
    }
}
