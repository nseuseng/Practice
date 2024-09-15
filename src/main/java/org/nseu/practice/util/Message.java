package org.nseu.practice.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;

public class Message {

    public static void sendMessage(Party party, String Message) {
        sendMessage(party.getLeader(), Message);
        party.getMembers().forEach(p -> {
            sendMessage(p, Message);
        });
    }

    @Deprecated
    public static void sendMessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
    }
}
