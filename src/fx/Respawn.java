/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fx;

import Enities.Ghost;
import Enities.Ghost;

/**
 *
 * @author NIE NIE
 */
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
