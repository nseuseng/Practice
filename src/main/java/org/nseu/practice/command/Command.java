package org.nseu.practice.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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
                if(args[0].equalsIgnoreCase("_") && args.length >= 2) {
                    String match_id = args[1];
                    MatchRecord.openRecord(match_id, p);
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
                if(args[0].equalsIgnoreCase("생성")) {

                }
                break;
            }
        }

        return false;
    }
}
