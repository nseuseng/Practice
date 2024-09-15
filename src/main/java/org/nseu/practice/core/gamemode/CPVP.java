package org.nseu.practice.core.gamemode;

import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.queue.PracticeQueue;

import java.util.HashMap;

public class CPVP {

    private static PracticeQueue Ranked;
    private static PracticeQueue UnRanked;

    public static void setup() {
        Ranked = new PracticeQueue(true, GameMode.CPVP, 1);
        UnRanked = new PracticeQueue(false, GameMode.CPVP, 1);
    }

    public static PracticeQueue getRankedQueue() {
        return Ranked;
    }

    public static PracticeQueue getUnRankedQueue() {
        return UnRanked;
    }

    private static HashMap<Arena, Boolean> isLocked = new HashMap<>();

    public static Arena selectArenaAndLock() {
        for(Arena arena : isLocked.keySet()) {
            if(!isLocked.get(arena)) {
                isLocked.put(arena, true);
                return arena;
            }
        }
        return null;
    }

    public static void unlockArena(Arena arena) {
        isLocked.put(arena, false);
    }
}
