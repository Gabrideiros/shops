package me.gabrideiros.lojas.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {

    public static String serialize(Location l) {

        return l.getWorld().getName() + ','
                + l.getX()   + ','
                + l.getY()   + ','
                + l.getZ()   + ','
                + l.getYaw() + ','
                + l.getPitch();
    }

    public static Location deserialize(String s) {
        String[] location = s.split(",");
        return new Location(
                Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float .parseFloat (location[4]),
                Float .parseFloat (location[5]));
    }
}
