package Utils;

import Entities.Ghost;

public class Respawn {
    private Ghost ghost;
    private long atMs;

    public Respawn(Ghost ghost, long atMs) {
        this.ghost = ghost;
        this.atMs = atMs;
    }

    public Ghost getGhost() {
        return ghost;
    }

    public long getAtMs() {
        return atMs;
    }
}
