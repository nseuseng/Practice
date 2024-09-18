package org.nseu.practice.arena;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.nseu.practice.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Arena {

    private static HashMap<String, ArrayList<Arena>> arena_by_world = new HashMap<>();

    private static HashMap<String, Arena> arenas = new HashMap<>();

    public static HashMap<String, HashMap<String, String>> data() {
        HashMap<String, HashMap<String, String>> data = new HashMap<>();
        arenas.forEach((k, v) -> {
            data.put(k, v.serialize());
        });
        return data;
    }

    public static void load(HashMap<String, HashMap<String, String>> data) {
        data.forEach((k, v) -> {
            Arena arena = deserialize(v);
            arenas.put(k, arena);
            ArrayList<Arena> arenalist = arena_by_world.getOrDefault(arena.getWorldName(), new ArrayList<>());
            if(!arenalist.contains(arena)) {
                arenalist.add(arena);
            }
            arena_by_world.put(arena.getWorldName(), arenalist);
        });
    }

    public Arena(String arenaName) {
        this.arenaName = arenaName;
    }

    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        {
            data.put("arenaName", this.arenaName);
            data.put("worldName", this.worldName);
            data.put("pos1", Util.Loc.toString(this.pos1));
            data.put("pos2", Util.Loc.toString(this.pos2));
            data.put("team1", Util.Loc.toString(this.team1));
            data.put("team2", Util.Loc.toString(this.team2));
            data.put("spec", Util.Loc.toString(this.spec));
        }
        return data;
    }

    public static Arena deserialize(HashMap<String, String> data) {
        return new Arena(data.get("arenaName"), data.get("worldName"), Util.Loc.fromString(data.get("pos1")), Util.Loc.fromString(data.get("pos2")), Util.Loc.fromString(data.get("team1")), Util.Loc.fromString(data.get("team2")), Util.Loc.fromString(data.get("spec")));
    }

    public static Arena getArena(Location loc) {
        for(Arena arena : arena_by_world.getOrDefault(loc.getWorld().getName(), new ArrayList<>())) {
            if(arena.contains(loc)) {
                return arena;
            }
        }
        return null;
    }

    public static void addArena(String arg, Arena arena) {
        arenas.put(arg, arena);
    }

    public static Arena getArenaByName(String arg) {
        return arenas.get(arg);
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

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void setTeam1(Location team1) {
        this.team1 = team1;
    }

    public void setTeam2(Location team2) {
        this.team2 = team2;
    }

    public void setSpec(Location spec) {
        this.spec = spec;
    }

    public void setWorldName(Location loc) {
        ArrayList<Arena> arenalist = arena_by_world.getOrDefault(loc.getWorld().getName(), new ArrayList<>());
        if(!arenalist.contains(this)) {
            arenalist.add(this);
        }
        arena_by_world.put(loc.getWorld().getName(), arenalist);
        this.worldName = loc.getWorld().getName();
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
