package org.nseu.practice.core.queue;

import org.nseu.practice.core.gamemode.GameMode;

import java.util.HashMap;

public class Ranked {

    public static HashMap<GameMode, PracticeQueue> queues = new HashMap<>();

    public static PracticeQueue getQueue(GameMode gameMode) {
        return queues.get(gameMode);
    }
}
