package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class DeleteSubCommand extends SubCommand {

    private final ShopController shopController;
    private final AdvertisingController advertisingController;
    private final ShopService shopService;
    private final AdvertisingService advertisingService;


    public DeleteSubCommand(Main plugin, CommandBase command, ShopController shopController, AdvertisingController advertisingController, ShopService shopService, AdvertisingService advertisingService) {
        super(plugin, command, "deletar", "§c[!] Utilize /loja deletar <nick>.", null, "loja.admin");

        this.shopController = shopController;
        this.advertisingController = advertisingController;
        this.shopService = shopService;
        this.advertisingService = advertisingService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(getUsage());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (shopController.getByName(offlinePlayer.getName()) == null) {
            sender.sendMessage("§cEste jogador não possui uma loja!");
            return;
        }

        Shop shop = shopController.getByName(offlinePlayer.getName());

        shopService.delete(shop);

        Advertising advertising = advertisingController.getByName(offlinePlayer.getName());

        if (advertising != null)
            advertisingService.delete(advertising);

        sender.sendMessage("§cLoja deletada com sucesso!");

    }
}
