package me.gabrideiros.lojas.commands.priorityshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetPriorityCommand extends CommandBase {

    private final ShopController shopController;

    public SetPriorityCommand(Main plugin, ShopController shopController) {
        super(plugin, "lojaprioritaria", null, null);

        this.shopController = shopController;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!sender.hasPermission("loja.admin")) return true;

        if (args.length < 2) {
            sender.sendMessage("§c[!] Utilize /lojaprioritaria setar <nick>.");
            return true;
        }

        if (args[0].equalsIgnoreCase("setar")) {

            if (args.length > 2) {
                sender.sendMessage("§c[!] Utilize /lojaprioritaria setar <nick>.");
                return true;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

            if (shopController.getByName(offlinePlayer.getName()) == null) {
                sender.sendMessage("§cEste jogador não possui uma loja");
            }

            Shop shop = shopController.getByName(offlinePlayer.getName());

            if (shop.isPriority()) {
                sender.sendMessage("§cEsta loja já esta setada como prioritária!");
                return true;
            }

            shop.setPriority(true);
            sender.sendMessage("§aEsta loja foi setada como prioritária!");
            return true;
        }

        return true;
    }
}
