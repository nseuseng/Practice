package org.nseu.practice.core.match;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record MatchInventory(double health, double hunger, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots,
                             ItemStack itemInOffHand, ItemStack[] contents) {

    public MatchInventory(double health, double hunger, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack itemInOffHand, ItemStack @NotNull [] contents) {
        this.health = health;
        this.hunger = hunger;
        this.helmet = helmet.clone();
        this.chestplate = chestplate.clone();
        this.leggings = leggings.clone();
        this.boots = boots.clone();
        this.itemInOffHand = itemInOffHand.clone();
        this.contents = contents.clone();
    }
}
