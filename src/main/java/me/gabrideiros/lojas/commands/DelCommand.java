package me.gabrideiros.lojas.commands;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@AllArgsConstructor
public class DelCommand implements CommandExecutor {

    private final ShopController controller;
    private final SQLManager sqlManager;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.hasPermission("loja.criar")) return true;


        if (controller.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja!");
            return true;
        }

        Shop shop = controller.getByPlayer(player);

        sqlManager.delete(shop);

        player.sendMessage("§cLoja deletada com sucesso!");

        return true;
    }
}
