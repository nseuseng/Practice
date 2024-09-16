package org.nseu.practice.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.player.PracticePlayer;

public class PartyUtils {

    public static void setPartyStatus(Party party, PracticePlayer.Status status) {
        PracticePlayer.getPlayer(party.getLeader()).setStatus(status);
        party.getMembers().forEach(uuid -> {
            PracticePlayer.getPlayer(uuid).setStatus(status);
        });
    }

    public static void teleportParty(Party party, Location location) {
        Bukkit.getPlayer(party.getLeader()).teleport(location);
        party.getMembers().forEach(player -> {
            Bukkit.getPlayer(player).teleport(location);
        });
    }
}
