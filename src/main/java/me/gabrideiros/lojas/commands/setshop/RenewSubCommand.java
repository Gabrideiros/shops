package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class RenewSubCommand extends SubCommand {

    private final ShopController controller;

    public RenewSubCommand(Main plugin, CommandBase command, ShopController controller) {

        super(plugin, command, "renovar", null, null, null);

        this.controller = controller;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (!player.hasPermission("loja.criar")) return;


        if (controller.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja para renovar!");
            return;
        }

        Shop shop = controller.getByPlayer(player);

        if (!shop.endedTime(TimeUnit.DAYS.toMillis(5))) {
            player.sendMessage("§cVocê só pode renovar sua loja nas últimas 48 horas!");
            return;
        }

        shop.setTime(System.currentTimeMillis());
        player.sendMessage("§aMuito bem! Sua loja foi renovada com sucesso!");

    }
}
