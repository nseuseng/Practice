package org.nseu.practice.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Util {
    public static class Loc {
        public static String toString(Location loc) {
            return loc.getWorld().getName() + "/" + loc.getX() + "/" + loc.getY() + "/" + loc.getZ() + "/" + loc.getYaw() + "/" + loc.getPitch();
        }

        public static Location fromString(String string) {
            String[] info = string.split("/");
            return new Location(Bukkit.getWorld(info[0]), Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]), Float.parseFloat(info[4]), Float.parseFloat(info[5]));
        }


    }
}
