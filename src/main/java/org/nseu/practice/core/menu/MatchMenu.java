package org.nseu.practice.core.menu;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.match.Session;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.itemutils;

import java.util.ArrayList;
import java.util.List;

public class MatchMenu {

    private static InventoryGUIHolder holder;

    public static void start() {
        holder = new InventoryGUIHolder("대전 메뉴", "duels_menu_unranked", 27, "대전 메뉴");
        ItemStack cpvp = itemutils.createItem("&3&lCPVP", Material.END_CRYSTAL);
        holder.getInventory().setItem(0, cpvp);

        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack cpvp_1 = holder.getInventory().getItem(0);
                List<String> lore = new ArrayList<>();
                lore.add("현재 플레이 인원 : " + size(CPVP.getActiveArenas()));
                holder.getInventory().setItem(0, itemutils.setLore(cpvp_1, lore));
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);

    }

    public static Inventory getMenu() {
        return holder.getInventory();
    }

    public static int size(List<Arena> activeArenas) {
        int i = 0;
        for(Arena arena : activeArenas) {
            Session session = Session.getSession(arena);
            i = i + session.getTeam1().getMembers().size() + session.getTeam2().getMembers().size();
        }
        return i;
    }
}
