package me.gabrideiros.lojas.timers;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
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
                if (!shop.endedTime(TimeUnit.DAYS.toMillis(30))) continue;
                shop.setPriority(false);
                shop.setTime(System.currentTimeMillis());
                continue;
            }

            if (!shop.endedTime(TimeUnit.DAYS.toMillis(7))) continue;

            Advertising advertising = advertisingController.getByName(shop.getName());

            if (advertising != null)
                advertisingService.delete(advertising);

            shopService.delete(shop);
        }

        for (Advertising advertising : advertisingController.getElements()) {

            if (!advertising.endedTime(TimeUnit.DAYS.toMillis(4))) continue;

            advertisingService.delete(advertising);
        }
    }
}
