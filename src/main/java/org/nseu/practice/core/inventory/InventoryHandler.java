package org.nseu.practice.core.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.itemutils;

public class InventoryHandler {

    public static final String mainInv = "main_menu_inv";
    public static final String queue_Inv = "main_menu_inv";

    private static InventoryGUIHolder menuInv;
    private static InventoryGUIHolder queueInv;

    public static void setup() {
        menuInv = new InventoryGUIHolder("", mainInv, 36, "");
        ItemStack unranked = itemutils.createItem("&3&l일반 대전", Material.IRON_SWORD);
        ItemStack ranked = itemutils.createItem("&3&l랭크 대전", Material.DIAMOND_SWORD);
        ItemStack help = itemutils.createItem("&c&l도움말", Material.BOOK);
        menuInv.getInventory().setItem(0, unranked);
        menuInv.getInventory().setItem(1, ranked);
        menuInv.getInventory().setItem(8, help);

        queueInv = new InventoryGUIHolder("", queue_Inv, 36, "");
        ItemStack queuecancel = itemutils.createItem("&c&l대전 대기 취소", Material.RED_DYE);
        queueInv.getInventory().setItem(8, queuecancel);
    }

    public static Inventory getMenuInventory() {
        return menuInv.getInventory();
    }

    public static Inventory getQueueInventory() {
        return queueInv.getInventory();
    }
}
