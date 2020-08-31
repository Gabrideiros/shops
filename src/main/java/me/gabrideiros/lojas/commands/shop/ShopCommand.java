package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.gui.ShopInventory;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends CommandBase {

    private final ShopController controller;
    private final ShopInventory inventory;

    public ShopCommand(Main plugin, ShopController controller, ShopInventory inventory) {
        super(plugin, "loja", null, null);

        this.controller = controller;
        this.inventory = inventory;

        register(new EvaluateSubCommand(plugin, this, controller));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        String key = args[0];

        if (!getSubCommandMap().containsKey(key)) {

            if (!(sender instanceof Player)) return true;

            Player player = (Player) sender;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(key);

            if (controller.getByName(offlinePlayer.getName()) == null) {
                player.sendMessage("§cEste jogador não possui uma loja!");
                return true;
            }

            Shop shop = controller.getByName(offlinePlayer.getName());

            inventory.openShop(shop, player);
            return true;
        }

        SubCommand subCommand = getSubCommandMap().get(key);

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) return true;

        subCommand.execute(sender, args);

        return true;
    }
}
