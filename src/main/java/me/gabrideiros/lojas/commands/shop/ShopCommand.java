package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends CommandBase {

    private final ShopController shopController;
    private final ShopInventory inventory;

    public ShopCommand(Main plugin, ShopController shopController, AdvertisingController advertisingController, ShopService shopService, AdvertisingService advertisingService, ShopInventory inventory) {
        super(plugin, "loja", null, null);

        this.shopController = shopController;

        this.inventory = inventory;

        register(new EvaluateSubCommand(plugin, this, shopController, inventory));
        register(new DeleteSubCommand(plugin, this, shopController, advertisingController, shopService, advertisingService));
        register(new SetHomeSubCommand(plugin, this, shopController));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    "",
                    "§7 * §e/loja <nick> §7 - Para ver a loja de um jogador.",
                    "§7 * §e/loja avaliar <nick> §7 - Para avaliar uma loja.",
                    ""
            });
            return true;
        }

        String key = args[0];

        if (!getSubCommandMap().containsKey(key)) {

            if (!(sender instanceof Player)) return true;

            Player player = (Player) sender;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(key);

            if (shopController.getByName(offlinePlayer.getName()) == null) {
                player.sendMessage("§cEste jogador não possui uma loja!");
                return true;
            }

            Shop shop = shopController.getByName(offlinePlayer.getName());

            inventory.openShop(shop, player);
            return true;
        }

        SubCommand subCommand = getSubCommandMap().get(key);

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) return true;

        subCommand.execute(sender, args);

        return true;
    }
}
