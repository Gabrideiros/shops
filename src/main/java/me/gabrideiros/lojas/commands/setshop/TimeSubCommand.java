package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TimeSubCommand extends SubCommand {

    private final ShopController controller;

    public TimeSubCommand(Main plugin, CommandBase command, ShopController controller) {
        super(plugin, command, "tempo", null, null, "loja.criar");

        this.controller = controller;
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

        player.sendMessage("§aTempo restante: §f" + shop.getTimeFormatted(TimeUnit.DAYS.toMillis(7)));

    }
}
