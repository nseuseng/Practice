package org.nseu.practice.core.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PracticePlayer {

    private final UUID uuid;
    private final Player player;
    private Status status = Status.IS_IDLE;

    public PracticePlayer(UUID uuid, Player player) {
        this.uuid = uuid;
        this.player = player;
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

    private static ConcurrentHashMap<UUID, PracticePlayer> PlayerMap = new ConcurrentHashMap<>();
    private static final Object Lock = new Object();

    public static void register(Player player) {
        synchronized (Lock) {
            UUID uuid = player.getUniqueId();
            PlayerMap.put(uuid, new PracticePlayer(uuid, player));
        }
    }

    public static void destroy(Player player) {
        synchronized (Lock) {
            UUID uuid = player.getUniqueId();
            PlayerMap.remove(uuid);
        }
    }

    public enum Status {
        IS_PLAYING, IS_QUEUING, IS_EDITING_KIT, IS_SPECTATING, IS_IN_FFA, IS_IDLE, LEFT_SERVER;
    }
}
