package org.nseu.practice.core.queue;

import org.bukkit.Bukkit;
import org.nseu.practice.core.Team;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.core.player.Stats;
import org.nseu.practice.util.TeamUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class PracticeQueue {

    private final boolean isRanked;
    private final GameMode gameMode;
    private ArrayList<Team> queue = new ArrayList<>();
    HashMap<Team, Integer> time_in_ticks = new HashMap<>();
    private int party_size;

    public PracticeQueue(boolean isRanked, GameMode gameMode, int party_size) {
        this.isRanked = isRanked;
        this.gameMode = gameMode;
        this.party_size = party_size;
    }

    public void RankedTick() {
        int currentTick = Bukkit.getCurrentTick();
        if(queue.size() <= 1) {
            return;
        }
        for(int i = 0; i < queue.size(); i++) {
            Team current = queue.get(i);
            int c_elo = Stats.getTeamElo(current, gameMode);
            int c_maxdiff = ((int)((int)(currentTick - time_in_ticks.get(current))/100)) * 50;
            if(i == queue.size() - 1) {
                break;
            }
            for(int p = i+1; p < queue.size(); p++) {
                Team target = queue.get(p);
                int t_elo = Stats.getTeamElo(target, gameMode);
                int t_maxdiff = ((int)((int)(currentTick - time_in_ticks.get(target))/100)) * 50;

                int diff = Math.abs(t_elo - c_elo);
                if(diff < c_maxdiff || diff < t_maxdiff) {
                    queue.remove(current);
                    queue.remove(target);
                    Perform.startMatch(gameMode, true, current, target, party_size);
                    RankedTick();
                }
            }
        }
    }

    public void add(Team team) {
        TeamUtils.setTeamStatus(team, PracticePlayer.Status.IS_QUEUING);
        if(isRanked) {
            time_in_ticks.put(team, Bukkit.getCurrentTick());
            queue.add(team);
        } else {
            queue.add(team);
            while(queue.size() >= 2) {
                Team random1 = queue.remove(0);
                Team random2 = queue.remove(0);
                Perform.startMatch(this.gameMode, false, random1, random2, party_size);
            }
        }
    }
}
