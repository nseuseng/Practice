package org.nseu.practice.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.nseu.practice.Main;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.util.InventoryGUIHolder;

public class EventListener implements Listener {

    public static void registerThis(Main instance) {
        Bukkit.getPluginManager().registerEvents(new EventListener(), instance);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(p.getUniqueId());
        PracticePlayer.Status status = practicePlayer.getStatus();
        if(status == PracticePlayer.Status.IS_IDLE || status == PracticePlayer.Status.IS_QUEUING || status == PracticePlayer.Status.IS_SPECTATING) {
            e.setCancelled(true);
            return;
        }

        Inventory inv = e.getClickedInventory();
        InventoryGUIHolder holder = (InventoryGUIHolder) inv.getHolder();
        if(inv == null) {
            return;
        }
        String Type = holder.getType();
        switch (Type) {
            case "kit_configure_inv" -> {
                int slot1 = e.getSlot();

                if(slot1 == 52) {
                    p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                }
            }
            default -> {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
           return;
        }
        Player p = (Player) e.getPlayer();
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(p.getUniqueId());
        PracticePlayer.Status status = practicePlayer.getStatus();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PracticePlayer.register(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PracticePlayer.destroy(e.getPlayer());
    }

}
