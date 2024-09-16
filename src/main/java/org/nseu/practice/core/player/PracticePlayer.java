package org.nseu.practice.core.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
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

    private static ConcurrentHashMap<UUID, PracticePlayer> PlayerMap = new ConcurrentHashMap<>();
    private static final Object Lock = new Object();

    public static void register(UUID uuid) {
        synchronized (Lock) {
            PlayerMap.put(uuid, new PracticePlayer(uuid));
        }
    }

    public static void destroy(UUID uuid) {
        synchronized (Lock) {
            PlayerMap.remove(uuid);
        }
    }

    public enum Status {
        IS_PLAYING, IS_QUEUING, IS_EDITING_KIT, IS_SPECTATING, IS_IN_FFA, IS_IDLE, LEFT_SERVER, IS_IN_MATCH_COOLDOWN;
    }
}
