package me.gabrideiros.lojas.commands;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class SetCommand implements CommandExecutor {

    private final ShopController controller;
    private final SQLManager sqlManager;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.hasPermission("loja.criar")) return true;


        if (controller.getByPlayer(player) != null) {
            player.sendMessage("§cVocê ja possui uma loja!");
            return true;
        }

        Shop shop = new Shop(player.getName(), player.getLocation(), 0, 0, new HashMap<>());

        CompletableFuture.runAsync(() -> sqlManager.insertShop(shop));

        player.sendMessage("§aLoja criada em sua localização!");

        return true;
    }
}
