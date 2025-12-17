<<<<<<< HEAD
    package Logic;
=======
package Logic;
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

import Entities.Block;
import Entities.MovableBlock;
import Utils.Direction;
<<<<<<< HEAD
import java.util.Set; 
=======
import java.util.Set; // Đổi import từ List sang Set
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

public class CollisionDetector {

    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;

    public CollisionDetector(int boardWidth, int boardHeight) {
        this.BOARD_WIDTH = boardWidth;
        this.BOARD_HEIGHT = boardHeight;
    }

<<<<<<< HEAD
=======
    // Kiểm tra va chạm cơ bản giữa 2 khối
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
    public boolean checkCollision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }

<<<<<<< HEAD
=======
    // Đổi List<Block> thành Set<Block> ở tham số walls
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
    public boolean canMove(Block b, Direction dir, Set<Block> walls) {
        int x = b.getX();
        int y = b.getY();
        int step = 32 / 4; 

        switch(dir) {
            case UP: y -= step; break;
            case DOWN: y += step; break;
            case LEFT: x -= step; break;
            case RIGHT: x += step; break;
        }

        for(Block w : walls) {
            if (x < w.getX() + w.getWidth() && x + b.getWidth() > w.getX() &&
                y < w.getY() + w.getHeight() && y + b.getHeight() > w.getY()) {
                return false; 
            }
        }
        return true; 
    }

<<<<<<< HEAD
=======
    // Đổi List<Block> thành Set<Block>
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
    public void resolveWallCollision(MovableBlock b, Set<Block> walls) {
        for(Block w : walls) {
            if(checkCollision(b, w)) {
                b.undoMove();
                break;
            }
        }
    }

    public void checkBoundaries(MovableBlock b) {
        if(b.getX() < 0 || b.getX() > BOARD_WIDTH || 
           b.getY() < 0 || b.getY() > BOARD_HEIGHT) {
            b.undoMove();
        }
    }
}