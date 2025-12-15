package Logic;

import Entities.Ghost;
import Entities.Block;
import Utils.Direction;
import java.util.Set; // Đổi import từ List sang Set
import java.util.Random;

public class GhostController {
    private final Random random = new Random();
    private final CollisionDetector collisionDetector;

    public GhostController(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector;
    }

    // Đổi List<Block> thành Set<Block> ở tham số walls
    public void updateGhostBehavior(Ghost g, long now, boolean powerMode, Set<Block> walls) {
        // 1. Kiểm tra xem có đến ngã rẽ để đổi hướng không
        if (g.canTurnNow(now)) {
             Direction[] dirs = Direction.values();
             Direction nextDir = dirs[random.nextInt(dirs.length)];
             
             if (collisionDetector.canMove(g, nextDir, walls)) {
                 g.setDirectionNoStep(nextDir, powerMode);
             }
        }
        
        // 2. Thực hiện di chuyển
        g.move();
        
        // 3. Xử lý va chạm nếu lỡ đi vào tường
        for(Block w : walls) {
            if(collisionDetector.checkCollision(g, w)) {
                g.undoMove();
                g.initializeRandomDirection(powerMode); 
                break;
            }
        }
        
        // 4. Kiểm tra biên
        collisionDetector.checkBoundaries(g);
    }
}