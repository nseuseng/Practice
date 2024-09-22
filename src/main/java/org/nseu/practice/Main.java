package org.nseu.practice;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.nseu.practice.command.Command;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.inventory.InventoryHandler;
import org.nseu.practice.core.menu.MatchMenu;
import org.nseu.practice.listener.EventListener;
import org.nseu.practice.storage.FileManager;
import org.nseu.practice.util.Util;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        FileManager.load();
        Main.spawn = Util.Loc.fromString(FileManager.getData("spawn"));
        EventListener.registerThis(instance);
        getCommand("대전").setExecutor(new Command());
        getCommand("파티").setExecutor(new Command());
        getCommand("관전").setExecutor(new Command());
        getCommand("아레나").setExecutor(new Command());
        getCommand("킷").setExecutor(new Command());
        MatchMenu.start();
        CPVP.setup();
        InventoryHandler.setup();

    }

    public static Location spawn = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    @Override
    public void onDisable() {
        FileManager.save();
        // Plugin shutdown logic
    }
}
