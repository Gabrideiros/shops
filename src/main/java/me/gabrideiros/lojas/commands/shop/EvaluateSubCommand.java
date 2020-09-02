package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EvaluateSubCommand extends SubCommand {

    private final ShopController controller;
    private final ShopInventory inventory;

    public EvaluateSubCommand(Main plugin, CommandBase command, ShopController controller, ShopInventory inventory) {
        super(plugin, command, "avaliar", "§c[!] Utilize /loja avaliar <nick>.", null, null);

        this.controller = controller;
        this.inventory = inventory;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(getUsage());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (player.getName().equalsIgnoreCase(offlinePlayer.getName())) {
            player.sendMessage("§cVocê não pode avaliar sua própria loja!");
            return;
        }

        if (controller.getByName(offlinePlayer.getName()) == null) {
            player.sendMessage("§cEste jogador não possui uma loja!");
            return;
        }

        Shop shop = controller.getByName(offlinePlayer.getName());

        if (shop.getNote().containsKey(player.getName())) {
            player.sendMessage("§cVocê já avaliou esta loja!");
            return;
        }

        inventory.openNote(shop, player);
    }
}
