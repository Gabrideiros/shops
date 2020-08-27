package me.gabrideiros.lojas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.Map;

@Data
@AllArgsConstructor
public class Shop {

    private String name;
    private Location location;
    private int visits;
    private long time;
    private Map<String, Integer> note;

}
