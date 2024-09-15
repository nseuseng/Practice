package org.nseu.practice.core;

import org.bukkit.entity.Player;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.gamemode.CPVP_CUSTOM;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.queue.Ranked;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.PartyUtils;

public class Perform {

    public static void queueRanked(Party party, GameMode gameMode) {
        switch (gameMode) {
            case CPVP -> {
                CPVP.getRankedQueue().add(party);
                break;
            }
            case CPVP_CUSTOM -> {
                //
                break;
            }
        }
    }

    public static void startMatch(GameMode gameMode, boolean isRanked, Party randomParty, Party party) {

        switch (gameMode) {
            case CPVP -> {
                Arena arena = CPVP.selectArenaAndLock();
                if(arena == null) {
                    Message.sendMessage(randomParty, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                    Message.sendMessage(party, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                }

                //telelport

                PartyUtils.teleportParty(randomParty, arena.pos1());


                //send message

                //5 second invincible
                break;
            }
        }
    }
}
