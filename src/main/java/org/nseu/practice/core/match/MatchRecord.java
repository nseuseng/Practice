package org.nseu.practice.core.match;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.nameutil;

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
        MatchInventory matchInventory = matchInventoryHashMap.get(id);
        if(matchInventory == null) {
            Message.sendMessage(p.getUniqueId(), "해당 세션은 만료됬습니다");
            return;
        }
        InventoryGUIHolder holder = new InventoryGUIHolder(nameutil.name(matchInventory.uuid()) + " 의 인벤토리", "match_inventory", 54, nameutil.name(matchInventory.uuid()) + " 의 인벤토리");
        holder.getInventory().setContents(matchInventory.contents());
        holder.getInventory().setItem(45, matchInventory.helmet());
        holder.getInventory().setItem(46, matchInventory.chestplate());
        holder.getInventory().setItem(47, matchInventory.leggings());
        holder.getInventory().setItem(48, matchInventory.boots());
        holder.getInventory().setItem(49, matchInventory.itemInOffHand());
        ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);
        holder.getInventory().setItem(50, item);
        holder.getInventory().setItem(51, item);
        holder.getInventory().setItem(52, item);
        holder.getInventory().setItem(53, item);
        p.openInventory(holder.getInventory());
    }
}
