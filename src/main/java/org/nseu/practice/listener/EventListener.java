package org.nseu.practice.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.match.Session;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.PartyUtils;
import org.nseu.practice.util.nameutil;

import java.util.UUID;

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
        PracticePlayer.register(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(uuid);
        Party party = Session.getSession(uuid).getParty(uuid);
        Session session = Session.getSession(party);
        if(session != null) {
            session.recordInventory(uuid, e.getPlayer().getHealth(), e.getPlayer().getSaturation(), e.getPlayer().getInventory());
            String leaveMessage = nameutil.name(uuid) + " 이가 서버에서 나갔습니다";
            Message.sendMessage(session.getParty1(), leaveMessage);
            Message.sendMessage(session.getParty2(), leaveMessage);
            boolean alldown = Party.getParty(uuid).isAllNotPlaying();
            boolean result = !session.getParty1().contains(uuid);
            if(alldown) {
                Perform.endMatch(session, result);
            }
            if(party.getLeader().equals(uuid)) {
                party.disband();
            } else {
                party.leave(uuid);
            }
            PracticePlayer.destroy(uuid);
        } else {
            if(party.getLeader().equals(uuid)) {
                party.disband();
            } else {
                party.leave(uuid);
            }
            PracticePlayer.destroy(uuid);
        }
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            PracticePlayer dp = PracticePlayer.getPlayer(damager.getUniqueId());
            PracticePlayer vp = PracticePlayer.getPlayer(victim.getUniqueId());
            if(!(dp.getStatus() == PracticePlayer.Status.IS_PLAYING && vp.getStatus() == PracticePlayer.Status.IS_PLAYING)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(PracticePlayer.getPlayer(e.getPlayer().getUniqueId()).getStatus() == PracticePlayer.Status.IS_IN_MATCH_COOLDOWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent e) {
        Arena arena = Arena.getArena(e.getBlock().getLocation());
        if(arena != null && arena.isUsed()) {

            Session.getSession(arena).record(e.getBlock().getLocation(), e.getBlock().getBlockData());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();

        if(PracticePlayer.getPlayer(p.getUniqueId()).getStatus() == PracticePlayer.Status.IS_PLAYING) {
            Session session = Session.getSession(Party.getParty(p.getUniqueId()));
            session.recordInventory(p.getUniqueId(), e.getPlayer().getHealth(), e.getPlayer().getSaturation(), p.getInventory());
            PracticePlayer.getPlayer(p.getUniqueId()).setStatus(PracticePlayer.Status.IS_SPECTATING);


            Player killer = null;
            if (e.getDamageSource().getDirectEntity() instanceof Player) {
                killer = (Player) e.getDamageSource().getDirectEntity();
            }

            e.deathMessage(Component.text(""));
            String deathmsg;
            if(killer != null) {
                deathmsg = nameutil.name(p.getUniqueId()) + "이가 " + killer.getName() + " 에 의해 죽었습니다";
            } else {
                deathmsg = nameutil.name(p.getUniqueId()) + "이가 죽었습니다";
            }
            Message.sendMessage(session.getParty1(), deathmsg);
            Message.sendMessage(session.getParty2(), deathmsg);

            boolean alldown = Party.getParty(p.getUniqueId()).isAllNotPlaying();
            boolean result = !session.getParty1().contains(p.getUniqueId());
            if(alldown) {
                Perform.endMatch(session, result);
            }

        }
    }

}
