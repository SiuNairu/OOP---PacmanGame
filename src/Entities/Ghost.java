package Entities;

import java.awt.Image;
import java.util.Random;
<<<<<<< HEAD
=======

>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
import Utils.Direction;

public class Ghost extends MovableBlock {

    private static final Random random = new Random();

    private int turnCooldownMs;
    private long lastTurnMs;

    private int normalStepFactor = 4;
    private int scaredStepFactor = 8;

    public Ghost(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        resetTurnCooldown();
    }

<<<<<<< HEAD
    // ===== MOVEMENT MECHANICS ONLY =====
=======
    // ===== MOVEMENT MECHANICS =====
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

    private int getStep(boolean powerMode) {
        if (getWidth() <= 0) return 0;
        return getWidth() / (powerMode ? scaredStepFactor : normalStepFactor);
    }

<<<<<<< HEAD
    public void applyDirection(Direction dir, boolean powerMode) {
        setDirection(dir);
        updateVelocity(powerMode);
    }

=======
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
    public void updateVelocity(boolean powerMode) {
        int step = getStep(powerMode);
        super.updateVelocity(step);
    }

<<<<<<< HEAD
    // ===== TURN COOLDOWN (MECHANICS, NOT AI) =====
=======
    public void initializeRandomDirection(boolean powerMode) {
        Direction[] dirs = Direction.values();
        // SỬA: Dùng setter vì direction là private
        setDirection(dirs[random.nextInt(dirs.length)]);
        updateVelocity(powerMode);
    }


    public void setDirectionNoStep(Direction direction, boolean powerMode) {
        // SỬA: Dùng setter
        setDirection(direction);
        updateVelocity(powerMode);
    }

    // Ghost chỉ THỰC THI direction được truyền vào
    public void applyDirection(Direction dir, boolean powerMode) {
        setDirection(dir);
        updateVelocity(powerMode);
    }

    // ===== TURN COOLDOWN (MECHANICS, KHÔNG PHẢI AI) =====
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407

    public boolean canTurnNow(long now) {
        if (now - lastTurnMs < turnCooldownMs) return false;
        if (!isAlignedToGrid()) return false;

        lastTurnMs = now;
        resetTurnCooldown();
        return true;
    }

    private void resetTurnCooldown() {
        this.turnCooldownMs = 80 + random.nextInt(71);
    }
<<<<<<< HEAD
}
=======


}
>>>>>>> afbe3349e4d6b845f61ec694a68c1c95fd62d407
