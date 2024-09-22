package org.nseu.practice.core.kits;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nseu.practice.util.BukkitSerialization;
import org.nseu.practice.util.InventoryGUIHolder;

import java.io.IOException;

public class KitInventory {

    private ItemStack[] armors;
    private ItemStack offhand;
    private ItemStack[] inventory;

    public KitInventory(PlayerInventory playerInventory) {
        armors = playerInventory.getArmorContents();
        offhand = playerInventory.getItemInOffHand();
        inventory = playerInventory.getStorageContents();
    }

    public KitInventory(ItemStack[] clone, ItemStack clone1, ItemStack[] clone2) {
        this.armors = clone;
        this.offhand = clone1;
        this.inventory = clone2;
    }

    public KitInventory clone() {
        return new KitInventory(armors.clone(), offhand.clone(), inventory.clone());
    }

    public String toString() {
        String armors_string = BukkitSerialization.itemStackArrayToBase64(armors);
        ItemStack[] offhanded = new ItemStack[1];
        offhanded[0] = offhand;
        String offhand_string = BukkitSerialization.itemStackArrayToBase64(offhanded);
        String inventory_string = BukkitSerialization.itemStackArrayToBase64(inventory);
        return armors_string + "/" + offhand_string + "/" + inventory_string;
    }

    public KitInventory(String data) throws IOException {
        String[] datachunks = data.split("/");
        armors = BukkitSerialization.itemStackArrayFromBase64(datachunks[0]);
        offhand = BukkitSerialization.itemStackArrayFromBase64(datachunks[1])[0];
        inventory = BukkitSerialization.itemStackArrayFromBase64(datachunks[2]);
    }

    public ItemStack[] getArmors() {
        return this.armors;
    }

    public ItemStack getOffhand() {
        return this.offhand;
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }
}
