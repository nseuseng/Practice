package org.nseu.practice.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;

import java.util.UUID;

public class Message {

    public static void sendMessage(Party party, String Message) {
        sendMessage(party.getLeader(), Message);
        party.getMembers().forEach(p -> {
            sendMessage(p, Message);
        });
    }

    @Deprecated
    public static void sendMessage(UUID uuid, String Message) {
        if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
        }
    }
}
