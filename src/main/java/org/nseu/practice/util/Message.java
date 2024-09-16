package org.nseu.practice.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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

    public static void sendMessage(UUID uuid, String Message) {
        if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Bukkit.getPlayer(uuid).sendMessage(Component.text(Message));
        }
    }

    public Component clickableMessage(String message, String command) {
        Component component = Component.text(ChatColor.translateAlternateColorCodes('&', message));
        component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        return component;
    }
}
