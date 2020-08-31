package me.gabrideiros.lojas.timers;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class VerifyTimer extends BukkitRunnable {

    private final ShopController controller;
    private final SQLManager sqlManager;

    @Override
    public void run() {

        for (Shop shop : controller.getElements()) {

            if (shop.isPriority()) {
                if (shop.endedTime(TimeUnit.DAYS.toMillis(30))) {
                    shop.setPriority(false);
                    shop.setTime(System.currentTimeMillis());
                }
                continue;
            }

            if (shop.endedTime(TimeUnit.DAYS.toMillis(7))) {
                sqlManager.delete(shop);
            }
        }
    }
}
