package org.nseu.practice.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.awt.*;

public class InventoryGUIHolder implements InventoryHolder {

    private Inventory inv;
    private String inv_name;

    private String inv_type;

    public InventoryGUIHolder(String name, String type, int size, String title) {
        this.inv = Bukkit.createInventory(this, size, Component.text(title));
        this.inv_name = name;
        this.inv_type = type;
    }

    public void changeName(String name) {
        this.inv_name = name;
    }
    public void changeType(String type) {
        this.inv_type = type;
    }
    public void changeInventory(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public String getName() {
        return inv_name;
    }

    public String getType() {
        return inv_type;
    }
}
