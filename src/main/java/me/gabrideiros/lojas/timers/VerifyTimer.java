package me.gabrideiros.lojas.timers;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class VerifyTimer extends BukkitRunnable {

    private final ShopController shopController;
    private final AdvertisingController advertisingController;
    private final ShopService shopService;
    private final AdvertisingService advertisingService;

    @Override
    public void run() {

        for (Shop shop : shopController.getElements()) {

            if (shop.isPriority()) {
                if (shop.timeEnded(shop.getMaxtime())) continue;
                shop.setPriority(false);
                shop.setMaxtime(0);
                shop.setTime(System.currentTimeMillis());
                continue;
            }

            if (shop.timeEnded(TimeUnit.DAYS.toMillis(7))) continue;

            Advertising advertising = advertisingController.getByName(shop.getName());

            if (advertising != null)
                advertisingService.delete(advertising);

            shopService.delete(shop);
        }

        for (Advertising advertising : advertisingController.getElements()) {

            if (!advertising.timeEnded(TimeUnit.DAYS.toMillis(7))) continue;

            advertisingService.delete(advertising);

            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + advertising.getName() + " perm unset loja.propaganda");

        }
    }
}
