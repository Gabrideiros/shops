package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class RenewSubCommand extends SubCommand {

    private final ShopController controller;
    private final Economy economy;

    public RenewSubCommand(Main plugin, CommandBase command, ShopController controller, Economy economy) {

        super(plugin, command, "renovar", null, null, "loja.criar");

        this.controller = controller;
        this.economy = economy;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (controller.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja para renovar!");
            return;
        }

        if (economy.getBalance(player) < 250000) {
            player.sendMessage("§cVocê precisa de 250k para renovar sua loja!");
            return;
        }

        Shop shop = controller.getByPlayer(player);

        if (shop.isPriority()) {
            player.sendMessage("§cVocê não pode renovar sua loja prioritária!");
            return;
        }

        if (shop.timeEnded(TimeUnit.DAYS.toMillis(5))) {
            player.sendMessage("§cVocê só pode renovar sua loja nas últimas 48 horas!");
            return;
        }

        economy.withdrawPlayer(player, 250000);

        shop.setTime(System.currentTimeMillis());
        player.sendMessage("§aMuito bem! Sua loja foi renovada com sucesso!");

    }
}
