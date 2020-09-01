package me.gabrideiros.lojas;

import dev.arantes.inventorymenulib.listeners.InventoryListener;
import lombok.Getter;
import me.gabrideiros.lojas.commands.advertising.AdvertisingCommand;
import me.gabrideiros.lojas.commands.priorityshop.SetPriorityCommand;
import me.gabrideiros.lojas.commands.setshop.SetCommand;
import me.gabrideiros.lojas.commands.shop.ShopCommand;
import me.gabrideiros.lojas.commands.shops.ShopsCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.database.*;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import me.gabrideiros.lojas.timers.AdvertisingTimer;
import me.gabrideiros.lojas.timers.SaveTimer;
import me.gabrideiros.lojas.timers.VerifyTimer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    private ShopController shopController;
    private AdvertisingController advertisingController;

    private ShopService shopService;
    private AdvertisingService advertisingService;

    private Storage storage;

    private ShopInventory inventory;

    private Economy economy;

    private int saveTime;
    private int verifyTime;
    private int advertisingTime;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        loadConfiguration();

        if (!setupEconomy()) {
            getServer().getConsoleSender().sendMessage("§cVault não encontrado! Desabilitando plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        shopController = new ShopController();
        advertisingController = new AdvertisingController();

        openStorage();

        shopService = new ShopService(this, shopController, storage);
        advertisingService = new AdvertisingService(this, advertisingController, storage);

        inventory = new ShopInventory(this);

        new InventoryListener(this);

        registerCommands();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SaveTimer(shopService, advertisingService), 20 * 60 * saveTime, 20 * 60 * saveTime);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new VerifyTimer(shopController, advertisingController, shopService, advertisingService), 20 * 60 * verifyTime, 20 * 60 * verifyTime);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AdvertisingTimer(advertisingController), 20 * 60 * advertisingTime, 20 * 60 * advertisingTime);

    }

    @Override
    public void onDisable() {
        shopService.close();
        advertisingService.close();
        storage.closeConnection();
    }

    private void registerCommands() {

        new ShopsCommand(this, inventory);
        new SetCommand(this, shopController, shopService, advertisingController, advertisingService, inventory, economy);
        new ShopCommand(this, shopController, advertisingController, shopService, advertisingService, inventory);
        new SetPriorityCommand(this, shopController);
        new AdvertisingCommand(this, shopController, advertisingController, advertisingService, economy);
    }

    public void openStorage() {

        String type = getConfig().getString("Connection.Type");

        switch (type) {
            case "MySQL":
                storage = new MySQLDatabase(this);
                break;
            case "MySQLPooling":
                storage = new PoolingDatabase(this);
                break;
            default:
                storage = new SQLDatabase(this, "data.db");
                break;
        }
    }

    public void loadConfiguration() {
        saveTime = getConfig().getInt("SaveTime");
        verifyTime = getConfig().getInt("VerifyTime");
        advertisingTime = getConfig().getInt("AdvertisingTime");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

}
