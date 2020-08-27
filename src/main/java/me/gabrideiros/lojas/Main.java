package me.gabrideiros.lojas;

import me.gabrideiros.lojas.commands.SetCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ShopController controller;

    private SQLManager sqlManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        controller = new ShopController();

        (sqlManager = new SQLManager(controller, this)).createTable();

    }

    @Override
    public void onDisable() {
        sqlManager.close();
    }

    private void registerCommands() {
        getCommand("setloja").setExecutor(new SetCommand(controller, sqlManager));
    }
}
