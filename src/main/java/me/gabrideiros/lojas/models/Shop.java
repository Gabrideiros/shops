package me.gabrideiros.lojas.models;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.gabrideiros.lojas.utils.TimeFormatter;
import org.bukkit.Location;

import java.util.*;

@Data
@AllArgsConstructor
public class Shop {

    private UUID uuid;
    private String name;
    private Location location;
    private int visits;
    private long time;
    private long maxtime;
    private List<String> items;
    private Map<String, Integer> note;
    private boolean priority;

    public Shop(UUID uuid, String name, Location location) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.visits = 0;
        this.time =  System.currentTimeMillis();
        this.maxtime = 0;
        this.items = new ArrayList<>();
        this.note = new HashMap<>();
        this.priority = false;
    }

    public int getNotes() {

        int votes = note.values().stream().mapToInt($ -> $).sum();

        if (votes == 0) return 0;

        return votes / note.size();
    }

    public String getNotesString() {

        float percent = (float) getNotes() / 5;
        int progress = (int) (5 * percent);

        return Strings.repeat("§6✭", progress) + Strings.repeat("§7✭", 5 - progress);

    }

    public boolean timeEnded(long time) {
        return !getTimeFormatted(time).equals("agora");
    }

    public String getTimeFormatted(long time) {

        long last = System.currentTimeMillis() - time;

        return TimeFormatter.format(this.time - last);
    }

    public String getTimeFormatted() {
        return TimeFormatter.format(System.currentTimeMillis() - this.time);
    }


}
