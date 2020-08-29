package me.gabrideiros.lojas;

import dev.arantes.inventorymenulib.listeners.InventoryListener;
import lombok.Getter;
import me.gabrideiros.lojas.commands.DelCommand;
import me.gabrideiros.lojas.commands.SetCommand;
import me.gabrideiros.lojas.commands.ShopsCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.gui.ShopsInventory;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    private ShopController controller;

    private SQLManager sqlManager;

    private ShopsInventory inventory;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        controller = new ShopController();

        sqlManager = new SQLManager(this);

        inventory = new ShopsInventory(this);

        new InventoryListener(this);

        registerCommands();

    }

    @Override
    public void onDisable() {
        sqlManager.close();
    }

    private void registerCommands() {
        getCommand("setloja").setExecutor(new SetCommand(controller, sqlManager));
        getCommand("delloja").setExecutor(new DelCommand(controller, sqlManager));
        getCommand("lojas").setExecutor(new ShopsCommand(inventory));
    }
}
