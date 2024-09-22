package org.nseu.practice.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.command.subcommands.ArenaCommands;
import org.nseu.practice.command.subcommands.KitCommands;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.match.MatchInventory;
import org.nseu.practice.core.match.MatchRecord;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        Player p = (Player) commandSender;
        switch (label) {
            case "대전" -> {
                if(args.length < 1) {
                    //command to short
                    return false;
                }
                if(args[0].equalsIgnoreCase("_") && args.length >= 3) {
                    String match_id = args[1];
                    MatchRecord.openRecord(match_id, args[2], p);
                }
                if(args[0].equalsIgnoreCase("메뉴")) {
                    Perform.openMenu(p);
                }
            }
            case "관전" -> {
                //do something
            }
            case "파티" -> {

                break;
            }
            case "아레나" -> {
                //do something ig
                if(p.hasPermission("practice.admin")) {
                    ArenaCommands.onCommand(p, args);
                }
                break;
            }
            case "킷" -> {
                if(p.hasPermission("practice.admin")) {
                    KitCommands.onCommand(p, args);
                }
            }
        }

        return false;
    }
}
