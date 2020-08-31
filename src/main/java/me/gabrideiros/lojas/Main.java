package me.gabrideiros.lojas;

import dev.arantes.inventorymenulib.listeners.InventoryListener;
import lombok.Getter;
import me.gabrideiros.lojas.commands.setshop.SetCommand;
import me.gabrideiros.lojas.commands.shop.ShopCommand;
import me.gabrideiros.lojas.commands.shops.ShopsCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.gui.ShopInventory;
import me.gabrideiros.lojas.timers.VerifyTimer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    private ShopController controller;

    private SQLManager sqlManager;

    private ShopInventory inventory;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        controller = new ShopController();

        sqlManager = new SQLManager(this);

        inventory = new ShopInventory(this);

        new InventoryListener(this);

        registerCommands();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new VerifyTimer(controller, sqlManager), 20 * 60 * 60, 20 * 60 * 60);

    }

    @Override
    public void onDisable() {
        sqlManager.close();
    }

    private void registerCommands() {

        new ShopsCommand(this, inventory);
        new SetCommand(this, controller, sqlManager, inventory);
        new ShopCommand(this, controller, inventory);

    }
}
