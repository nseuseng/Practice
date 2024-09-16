package org.nseu.practice;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.nseu.practice.command.Command;
import org.nseu.practice.listener.EventListener;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        EventListener.registerThis(instance);
        Main.getInstance().getCommand("대전").setExecutor(new Command());
        Main.getInstance().getCommand("파티").setExecutor(new Command());
        Main.getInstance().getCommand("관전").setExecutor(new Command());
    }

    public static Location spawn = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
