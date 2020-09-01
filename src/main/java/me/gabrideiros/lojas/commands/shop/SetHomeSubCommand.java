package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeSubCommand extends SubCommand {

    private final ShopController shopController;

    public SetHomeSubCommand(Main plugin, CommandBase command, ShopController shopController) {
        super(plugin, command, "sethome", "§c[!] Utilize /loja sethome <nick>.", null, "loja.admin");

        this.shopController = shopController;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

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

        shop.setLocation(player.getLocation());

        sender.sendMessage("§aHome desta loja setada em sua localização!");

    }
}
