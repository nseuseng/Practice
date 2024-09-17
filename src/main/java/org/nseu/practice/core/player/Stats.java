package org.nseu.practice.core.player;

import org.nseu.practice.core.Team;
import org.nseu.practice.core.gamemode.GameMode;

import java.util.UUID;

public class Stats {

    public static int getElo(UUID uuid, GameMode gameMode) {

        return 0;
    }

    public static int getTeamElo(Team current, GameMode gameMode) {
        int size = current.getMembers().size();
        int add_All_elo = 0;
        for(UUID member : current.getMembers()) {
            add_All_elo = add_All_elo + getElo(member, gameMode);
        }
        return add_All_elo / size;
    }
}
