package me.gabrideiros.lojas.commands.priorityshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class SetSubCommand extends SubCommand {

    private final ShopController shopController;

    public SetSubCommand(Main plugin, CommandBase command, ShopController shopController) {
        super(plugin, command, "setar", "§c[!] Utilize /lojaprioritaria setar <nick>.", null, "loja.admin");
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

        if (shop.isPriority()) {
            shop.setMaxtime(shop.getMaxtime() + TimeUnit.DAYS.toMillis(30));
            sender.sendMessage("§aVocê adicionou mais 30d para esta loja prioritária!");
            return;
        }

        shop.setPriority(true);
        shop.setMaxtime(TimeUnit.DAYS.toMillis(30));
        sender.sendMessage("§aEsta loja foi setada como prioritária!");

    }

}

