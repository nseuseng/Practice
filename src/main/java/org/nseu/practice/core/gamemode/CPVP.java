package org.nseu.practice.core.gamemode;

import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.queue.PracticeQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private static HashMap<String, Boolean> isLocked = new HashMap<>();

    public static ArrayList<String> getArenas() {
        return new ArrayList<>(isLocked.keySet());
    }

    public static void loadArenas(ArrayList<String> arenas) {
        arenas.forEach(s -> {
            isLocked.put(s, false);
        });
    }

    public static Arena selectArenaAndLock() {
        for(String arenaName : isLocked.keySet()) {
            if(!isLocked.get(arenaName)) {
                isLocked.put(arenaName, true);
                Arena arena = Arena.getArenaByName(arenaName);
                arena.lock();
                return arena;
            }
        }
        return null;
    }

    public static void unlockArena(String arenaName) {
        isLocked.put(arenaName, false);
        Arena.getArenaByName(arenaName).unlock();
    }

    public static List<Arena> getActiveArenas() {
        List<Arena> list = new ArrayList<>();
        isLocked.forEach((k, v) -> {
            if(v) {
               list.add(Arena.getArenaByName(k));
            }
        });
        return list;
    }

    public static void addArena(String arenaName) {
        isLocked.put(arenaName, false);
    }
}
