package org.nseu.practice.core.player;

import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.gamemode.GameMode;

import java.util.UUID;

public class Stats {

    public static int getElo(UUID uuid, GameMode gameMode) {

        return 0;
    }

    public static int getPartyElo(Party current, GameMode gameMode) {
        if(current.getMembers().isEmpty()) {
            return getElo(current.getLeader().getUniqueId(), gameMode);
        } else {
            int size = current.getMembers().size();
            int add_All_elo = getElo(current.getLeader().getUniqueId(), gameMode);
            for(Player member : current.getMembers()) {
                add_All_elo = add_All_elo + getElo(member.getUniqueId(), gameMode);
            }
            return add_All_elo / size;
        }
    }
}
