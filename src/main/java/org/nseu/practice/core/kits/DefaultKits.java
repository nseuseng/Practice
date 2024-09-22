package org.nseu.practice.core.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.HashMap;

public class DefaultKits {

    private static HashMap<String, KitInventory> defaultkits = new HashMap<>();

    public static void setDefaultkit(String string, PlayerInventory inventory) {
        KitInventory kitInventory = new KitInventory(inventory);
        defaultkits.put(string, kitInventory);
    }

    public static void loadDefaultkit(Player p, String string) {
        KitInventory kitInventory = defaultkits.get(string).clone();
        defaultkits.put(string, kitInventory.clone());
        p.getInventory().setArmorContents(kitInventory.getArmors().clone());
        p.getInventory().setItemInOffHand(kitInventory.getOffhand().clone());
        p.getInventory().setContents(kitInventory.getInventory().clone());
        System.out.println(p.getName() + " -> kit " + string);
    }

    public static KitInventory fetchKit(String string) {
        if(!defaultkits.containsKey(string)) {
            return null;
        }
        return defaultkits.get(string).clone();
    }

    public static HashMap<String, String> GetDefaultKits() {
        HashMap<String, String> data = new HashMap<>();
        defaultkits.forEach((k, v) -> {
            data.put(k, v.toString());
        });
        return data;
    }

    public static void LoadDefaultKits(HashMap<String, String> data) {
        data.forEach((k, v) -> {
            try {
                defaultkits.put(k, new KitInventory(v));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
