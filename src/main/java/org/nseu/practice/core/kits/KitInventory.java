package org.nseu.practice.core.kits;

import org.bukkit.inventory.Inventory;
import org.nseu.practice.util.InventoryGUIHolder;

public class KitInventory {


    private static Inventory cpvp_kit_1;
    private static Inventory cpvp_kit_2;
    private static Inventory cpvp_kit_3;

    public static Inventory getCpvpKitItems(int page) {
        if(page == 1) {
            InventoryGUIHolder holder = new InventoryGUIHolder("cpvp_kit_1", "cpvp_kit_1", 54, "CPVP kit page 1/3");
            holder.getInventory().setContents(cpvp_kit_1.getContents());
            return holder.getInventory();
        } else if(page == 2) {
            InventoryGUIHolder holder = new InventoryGUIHolder("cpvp_kit_2", "cpvp_kit_2", 54, "CPVP kit page 2/3");
            holder.getInventory().setContents(cpvp_kit_2.getContents());
            return holder.getInventory();
        } else {
            InventoryGUIHolder holder = new InventoryGUIHolder("cpvp_kit_3", "cpvp_kit_3", 54, "CPVP kit page 3/3");
            holder.getInventory().setContents(cpvp_kit_3.getContents());
            return holder.getInventory();
        }
    }

}
