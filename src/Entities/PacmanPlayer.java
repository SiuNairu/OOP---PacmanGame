package Entities;

import java.awt.Image;

import Utils.Direction;

public class PacmanPlayer extends MovableBlock {

    private Direction pendingDirection = null; 
    private final int speedFactor = 4;
    
    // Lưu trữ 4 trạng thái hình ảnh
    private final Image up, down, left, right;

    // Constructor nhận vào 4 ảnh thay vì 1
    public PacmanPlayer(Image up, Image down, Image left, Image right, int x, int y, int width, int height) {
        // Mặc định khởi đầu dùng ảnh RIGHT
        super(right, x, y, width, height);
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public Direction getPendingDirection() {
        return pendingDirection;
    }

    public void setPendingDirection(Direction pendingDirection) {
        this.pendingDirection = pendingDirection;
    }

    public void updateSpeed() {
        int step = getWidth() / speedFactor;
        updateVelocity(step);
    }
    
    // Override hàm setDirection của cha (MovableBlock)
    // Để mỗi khi hướng thay đổi -> Hình ảnh cũng tự đổi theo
    @Override
    public void setDirection(Direction newDir) {
        super.setDirection(newDir); // Gọi logic setDirection gốc để chỉnh velocity
        updateImage(); // Gọi hàm đổi ảnh
    }

    private void updateImage() {
        Direction dir = getDirection();
        if (dir == null) return;

        switch (dir) {
            case UP:    this.image = this.up;    break;
            case DOWN:  this.image = this.down;  break;
            case LEFT:  this.image = this.left;  break;
            case RIGHT: this.image = this.right; break;
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        pendingDirection = null;
        this.image = this.right; // Reset về ảnh mặc định
    }
}