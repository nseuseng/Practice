package org.nseu.practice.core.match;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.gamemode.GameMode;

import java.util.HashMap;
import java.util.UUID;

public class Session {

    private static HashMap<Party, Session> sessions = new HashMap<>();
    private static HashMap<Arena, Session> sessions2 = new HashMap<>();
    private static HashMap<UUID, Session> sessions3 = new HashMap<>();

    private Party party1;
    private Party party2;
    private Arena arena;
    private GameMode gameMode;
    private boolean isRanked;
    private int size;
    private final UUID sessionID;

    public Session(Party party1, Party party2, Arena arena, boolean isRanked, GameMode gameMode, int size) {
        this.party1 = party1;
        this.party2 = party2;
        this.arena = arena;
        this.gameMode = gameMode;
        this.isRanked = isRanked;
        this.size = size;
        this.sessionID = UUID.randomUUID();
    }

    public static void createSession(Party randomParty, Party party, Arena arena, boolean isRanked, GameMode gameMode, int size) {
        Session session = new Session(randomParty, party, arena, isRanked, gameMode, size);
        sessions.put(randomParty, session);
        sessions.put(party, session);
        sessions2.put(arena, session);
        randomParty.getAll().forEach(uuid -> {
            sessions3.put(uuid, session);
        });
        party.getAll().forEach(uuid -> {
            sessions3.put(uuid, session);
        });
    }

    public static Session getSession(UUID uuid) {
        return sessions3.get(uuid);
    }

    public static void destroySession(Session session) {
        sessions.remove(session.getParty1());
        sessions.remove(session.getParty2());
        sessions2.remove(session.getArena());
        session.getParty1().getAll().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
        session.getParty2().getAll().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
    }



    public int getSize() {
        return this.size;
    }

    public static void destroySession(Party anyParty) {
        if(!sessions.containsKey(anyParty)) {
            return;
        }
        Session session = sessions.get(anyParty);
        sessions.remove(session.getParty1());
        sessions.remove(session.getParty2());
        sessions2.remove(session.getArena());
        session.getParty1().getAll().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
        session.getParty2().getAll().forEach(uuid -> {
            if(sessions3.get(uuid).equals(session)) {
                sessions3.put(uuid, session);
            }
        });
    }

    public static Session getSession(Arena arena) {
        return sessions2.getOrDefault(arena, null);
    }

    public static Session getSession(Party party) {
        return sessions.getOrDefault(party, null);
    }

    public Party getParty(UUID uuid) {
        if(getParty1().contains(uuid)) {
            return getParty1();
        } else if(getParty2().contains(uuid)) {
            return getParty2();
        } else {
            return null;
        }
    }

    public Party getParty1() {
        return party1;
    }

    public Party getParty2() {
        return party2;
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
        MatchInventory minv = new MatchInventory(health, hunger, inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots(), inventory.getItemInOffHand(), inventory.getContents());
        minvs.put(uniqueId, minv);
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public boolean isRanked() {
        return isRanked;
    }
}
