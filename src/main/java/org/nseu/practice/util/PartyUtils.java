package org.nseu.practice.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.player.PracticePlayer;

public class PartyUtils {

    public static void setPartyStatus(Party party, PracticePlayer.Status status) {
        Player Leader = party.getLeader();
        PracticePlayer.getPlayer(Leader.getUniqueId()).setStatus(status);
        party.getMembers().forEach(player -> {
            PracticePlayer.getPlayer(player.getUniqueId()).setStatus(status);
        });
    }

    public static void teleportParty(Party party, Location location) {
        party.getLeader().teleport(location);
        party.getMembers().forEach(player -> {
            player.teleport(location);
        });
    }
}
