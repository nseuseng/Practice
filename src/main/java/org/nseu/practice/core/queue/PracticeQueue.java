package org.nseu.practice.core.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.player.Stats;

import java.util.ArrayList;
import java.util.HashMap;

public class PracticeQueue {

    private final boolean isRanked;
    private final GameMode gameMode;
    private ArrayList<Party> queue = new ArrayList<>();
    HashMap<Party, Integer> time_in_ticks = new HashMap<>();
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
            Party current = queue.get(i);
            int c_elo = Stats.getPartyElo(current, gameMode);
            int c_maxdiff = ((int)((int)(currentTick - time_in_ticks.get(current))/100)) * 50;
            if(i == queue.size() - 1) {
                break;
            }
            for(int p = i+1; p < queue.size(); p++) {
                Party target = queue.get(p);
                int t_elo = Stats.getPartyElo(target, gameMode);
                int t_maxdiff = ((int)((int)(currentTick - time_in_ticks.get(target))/100)) * 50;

                int diff = Math.abs(t_elo - c_elo);
                if(diff < c_maxdiff || diff < t_maxdiff) {
                    queue.remove(current);
                    queue.remove(target);
                    Perform.startMatch(gameMode, true, current, target);
                    RankedTick();
                }
            }
        }
    }

    public void add(Party party) {
        if(isRanked) {
            time_in_ticks.put(party, Bukkit.getCurrentTick());
            queue.add(party);
        } else {
            queue.add(party);
            while(queue.size() >= 2) {
                Party random1 = queue.remove(0);
                Party random2 = queue.remove(0);
                Perform.startMatch(this.gameMode, false, random1, random2);
            }
        }
    }
}
