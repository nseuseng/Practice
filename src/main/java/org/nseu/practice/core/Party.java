package org.nseu.practice.core;

import org.bukkit.entity.Player;
import org.nseu.practice.core.player.PracticePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Party {

    private UUID Leader;
    private ArrayList<UUID> Member = new ArrayList<>();


    public Party(UUID Leader) {
        this.Leader = Leader;
    }

    public boolean isAllNotPlaying() {
        if(PracticePlayer.getPlayer(Leader).getStatus() == PracticePlayer.Status.IS_PLAYING) {
            return false;
        }

        for(UUID uuid : Member) {
            if(PracticePlayer.getPlayer(uuid).getStatus() == PracticePlayer.Status.IS_PLAYING) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<UUID> getMembers() {
        return this.Member;
    }

    public UUID getLeader() {
        return this.Leader;
    }

    private static HashMap<UUID, Party> partyList = new HashMap<>();

    public static Party getParty(UUID uuid) {
        return partyList.get(uuid);
    }
}
