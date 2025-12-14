/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enities;

/**
 *
 * @author NIE NIE
 */
import java.awt.Image;
import java.util.Random;
import pacman2.MovableBlock;

public class Ghost extends MovableBlock {

    private static final Random random = new Random();

    // Cooldown change direction
    private int turnCooldownMs;
    private long lastTurnMs;

    private int normalStepFactor = 4;   // tileSize / 4
    private int scaredStepFactor = 8;   // tileSize / 8

    public Ghost(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        resetTurnCooldown();
    }

    // Velocity

    private int getStep(boolean powerMode) {
        if (tileSize <= 0) return 0;
        if (powerMode) {
            return tileSize / scaredStepFactor;
        }
        return tileSize / normalStepFactor;
    }

    public void updateVelocity(boolean powerMode) {
        int step = getStep(powerMode);
        super.updateVelocity(step);
    }

    // Direction

    public void initializeRandomDirection(char[] directions, boolean powerMode) {
        if (directions == null || directions.length == 0) return;
        char newDir = directions[random.nextInt(directions.length)];
        this.direction = newDir;
        updateVelocity(powerMode);
    }

    public void initializeRandomDirection(char[] directions) {
        initializeRandomDirection(directions, false);
    }

    public void setDirectionNoStep(char direction, boolean powerMode) {
        this.direction = direction;
        updateVelocity(powerMode);
    }

    public static boolean isOpposite(char a, char b) {
        return (a == 'U' && b == 'D') ||
               (a == 'D' && b == 'U') ||
               (a == 'L' && b == 'R') ||
               (a == 'R' && b == 'L');
    }

    // Cooldown change direction

    public void resetTurnCooldown() {
        this.turnCooldownMs = 80 + random.nextInt(71); // 80–150 ms
    }

    public boolean canTurnNow(long now) {
        if (now - lastTurnMs < turnCooldownMs) return false;
        if (!isAlignedToGrid()) return false;
        lastTurnMs = now;
        resetTurnCooldown();
        return true;
    }
}

