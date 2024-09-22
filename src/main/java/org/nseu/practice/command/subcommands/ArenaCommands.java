package org.nseu.practice.command.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.storage.FileManager;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.Util;

import java.util.Locale;

public class ArenaCommands {
    public static void onCommand(Player p, String[] args) {
        switch (args[0]) {
            case "생성" -> {
                if(Arena.getArenaByName(args[1]) == null) {
                    Arena arena = new Arena(args[1]);
                    Arena.addArena(args[1], arena);
                    Message.sendMessage(p, "&c&lSUCCESS &r&7새 아레나 [" + args[1] + "]을/를 생성했습니다");
                } else {
                    Message.sendMessage(p, "&c&lERROR &r&7해당 아레나 [" + args[1] + "]은/는 이미 존재합니다");
                }
                break;

            }
            case "설정" -> {
                Arena arena = Arena.getArenaByName(args[1]);

                Location loc = p.getLocation();
                switch (args[2].toLowerCase(Locale.ROOT)) {
                    case "team1pos" -> {
                        arena.setTeam1(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 팀 1 지점을 설정했습니다");
                    }
                    case "team2pos" -> {
                        arena.setTeam2(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 팀 2 지점을 설정했습니다");
                    }
                    case "pos1" -> {
                        arena.setPos1(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 구간 1 지점을 설정했습니다");
                    }
                    case "pos2" -> {
                        arena.setPos2(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 구간 2 지점을 설정했습니다");
                    }
                    case "world" -> {
                        arena.setWorldName(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 월드를 설정했습니다");
                    }
                    case "spec" -> {
                        arena.setSpec(loc);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + args[1] + " 의 관전 지점을 설정했습니다");
                    }
                }
            }
            case "삭제" -> {

            }
            case "등록" -> {

                String gameMode = args[1];
                String arenaName = args[2];

                if(Arena.getArenaByName(arenaName)==null) {
                    Message.sendMessage(p.getUniqueId(), "&c&lERROR &r&7아레나 " + arenaName + "은는 존재하지 않습니다");
                }
                switch (gameMode.toLowerCase()) {
                    case "cpvp" -> {
                        CPVP.addArena(arenaName);
                        Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7아레나 " + arenaName + "을/를 CPVP 아레나로 등록했습니다");
                    }
                }
            }
            case "스폰설정" -> {
                Main.spawn = p.getLocation();
                FileManager.setData("spawn", Util.Loc.toString(Main.spawn));
                Message.sendMessage(p.getUniqueId(), "&c&lSUCCESS &r&7스폰을 설정했습니다");
            }
        }

    }
}
