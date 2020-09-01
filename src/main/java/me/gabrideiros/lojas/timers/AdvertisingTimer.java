package me.gabrideiros.lojas.timers;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.utils.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class AdvertisingTimer extends BukkitRunnable {

    private final AdvertisingController advertisingController;

    @Override
    public void run() {

        for (Advertising advertising : advertisingController.getElements()) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                new FancyMessage("§6[PROPAGANDA] ")
                        .text(advertising.getMessage().replace("&", "§"))
                        .hover("§7Clique para ver informações!")
                        .command("/loja " + advertising.getName())
                        .send(player);
            }
        }
    }
}
