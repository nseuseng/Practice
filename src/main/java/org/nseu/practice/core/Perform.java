package org.nseu.practice.core;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.gamemode.GameMode;
import org.nseu.practice.core.inventory.InventoryHandler;
import org.nseu.practice.core.kits.DefaultKits;
import org.nseu.practice.core.match.Session;
import org.nseu.practice.core.menu.MatchMenu;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.core.player.Stats;
import org.nseu.practice.core.queue.PracticeQueue;
import org.nseu.practice.util.*;

import java.util.Objects;
import java.util.UUID;

public class Perform {

    public static void queueRanked(Team team, GameMode gameMode) {
        switch (gameMode) {
            case CPVP -> {
                CPVP.getRankedQueue().add(team);
                break;
            }
            case CPVP_CUSTOM -> {
                //
                break;
            }
        }
    }

    public static void onGameLeave() {

    }

    public static void startMatch(GameMode gameMode, boolean isRanked, Team randomTeam, Team team, int size) {
        if(size == 1) {
            switch (gameMode) {
                case CPVP -> {

                    Arena arena = CPVP.selectArenaAndLock();
                    if (arena == null) {
                        Message.sendMessage(randomTeam, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                        Message.sendMessage(team, "&c&lERROR &f&7아레나 맵이 부족하여 대전이 취소되었습니다");
                        return;
                    }


                    Session.createSession(randomTeam, team, arena, isRanked, gameMode, size);
                    //telelport

                    TeamUtils.teleportTeam(randomTeam, arena.getTeam1loc());
                    TeamUtils.teleportTeam(team, arena.getTeam2loc());

                    randomTeam.getMembers().forEach(member -> {
                        DefaultKits.loadDefaultkit(Objects.requireNonNull(Bukkit.getPlayer(member)), gameMode.name());
                    });
                    team.getMembers().forEach(member -> {
                        DefaultKits.loadDefaultkit(Objects.requireNonNull(Bukkit.getPlayer(member)), gameMode.name());
                    });

                    TeamUtils.setTeamStatus(randomTeam, PracticePlayer.Status.IS_IN_MATCH_COOLDOWN);
                    TeamUtils.setTeamStatus(team, PracticePlayer.Status.IS_IN_MATCH_COOLDOWN);
                    //send message
                    String msg1;
                    String msg2;
                    if(isRanked) {
                        msg1 =  "================================\n" +
                                " vs " + nameutil.name(team.getMembers().getFirst()) + " " + Stats.getElo(team.getMembers().getFirst(), gameMode) +  "\n" +
                                "================================\n";
                        msg2 =  "================================" +
                                " vs " + nameutil.name(randomTeam.getMembers().getFirst()) + " " + Stats.getElo(randomTeam.getMembers().getFirst(), gameMode) +  "\n" +
                                "================================";
                    } else {
                        msg1 =  "================================\n" +
                                " vs " + nameutil.name(team.getMembers().getFirst()) +  "\n" +
                                "================================\n";
                        msg2 =  "================================" +
                                " vs " + nameutil.name(randomTeam.getMembers().getFirst()) +  "\n" +
                                "================================";
                    }
                    Message.sendMessage(randomTeam, msg1);
                    Message.sendMessage(team, msg2);


                    randomTeam.getMembers().forEach(member -> {
                        Player p = Bukkit.getPlayer(member);
                        System.out.println(p.getName() + " " + gameMode.name());
                        DefaultKits.loadDefaultkit(p, gameMode.name());
                    });
                    team.getMembers().forEach(member -> {
                        Player p = Bukkit.getPlayer(member);
                        System.out.println(p.getName() + " " + gameMode.name());
                        DefaultKits.loadDefaultkit(p, gameMode.name());
                    });
                    //3 second invincible

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomTeam, "1");
                            Message.sendMessage(team, "1");
                        }
                    }.runTaskLater(Main.getInstance(), 20L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomTeam, "2");
                            Message.sendMessage(team, "2");
                        }
                    }.runTaskLater(Main.getInstance(), 40L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomTeam, "3");
                            Message.sendMessage(team, "3");
                        }
                    }.runTaskLater(Main.getInstance(), 60L);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Message.sendMessage(randomTeam, "start!");
                            Message.sendMessage(team, "start!");
                            TeamUtils.setTeamStatus(randomTeam, PracticePlayer.Status.IS_PLAYING);
                            TeamUtils.setTeamStatus(team, PracticePlayer.Status.IS_PLAYING);
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
                    Team winner;
                    Team loser;
                    if(result) {
                        winner = session.getTeam1();
                        loser = session.getTeam2();
                    } else {
                        winner = session.getTeam2();
                        loser = session.getTeam1();
                    }
                    UUID sessionid = session.getSessionID();
                    Component winner_inv = Message.clickableMessage(" 승리! - " + nameutil.name(winner.getMembers().getFirst()) + " : [인벤토리 확인]\n", "/대전 _ " + sessionid.toString() + winner.getMembers().getFirst().toString());
                    Component loser_inv = Message.clickableMessage(" 패배! - " + nameutil.name(loser.getMembers().getFirst()) + " : [인벤토리 확인]\n", "/대전 _ " + sessionid.toString() + loser.getMembers().getFirst().toString());
                    Component component = Component.text(Message.c("===========================\n"));
                    component = component.append(winner_inv);
                    component = component.append(loser_inv);
                    component = component.append(Component.text(Message.c("===========================")));
                    Message.sendMessage(winner, component);
                    Message.sendMessage(loser, component);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            TeamUtils.teleportTeam(winner, Main.spawn);
                            TeamUtils.teleportTeam(loser, Main.spawn);
                            session.unHideAll();
                            TeamUtils.setTeamStatus(winner, PracticePlayer.Status.IS_IDLE);
                            TeamUtils.setTeamStatus(loser, PracticePlayer.Status.IS_IDLE);
                            Arena arena = session.getArena();
                            session.revertWorld();
                            Session.destroySession(session);
                            CPVP.unlockArena(arena.getArenaName());
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

    public static void openMenu(Player p) {
        p.openInventory(MatchMenu.getMenu());
    }

    public static void cancelQueue(Player p) {
        UUID uuid = p.getUniqueId();
        PracticeQueue practiceQueue = PracticeQueue.getQueue(uuid);
        PracticeQueue.cancelQueue(uuid);
        Message.sendMessage(uuid, practiceQueue.getGameMode().name() + " 대기줄에서 나갔습니다");
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(uuid);
        if(practicePlayer != null) {
            practicePlayer.setStatus(PracticePlayer.Status.IS_IDLE);
            p.getInventory().setContents(InventoryHandler.getMenuInventory().getContents());
        }
    }

    public static void startSpectatng(Player p, boolean changeStatus) {

        p.setGameMode(org.bukkit.GameMode.CREATIVE);

    }

    public static void stopSpectating(Player p) {
        PracticePlayer.getPlayer(p.getUniqueId()).setStatus(PracticePlayer.Status.IS_IDLE);
        p.teleport(Main.spawn);
        Bukkit.getOnlinePlayers().forEach(user -> user.showPlayer(Main.getInstance(), p));
        p.setGameMode(org.bukkit.GameMode.SURVIVAL);
    }

    public static void spectate(Player p, Session session) {
        session.addSpectator(p.getUniqueId());
        p.teleport(session.getArena().spectate());
        p.setGameMode(org.bukkit.GameMode.CREATIVE);
        p.getInventory().setContents(InventoryHandler.getSpecInventory().getContents());
        PracticePlayer.getPlayer(p.getUniqueId()).unhideAll();
        session.getAllPlayers().forEach(uuid -> {
            if(PracticePlayer.getPlayer(uuid).getStatus() == PracticePlayer.Status.IS_PLAYING) {
                PracticePlayer.getPlayer(uuid).hidePlayer(p);
            }
        });
    }
}
