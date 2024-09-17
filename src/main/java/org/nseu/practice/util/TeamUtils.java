package org.nseu.practice.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.nseu.practice.core.Team;
import org.nseu.practice.core.player.PracticePlayer;

public class TeamUtils {

    public static void setTeamStatus(Team team, PracticePlayer.Status status) {
        team.getMembers().forEach(uuid -> {
            PracticePlayer.getPlayer(uuid).setStatus(status);
        });
    }

    public static void teleportTeam(Team team, Location location) {
        team.getMembers().forEach(player -> {
            Bukkit.getPlayer(player).teleport(location);
        });
    }
}
