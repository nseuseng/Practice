package org.nseu.practice.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class itemutils {


    public static ItemStack createItem(String Name, Material material) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(Name));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        List<Component> list = new ArrayList<>();
        lore.forEach(s -> {
            list.add(Component.text(s));
        });
        meta.lore(list);
        item.setItemMeta(meta);
        return item;
    }

}
