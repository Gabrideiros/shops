package me.gabrideiros.lojas.timers;

import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.utils.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdvertisingTimer extends BukkitRunnable {

    private final AdvertisingController advertisingController;

    private int time = 0;

    public AdvertisingTimer(AdvertisingController advertisingController) {
        this.advertisingController = advertisingController;
    }

    @Override

    public void run() {

        List<Advertising> list = new ArrayList<>(advertisingController.getElements());

        list.sort((a1,a2) -> Long.compare(a2.getTime(), a1.getTime()));
        Collections.reverse(list);

        if (time == list.size()) {
            time = 0;
        }

        Advertising advertising = list.get(time);
        time++;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            new FancyMessage("§6[§lPROPAGANDA§6] ")
                    .text(advertising.getMessage().replace("&", "§"))
                    .hover("§7Clique para ver informações da loja de " + advertising.getName() + "§7!")
                    .command("/loja " + advertising.getName())
                    .send(player);
            player.sendMessage("");
        }
    }
}
