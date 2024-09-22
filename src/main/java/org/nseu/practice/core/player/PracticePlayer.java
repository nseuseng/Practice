package org.nseu.practice.core.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nseu.practice.Main;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PracticePlayer {

    private final UUID uuid;
    private Status status = Status.IS_IDLE;

    public PracticePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public static PracticePlayer getPlayer(UUID uniqueId) {
        return PlayerMap.get(uniqueId);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    private ArrayList<UUID> hiddenFrom = new ArrayList<>();
    private ArrayList<UUID> hidden = new ArrayList<>();

    public void hidePlayer(Player p) {
        this.hidden.add(p.getUniqueId());
        PracticePlayer.getPlayer(p.getUniqueId()).addHiddenFrom(this.uuid);
        Bukkit.getPlayer(this.uuid).hidePlayer(Main.getInstance(), p);
    }

    public void unhidePlayer(Player p) {
        this.hidden.remove(p.getUniqueId());
        PracticePlayer.getPlayer(p.getUniqueId()).removeHiddenFrom(this.uuid);
        Bukkit.getPlayer(this.uuid).showPlayer(Main.getInstance(), p);
    }

    public void unhideAll() {
        for(UUID uuid : hidden) {
            unhidePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
        }
    }

    public void unhideFromAll() {
        List<UUID> temp = new ArrayList<>();
        for(UUID uuid : hiddenFrom) {
            temp.add(uuid);
        }
        for(UUID uuid : temp) {
            PracticePlayer.getPlayer(uuid).unhidePlayer(Bukkit.getPlayer(this.uuid));
        }
    }

    private void removeHiddenFrom(UUID uuid) {
        hiddenFrom.remove(uuid);
    }

    private void addHiddenFrom(UUID uuid) {
        hiddenFrom.add(uuid);
    }

    public void onLeave() {

    }

    private static ConcurrentHashMap<UUID, PracticePlayer> PlayerMap = new ConcurrentHashMap<>();
    private static final Object Lock = new Object();

    public static void register(UUID uuid) {
        synchronized (Lock) {
            PlayerMap.put(uuid, new PracticePlayer(uuid));
        }
    }

    public static void destroy(UUID uuid) {
        synchronized (Lock) {
            PlayerMap.get(uuid).onLeave();
            PlayerMap.remove(uuid);
        }
    }

    public enum Status {
        IS_PLAYING, IS_QUEUING, IS_EDITING_KIT, IS_SPECTATING, IS_IN_FFA, IS_IDLE, LEFT_SERVER, IS_IN_MATCH_COOLDOWN;
    }
}
