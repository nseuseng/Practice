package org.nseu.practice.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.Team;

import java.util.UUID;

public class Message {

    public static void sendMessage(Team team, String Message) {
        team.getMembers().forEach(p -> {
            sendMessage(p, Message);
        });
    }

    public static void sendMessage(Party party, String Message) {
        party.getAll().forEach(p -> {
            sendMessage(p, Message);
        });
    }

    public static void sendMessage(UUID uuid, String Message) {
        if(Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Bukkit.getPlayer(uuid).sendMessage(Component.text(c(Message)));
        }
    }

    public static void sendMessage(Player p, String Message) {
        p.sendMessage(Component.text(c(Message)));
    }

    public static Component clickableMessage(String message, String command) {
        Component component = Component.text(c(message));
        component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        return component;
    }

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(Team team, Component component) {
        team.getMembers().forEach(p -> {
            if(Bukkit.getOfflinePlayer(p).isOnline()) {
                Bukkit.getPlayer(p).sendMessage(component);
            }
        });
    }
}
