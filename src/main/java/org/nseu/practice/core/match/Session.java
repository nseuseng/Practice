package org.nseu.practice.core.match;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.PlayerInventory;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.Team;
import org.nseu.practice.core.gamemode.GameMode;

import java.util.HashMap;
import java.util.UUID;

public class Session {
    private static HashMap<Arena, Session> sessions2 = new HashMap<>();
    private static HashMap<UUID, Session> sessions3 = new HashMap<>();

    private Team team1;
    private Team team2;
    private Arena arena;
    private GameMode gameMode;
    private boolean isRanked;
    private int size;
    private final UUID sessionID;

    public Session(Team team1, Team team2, Arena arena, boolean isRanked, GameMode gameMode, int size) {
        this.team1 = team1;
        this.team2 = team2;
        this.arena = arena;
        this.gameMode = gameMode;
        this.isRanked = isRanked;
        this.size = size;
        this.sessionID = UUID.randomUUID();
    }

    public static void createSession(Team randomTeam, Team team, Arena arena, boolean isRanked, GameMode gameMode, int size) {
        Session session = new Session(randomTeam, team, arena, isRanked, gameMode, size);
        sessions2.put(arena, session);
        randomTeam.getMembers().forEach(uuid -> {
            sessions3.put(uuid, session);
        });
        team.getMembers().forEach(uuid -> {
            sessions3.put(uuid, session);
        });
    }

    public static Session getSession(UUID uuid) {
        return sessions3.get(uuid);
    }

    public static void destroySession(Session session) {
        sessions2.remove(session.getArena());
        session.getTeam1().getMembers().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
        session.getTeam2().getMembers().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
    }



    public int getSize() {
        return this.size;
    }


    public static Session getSession(Arena arena) {
        return sessions2.getOrDefault(arena, null);
    }

    public Team getTeam(UUID uuid) {
        if(getTeam1().contains(uuid)) {
            return getTeam1();
        } else if(getTeam2().contains(uuid)) {
            return getTeam2();
        } else {
            return null;
        }
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Arena getArena() {
        return arena;
    }

    private HashMap<Location, BlockData> records = new HashMap<>();

    public void record(Location location, BlockData blockData) {
        if(!records.containsKey(location)) {
            records.put(location, blockData);
        }
    }

    public void revertWorld() {
        records.forEach((location, blockData) -> {
            location.getBlock().setBlockData(blockData);
        });
    }

    public void recordInventoryToMatchRecord() {
        minvs.forEach((k, v) -> {
            MatchRecord.setMatchInventory(this.sessionID, k, v);
        });
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private HashMap<UUID, MatchInventory> minvs = new HashMap<>();

    public void recordInventory(UUID uniqueId, double health, double hunger, PlayerInventory inventory) {
        MatchInventory minv = new MatchInventory(uniqueId, health, hunger, inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots(), inventory.getItemInOffHand(), inventory.getContents());
        minvs.put(uniqueId, minv);
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public boolean isRanked() {
        return isRanked;
    }

    public void midPlayerLeave(UUID uuid) {

        boolean alldown = this.getTeam(uuid).isAllNotPlaying();
        boolean result = !this.getTeam1().contains(uuid);
        if(alldown) {
            Perform.endMatch(this, result);
        }
    }
}
