package org.nseu.practice.core;

import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.nameutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Party {

    private UUID Leader;
    private ArrayList<UUID> Member = new ArrayList<>();

    public static boolean isInParty(UUID uuid) {
        return partyList.containsKey(uuid);
    }


    public ArrayList<UUID> getAll() {
        ArrayList<UUID> list = new ArrayList<>(Member);
        list.add(Leader);
        return list;
    }

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

    public void disband() {
        this.getAll().forEach(uuid -> {
            partyList.remove(uuid);
        });
        Message.sendMessage(this, "파티가 해산되었습니다");
    }

    public boolean contains(UUID uuid) {
        if(Leader == uuid) {
            return true;
        } else {
            return Member.contains(uuid);
        }
    }

    public void leave(UUID uuid) {
        this.Member.remove(uuid);
        Message.sendMessage(uuid, "당신은 파티에서 나갔습니다");
        Message.sendMessage(this, nameutil.name(uuid) + " 이가 파티에서 나갔습니다");
    }

    public void invite(UUID uuid) {

    }



    public void kick(UUID uuid) {
        this.Member.remove(uuid);
        Message.sendMessage(uuid, "당신은 파티에서 추방됬습니다");
        Message.sendMessage(this, nameutil.name(uuid) + " 이가 파티에서 추방됬습니다");
    }
}
