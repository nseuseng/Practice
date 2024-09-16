package org.nseu.practice.core;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.gamemode.CPVP_CUSTOM;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.match.Session;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.core.player.Stats;
import org.nseu.practice.core.queue.Ranked;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.PartyUtils;
import org.nseu.practice.util.nameutil;

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

    public static void startMatch(GameMode gameMode, boolean isRanked, Party randomParty, Party party, int size) {
        if(size == 1) {
            switch (gameMode) {
                case CPVP -> {

                    Arena arena = CPVP.selectArenaAndLock();
                    if (arena == null) {
                        Message.sendMessage(randomParty, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                        Message.sendMessage(party, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                    }

                    Session.createSession(randomParty, party, arena, isRanked, gameMode, size);
                    //telelport

                    PartyUtils.teleportParty(randomParty, arena.pos1());
                    PartyUtils.teleportParty(party, arena.pos2());

                    PartyUtils.setPartyStatus(randomParty, PracticePlayer.Status.IS_IN_MATCH_COOLDOWN);
                    PartyUtils.setPartyStatus(party, PracticePlayer.Status.IS_IN_MATCH_COOLDOWN);
                    //send message
                    String msg1;
                    String msg2;
                    if(isRanked) {
                        msg1 =  "================================\n" +
                                " vs " + nameutil.name(party.getLeader()) + " " + Stats.getElo(party.getLeader(), gameMode) +  "\n" +
                                "================================\n";
                        msg2 =  "================================" +
                                " vs " + nameutil.name(randomParty.getLeader()) + " " + Stats.getElo(randomParty.getLeader(), gameMode) +  "\n" +
                                "================================";
                    } else {
                        msg1 =  "================================\n" +
                                " vs " + nameutil.name(party.getLeader()) +  "\n" +
                                "================================\n";
                        msg2 =  "================================" +
                                " vs " + nameutil.name(randomParty.getLeader()) +  "\n" +
                                "================================";
                    }
                    Message.sendMessage(randomParty, msg1);
                    Message.sendMessage(party, msg2);


                    //3 second invincible

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomParty, "1");
                            Message.sendMessage(party, "1");
                        }
                    }.runTaskLater(Main.getInstance(), 20L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomParty, "2");
                            Message.sendMessage(party, "2");
                        }
                    }.runTaskLater(Main.getInstance(), 40L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomParty, "3");
                            Message.sendMessage(party, "3");
                        }
                    }.runTaskLater(Main.getInstance(), 60L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomParty, "start!");
                            Message.sendMessage(party, "start!");
                            PartyUtils.setPartyStatus(randomParty, PracticePlayer.Status.IS_PLAYING);
                            PartyUtils.setPartyStatus(party, PracticePlayer.Status.IS_PLAYING);
                        }
                    }.runTaskLater(Main.getInstance(), 80L);
                    break;
                }
            }
        }
    }

    public static void endMatch(Session session, boolean result) {
        if(session.getSize() == 1) {
            switch (session.getGameMode()) {
                case CPVP -> {
                    Party winner;
                    Party loser;
                    if(result) {
                        winner = session.getParty1();
                        loser = session.getParty2();
                    } else {
                        winner = session.getParty2();
                        loser = session.getParty1();
                    }
                    String winnerMsg = "============================" +
                            "인벤토리 확인" +
                            "인벤토리 확인" +
                            "============================";
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PartyUtils.teleportParty(winner, Main.spawn);
                            PartyUtils.teleportParty(loser, Main.spawn);
                            PartyUtils.setPartyStatus(winner, PracticePlayer.Status.IS_IDLE);
                            PartyUtils.setPartyStatus(loser, PracticePlayer.Status.IS_IDLE);
                            Arena arena = session.getArena();
                            Session.destroySession(session);
                            CPVP.unlockArena(arena);
                        }
                    }.runTaskLater(Main.getInstance(), 100L);

                    break;
                }
                default -> {
                    break;
                }
            }
        }
    }
}
