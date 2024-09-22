package org.nseu.practice.core.match;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.nseu.practice.core.Team;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.nameutil;

import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;

public class MatchRecord {

    private static HashMap<UUID, MatchRecord> records = new HashMap<>();

    private HashMap<UUID, MatchInventory> matchInvs = new HashMap<>();

    private Team team1;
    private Team team2;

    private UUID getNext(UUID uuid) {
        if(team1.contains(uuid)) {
            for(int i = 0; i < team1.getMembers().size(); i++) {
                if(team1.getMembers().get(i) == uuid) {
                    if(i != team1.getMembers().size() - 1) {
                        return team1.getMembers().get(i + 1);
                    } else {
                        return team2.getMembers().getFirst();
                    }
                }
            }
        } else if (team2.contains(uuid)) {
            for(int i = 0; i < team2.getMembers().size(); i++) {
                if(team2.getMembers().get(i) == uuid) {
                    if(i != team2.getMembers().size() - 1) {
                        return team2.getMembers().get(i + 1);
                    } else {
                        return team1.getMembers().getFirst();
                    }
                }
            }
        }
        return null;
    }

    private UUID getBefore(UUID uuid) {
        if(team1.contains(uuid)) {
            for(int i = 0; i < team1.getMembers().size(); i++) {
                if(team1.getMembers().get(i) == uuid) {
                    if(i != 0) {
                        return team1.getMembers().get(i - 1);
                    } else {
                        return team2.getMembers().getLast();
                    }
                }
            }
        } else if (team2.contains(uuid)) {
            for(int i = 0; i < team2.getMembers().size(); i++) {
                if(team2.getMembers().get(i) == uuid) {
                    if(i != 0) {
                        return team2.getMembers().get(i - 1);
                    } else {
                        return team1.getMembers().getLast();
                    }
                }
            }
        }
        return null;
    }

    public MatchRecord(UUID sessionID, Team team1, Team team2) {
        records.put(sessionID, this);
        this.team1 = team1;
        this.team2 = team2;
    }

    public static void openRecord(String id, String info_id, Player p) {
        MatchRecord matchRecord = records.get(UUID.fromString(id));
        if(matchRecord == null) {
            Message.sendMessage(p.getUniqueId(), "해당 세션은 만료됬습니다");
            return;
        }
        MatchInventory matchInventory = matchRecord.matchInvs.get(UUID.fromString(info_id));
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

    public void recordInventory(UUID uniqueId, MatchInventory minv) {

    }
}
