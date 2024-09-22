package org.nseu.practice.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.nseu.practice.Main;
import org.nseu.practice.arena.Arena;
import org.nseu.practice.core.Party;
import org.nseu.practice.core.Team;
import org.nseu.practice.core.Perform;
import org.nseu.practice.core.gamemode.CPVP;
import org.nseu.practice.core.inventory.InventoryHandler;
import org.nseu.practice.core.match.Session;
import org.nseu.practice.core.player.PracticePlayer;
import org.nseu.practice.util.InventoryGUIHolder;
import org.nseu.practice.util.Message;
import org.nseu.practice.util.nameutil;

import java.util.ArrayList;
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
        }

        Inventory inv = e.getClickedInventory();
        if(inv != null && inv.getHolder() instanceof InventoryGUIHolder holder) {
            String Type = holder.getType();
            switch (Type) {
                case "kit_configure_inv" -> {
                    int slot1 = e.getSlot();

                    if (slot1 == 52) {
                        p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                    }
                }
                case "duels_menu_unranked" -> {
                    int slot1 = e.getSlot();
                    if (slot1 == 0) {
                        if (PracticePlayer.getPlayer(p.getUniqueId()).getStatus() == PracticePlayer.Status.IS_IDLE) {
                            ArrayList<UUID> list = new ArrayList<>();
                            list.add(p.getUniqueId());
                            CPVP.getUnRankedQueue().add(new Team(list));
                            Message.sendMessage(p.getUniqueId(), "CPVP 대전에 대기합니다");
                            p.getInventory().setContents(InventoryHandler.getQueueInventory().getContents());
                        } else {
                            Message.sendMessage(p.getUniqueId(), "이미 다른 대전에 대기중입니다");
                        }
                        p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                    }
                }
                default -> {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player p  = e.getPlayer();
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(p.getUniqueId());

        switch (practicePlayer.getStatus()) {
            case IS_IDLE -> {
                e.setCancelled(true);
                if(!e.getAction().isRightClick()) {
                    return;
                }
                Material Type = e.getMaterial();
                if(Type.equals(Material.IRON_SWORD)) {
                    Perform.openMenu(p);
                } else if (Type.equals(Material.DIAMOND_SWORD)) {
                    // comming soon
                }
            }
            case IS_QUEUING -> {
                e.setCancelled(true);
                if(!e.getAction().isRightClick()) {
                    return;
                }
                Material Type = e.getMaterial();
                if(Type.equals(Material.RED_DYE)) {
                    Perform.cancelQueue(p);
                }
            }
            case IS_SPECTATING -> {
                e.setCancelled(true);
                if(!e.getAction().isRightClick()) {
                    return;
                }
                Material Type = e.getMaterial();
                if(Type.equals(Material.RED_DYE)) {
                    Perform.stopSpectating(p);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        Inventory inv = e.getInventory();
        InventoryGUIHolder holder = (InventoryGUIHolder) inv.getHolder();
        if(inv == null) {
            return;
        }
        String Type = holder.getType();
        if(Type.equalsIgnoreCase("match_inventory")) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(checkPlayerStatus((Player) e.getWhoClicked()));
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {
        Inventory inv1 = e.getDestination();
        Inventory inv2 = e.getSource();
        boolean inv1_b = checkInventory(inv1);
        boolean inv2_b = checkInventory(inv2);
        if(inv1_b || inv2_b) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(checkPlayerStatus((Player) e.getInitiator().getHolder()));
    }

    private static boolean checkPlayerStatus(Player p) {
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(p.getUniqueId());
        switch (practicePlayer.getStatus()) {
            case IS_IDLE, IS_EDITING_KIT, IS_SPECTATING, IS_QUEUING -> {
                return true;
            }
            case IS_IN_FFA, IS_PLAYING, IS_IN_MATCH_COOLDOWN -> {
                return false;
            }
        }
        return false;
    }

    private static boolean checkInventory(Inventory inv) {
        InventoryGUIHolder holder = (InventoryGUIHolder) inv.getHolder();
        if(inv == null) {
            return false;
        }
        String Type = holder.getType();
        switch (Type) {
            case "match_inventory", "duels_menu_unranked" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(e.getPlayer().getUniqueId());
        switch (practicePlayer.getStatus()) {
            case IS_IDLE, IS_SPECTATING, IS_IN_MATCH_COOLDOWN, IS_QUEUING -> {
                e.setCancelled(true);
            }
            case IS_EDITING_KIT -> {
                e.getItemDrop().remove();
            }
            case IS_IN_FFA, IS_PLAYING -> {
                //
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
           return;
        }
        Player p = (Player) e.getPlayer();
        //PracticePlayer practicePlayer = PracticePlayer.getPlayer(p.getUniqueId());
        //PracticePlayer.Status status = practicePlayer.getStatus();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PracticePlayer.register(e.getPlayer().getUniqueId());
        e.getPlayer().setGameMode(GameMode.SURVIVAL);
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setContents(InventoryHandler.getMenuInventory().getContents());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        PracticePlayer practicePlayer = PracticePlayer.getPlayer(uuid);
        Session session = Session.currentlyPlaying(uuid);
        if(practicePlayer.getStatus() == PracticePlayer.Status.IS_QUEUING) {
            Perform.cancelQueue(e.getPlayer());
        }
        if(Party.isInParty(uuid)) {
            Party party = Party.getParty(uuid);
            if(party.getLeader().equals(uuid)) {
                party.disband();
            } else {
                party.leave(uuid);
            }
        }
        if(session != null) {
            session.recordInventory(uuid, e.getPlayer().getHealth(), e.getPlayer().getSaturation(), e.getPlayer().getInventory());
            String leaveMessage = nameutil.name(uuid) + " 이가 서버에서 나갔습니다";
            Message.sendMessage(session.getTeam1(), leaveMessage);
            Message.sendMessage(session.getTeam2(), leaveMessage);
            practicePlayer.setStatus(PracticePlayer.Status.IS_SPECTATING);
            boolean alldown = session.getTeam(uuid).isAllNotPlaying();
            boolean result = !session.getTeam1().contains(uuid);
            if(alldown) {
                Perform.endMatch(session, result);
            }
        }
        PracticePlayer.destroy(uuid);

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

            Session.currentlyPlaying(arena).record(e.getBlock().getLocation(), e.getBlock().getBlockData());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();

        Location loc = p.getLocation();
        if(PracticePlayer.getPlayer(p.getUniqueId()).getStatus() == PracticePlayer.Status.IS_PLAYING) {
            Session session = Session.currentlyPlaying(p.getUniqueId());
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
            Message.sendMessage(session.getTeam1(), deathmsg);
            Message.sendMessage(session.getTeam2(), deathmsg);

            Perform.spectate(p, session);
            System.out.println("Ended");
            boolean alldown = session.getTeam(p.getUniqueId()).isAllNotPlaying();
            for(UUID uid : session.getTeam(p.getUniqueId()).getMembers()) {

                System.out.println(nameutil.name(uid) + " / " + PracticePlayer.getPlayer(uid).getStatus().name());
            }
            boolean result = !session.getTeam1().contains(p.getUniqueId());
            System.out.println("isALLDOWN " + alldown);

            if(alldown) {
                Perform.endMatch(session, result);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.spigot().respawn();
                    p.teleport(loc);

                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

}
