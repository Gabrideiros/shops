package me.gabrideiros.lojas.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.gabrideiros.lojas.util.TimeFormatter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Shop {

    private String name;
    private Location location;
    private int visits;
    private long time;
    private String message;
    private List<String> items;
    private Map<String, Integer> note;
    private boolean priority;

    public Shop(String name, Location location) {
        this.name = name;
        this.location = location;
        this.visits = 0;
        this.time =  System.currentTimeMillis();
        this.message = null;
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

    public boolean endedTime(long time) {
        return getTimeFormatted(time).equals("agora");
    }

    public String getTimeFormatted(long time) {

        long last = System.currentTimeMillis() - time;

        return TimeFormatter.format(this.time - last);
    }

    public String getTimeFormatted() {
        return TimeFormatter.format(System.currentTimeMillis() - this.time);
    }


}
