package me.gabrideiros.lojas.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.gabrideiros.lojas.utils.TimeFormatter;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Advertising {

    private UUID uuid;
    private String name;
    private String message;
    private long time;

    public Advertising(UUID uuid, String name, String message) {
        this.uuid = uuid;
        this.name = name;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public boolean endedTime(long time) {
        return getTimeFormatted(time).equals("agora");
    }

    public String getTimeFormatted(long time) {

        long last = System.currentTimeMillis() - time;

        return TimeFormatter.format(this.time - last);
    }

}
