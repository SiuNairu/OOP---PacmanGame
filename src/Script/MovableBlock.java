package pacman2;

import Enities.Block;
import java.awt.Image;

public abstract class MovableBlock extends Block {

    // Hướng và vận tốc
    protected char direction = 'U'; // U D L R
    protected int velocityX = 0;
    protected int velocityY = 0;

    // Kích thước tile (dùng chung)
    protected static int tileSize;

    public MovableBlock(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    public static void setTileSize(int size) {
        tileSize = size;
    }

    public static int getTileSize() {
        return tileSize;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    /**
     * Cập nhật vận tốc dựa trên hướng hiện tại và bước di chuyển.
     */
    public void updateVelocity(int step) {
        switch (direction) {
            case 'U':
                velocityX = 0;
                velocityY = -step;
                break;
            case 'D':
                velocityX = 0;
                velocityY = step;
                break;
            case 'L':
                velocityX = -step;
                velocityY = 0;
                break;
            case 'R':
                velocityX = step;
                velocityY = 0;
                break;
            default:
                velocityX = 0;
                velocityY = 0;
        }
    }

    /**
     * Di chuyển theo vận tốc hiện tại.
     */
    public void move() {
        setX(getX() + velocityX);
        setY(getY() + velocityY);
    }

    /**
     * Đang nằm thẳng hàng với lưới (chia theo 1/4 tile) không.
     * Dùng cho logic rẽ hướng của Ghost.
     */
    public boolean isAlignedToGrid() {
        if (tileSize == 0) return true;
        return (getX() % (tileSize / 4) == 0) && (getY() % (tileSize / 4) == 0);
    }

    @Override
    public void reset() {
        super.reset();
        velocityX = 0;
        velocityY = 0;
        direction = 'U';
    }
}
