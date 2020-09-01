package me.gabrideiros.lojas.timers;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class SaveTimer extends BukkitRunnable {

    private final ShopService shopService;
    private final AdvertisingService advertisingService;

    @Override
    public void run() {
        shopService.saveAll();
        advertisingService.saveAll();
    }
}
