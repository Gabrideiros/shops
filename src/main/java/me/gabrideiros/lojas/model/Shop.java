package me.gabrideiros.lojas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class Shop {

    private String name;
    private Location location;
    private int visits;
    private long time;
    private Map<String, Integer> note;

    public Shop(String name, Location location) {
        this.name = name;
        this.location = location;
        this.visits = 0;
        this.time = 0;
        this.note = new HashMap<>();
    }

}
