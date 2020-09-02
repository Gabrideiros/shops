package me.gabrideiros.lojas.timers;

import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.utils.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvertisingTimer extends BukkitRunnable {

    private final AdvertisingController advertisingController;

    private int time = -1;

    public AdvertisingTimer(AdvertisingController advertisingController) {
        this.advertisingController = advertisingController;
    }

    @Override
    public void run() {

        List<Advertising> list = new ArrayList<>(advertisingController.getElements());

        list.sort(Comparator.comparingLong(Advertising::getTime).reversed());

        for (int i = time; i < list.size(); ++i) {

            try {

                Advertising advertising = list.get(i);
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

                return;

            } catch (IndexOutOfBoundsException ignored) {

            }
        }
    }
}
