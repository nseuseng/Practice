package org.nseu.practice.core.match;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.Team;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.player.PracticePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private MatchRecord matchRecord;

    private ArrayList<UUID> spectators = new ArrayList<>();

    public Session(Team team1, Team team2, Arena arena, boolean isRanked, GameMode gameMode, int size) {
        this.team1 = team1;
        this.team2 = team2;
        System.out.println(team1.getMembers());
        System.out.println(team2.getMembers());
        this.arena = arena;
        this.gameMode = gameMode;
        this.isRanked = isRanked;
        this.size = size;
        this.sessionID = UUID.randomUUID();
        this.matchRecord = new MatchRecord(this.sessionID, this.team1, this.team2);
    }

    public void unHideAll() {
        spectators.forEach(s -> {

            OfflinePlayer op = Bukkit.getOfflinePlayer(s);
            if(op.isOnline()) {
                Perform.stopSpectating(op.getPlayer());
                PracticePlayer.getPlayer(op.getUniqueId()).unhideFromAll();
                PracticePlayer.getPlayer(op.getUniqueId()).unhideAll();
            }
        });
    }

    public ArrayList<UUID> getSpectators() {
        return spectators;
    }

    public void addSpectator(UUID uuid) {
        spectators.add(uuid);
    }

    public void removeSpectator(UUID uuid) {
        spectators.remove(uuid);
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

    public static Session currentlyPlaying(UUID uuid) {
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


    public static Session currentlyPlaying(Arena arena) {
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

    public GameMode getGameMode() {
        return gameMode;
    }



    public void recordInventory(UUID uniqueId, double health, double hunger, PlayerInventory inventory) {
        MatchInventory minv = new MatchInventory(uniqueId, health, hunger, inventory.getHelmet() != null ? inventory.getHelmet() : new ItemStack(Material.AIR), inventory.getChestplate() != null ? inventory.getChestplate() : new ItemStack(Material.AIR), inventory.getLeggings() != null ? inventory.getLeggings() : new ItemStack(Material.AIR), inventory.getBoots() != null ? inventory.getBoots() : new ItemStack(Material.AIR), inventory.getItemInOffHand() != null ? inventory.getItemInOffHand() : new ItemStack(Material.AIR), inventory.getContents());
        matchRecord.recordInventory(uniqueId, minv);
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
