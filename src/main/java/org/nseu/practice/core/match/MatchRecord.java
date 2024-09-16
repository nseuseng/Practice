package org.nseu.practice.core.match;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;

public class MatchRecord {

    private static WeakHashMap<String, MatchInventory> matchInventoryHashMap = new WeakHashMap<>();

    public static MatchInventory getMatchInventory(UUID sessionid, UUID playerid) {
        return matchInventoryHashMap.get(sessionid.toString() + playerid.toString());
    }

    public static void setMatchInventory(UUID sessionid, UUID playerid, MatchInventory matchInventory) {
        matchInventoryHashMap.put(sessionid.toString() + playerid.toString(), matchInventory);
    }

    public static void openRecord(String id, Player p) {

    }
}
