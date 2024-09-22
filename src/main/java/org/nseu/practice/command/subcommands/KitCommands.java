package org.nseu.practice.command.subcommands;

import org.bukkit.entity.Player;
import org.nseu.practice.core.kits.DefaultKits;
import org.nseu.practice.util.Message;

public class KitCommands {
    public static void onCommand(Player p, String[] args) {
        switch (args[0]) {
            case "설정" -> {
                DefaultKits.setDefaultkit(args[1], p.getInventory());
                Message.sendMessage(p, "&c&lSUCCESS &r&7현재 인벤토리를 킷 " + args[1] + " 으로 설정했습니다");
            }
            case "로드" -> {
                DefaultKits.loadDefaultkit(p, args[1]);
            }
        }
    }
}
