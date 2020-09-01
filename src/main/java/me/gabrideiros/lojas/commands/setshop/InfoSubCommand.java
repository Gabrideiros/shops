package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoSubCommand extends SubCommand {

    private final ShopController controller;
    private final ShopInventory inventory;

    public InfoSubCommand(Main plugin, CommandBase command, ShopController controller, ShopInventory inventory) {
        super(plugin, command, "info", null, null, "loja.criar");

        this.controller = controller;
        this.inventory = inventory;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (controller.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja!");
            return;
        }

        Shop shop = controller.getByPlayer(player);

        inventory.openInfo(shop, player);

    }
}
