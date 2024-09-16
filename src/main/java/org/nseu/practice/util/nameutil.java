package org.nseu.practice.util;

import org.bukkit.Bukkit;

import java.util.UUID;

public class nameutil {

    public static String name(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
