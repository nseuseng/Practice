package org.nseu.practice.arena;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Arena {

    private static HashMap<String, ArrayList<Arena>> arena_by_world = new HashMap<>();

    private static HashMap<String, Arena> arenas = new HashMap<>();

    public static Arena getArena(Location loc) {
        for(Arena arena : arena_by_world.getOrDefault(loc.getWorld().getName(), new ArrayList<>())) {
            if(arena.contains(loc)) {
                return arena;
            }
        }
        return null;
    }

    private boolean contains(Location loc) {
        int pos1_x = pos1.getBlockX();
        int pos2_x = pos2.getBlockX();
        int loc_x = loc.getBlockX();
        if(pos1_x > pos2_x) {
            if(loc_x > pos1_x || loc_x < pos2_x) {
                return false;
            }
        } else {
            if(loc_x < pos1_x || loc_x > pos2_x) {
                return false;
            }
        }

        int pos1_y = pos1.getBlockY();
        int pos2_y = pos2.getBlockY();
        int loc_y = loc.getBlockY();
        if(pos1_y > pos2_y) {
            if(loc_y > pos1_y || loc_y < pos2_y) {
                return false;
            }
        } else {
            if(loc_y < pos1_y || loc_y > pos2_y) {
                return false;
            }
        }

        int pos1_z = pos1.getBlockZ();
        int pos2_z = pos2.getBlockZ();
        int loc_z = loc.getBlockZ();
        if(pos1_z > pos2_z) {
            if(loc_z > pos1_z || loc_z < pos2_z) {
                return false;
            }
        } else {
            if(loc_z < pos1_z || loc_z > pos2_z) {
                return false;
            }
        }
        return true;
    }

    private Location pos1;
    private Location pos2;
    private Location spec;
    private Location team1;
    private Location team2;
    private String worldName;
    private String arenaName;
    public Arena(String arenaName, String worldName, Location pos1, Location pos2, Location team1, Location team2, Location spec) {
        this.arenaName = arenaName;
        this.worldName = worldName;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.team1 = team1;
        this.team2 = team2;
        this.spec = spec;
    }

    public Location pos1() {
        return pos1;
    }

    public Location pos2() {
        return pos2;
    }

    public Location spectate() {
        return spec;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getArenaName() {
        return arenaName;
    }

    private boolean currentlyUsed = false;

    public boolean isUsed() {
        return this.currentlyUsed;
    }

    public void lock() {
        this.currentlyUsed = true;
    }

    public void unlock() {
        this.currentlyUsed = false;
    }

    public Location getTeam1loc() {
        return team1;
    }

    public Location getTeam2loc() {
        return team2;
    }
}
