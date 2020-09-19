package me.gabrideiros.lojas.commands.priorityshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class DelSubCommand extends SubCommand {

    private final ShopController shopController;

    public DelSubCommand(Main plugin, CommandBase command, ShopController shopController) {
        super(plugin, command, "remover", "§c[!] Utilize /lojaprioritaria remover <nick>.", null, "loja.admin");

        this.shopController = shopController;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(getUsage());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (shopController.getByName(offlinePlayer.getName()) == null) {
            sender.sendMessage("§cEste jogador não possui uma loja");
        }

        Shop shop = shopController.getByName(offlinePlayer.getName());

        if (!shop.isPriority()) {
            sender.sendMessage("§cA loja deste jogador não é prioritária!");
            return;
        }

        shop.setPriority(false);
        shop.setMaxtime(0);
        shop.setTime(System.currentTimeMillis());
        sender.sendMessage("§aEsta loja foi setada como normal!");
    }
}
