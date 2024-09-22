package org.nseu.practice.core.kits;

import org.bukkit.inventory.Inventory;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.util.BukkitSerialization;
import org.nseu.practice.util.InventoryGUIHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class KitConfig {

    private HashMap<String, HashMap<String, KitInventory>> kits = new HashMap<>();

    private final UUID uuid;

    public KitConfig(UUID uuid) {
        this.uuid = uuid;
    }

    public HashMap<String, HashMap<String, String>> serialize() {
        HashMap<String, HashMap<String, String>> data = new HashMap<>();
        kits.forEach((k, v) -> {
            HashMap<String, String> innerdata = new HashMap<>();
            v.forEach((v1, v2) -> {
                innerdata.put(v1, v2.toString());
            });
            data.put(k, innerdata);
        });
        return data;
    }

    public void deserialize(HashMap<String, HashMap<String, String>> data) {
        data.forEach((k, v) -> {
            HashMap<String, KitInventory> innerdata = new HashMap<>();
            v.forEach((v1, v2) -> {
                try {
                    innerdata.put(v1, new KitInventory(v2));
                } catch (IOException e) {
                    System.out.println("exception while serializing -> " + v2);
                    throw new RuntimeException(e);
                }
            });
            kits.put(k, innerdata);
        });
    }

    public UUID getUuid() {
        return uuid;
    }
}
