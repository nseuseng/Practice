package org.nseu.practice.core;

import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.nameutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Team {

    private List<UUID> Member = new ArrayList<>();

    public Team(List<UUID> member) {
        this.Member = member;
    }

    public boolean isAllNotPlaying() {
        for(UUID uuid : Member) {
            if(PracticePlayer.getPlayer(uuid).getStatus() == PracticePlayer.Status.IS_PLAYING) {
                return false;
            }
        }

        return true;
    }

    public List<UUID> getMembers() {
        return this.Member;
    }

    public boolean contains(UUID uuid) {
        return Member.contains(uuid);
    }

    public void leave(UUID uuid) {

    }
}
