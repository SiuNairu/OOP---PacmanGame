package Entities;

import java.awt.Image;

public class Block {
    private int x;
    private int y;
    private int width;
    private int height;

    public Image image;
    public Image normalImage;

    // SỬA: Chuyển sang private để đảm bảo tính đóng gói
    private final int startX;
    private final int startY;

    public Block(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.normalImage = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    // Getters
    public int getX()        { return x; }
    public int getY()        { return y; }
    public int getWidth()    { return width; }
    public int getHeight()   { return height; }

    // Setters
    public void setX(int x)  { this.x = x; }
    public void setY(int y)  { this.y = y; }

    public void setImage(Image image) {
        this.image = image;
    }

    public void resetImage() {
        this.image = this.normalImage;
    }

    // Reset back to the original location
    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}