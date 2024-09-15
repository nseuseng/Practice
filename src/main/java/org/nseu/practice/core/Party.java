package org.nseu.practice.core;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Party {

    private Player Leader;
    private ArrayList<Player> Member = new ArrayList<>();


    public Party(Player Leader) {
        this.Leader = Leader;
    }

    public static Party getParty(UUID leader) {

    }

    public ArrayList<Player> getMembers() {
        return this.Member;
    }

    public Player getLeader() {
        return this.Leader;
    }
}
