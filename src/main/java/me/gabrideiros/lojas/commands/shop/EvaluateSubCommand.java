package me.gabrideiros.lojas.commands.shop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EvaluateSubCommand extends SubCommand {

    private final ShopController controller;

    public EvaluateSubCommand(Main plugin, CommandBase command, ShopController controller) {
        super(plugin, command, "avaliar", "§c[!] Utilize /loja avaliar <nick> <nota>.", null, null);

        this.controller = controller;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (args.length < 3) {
            player.sendMessage(getUsage());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (controller.getByName(offlinePlayer.getName()) == null) {
            player.sendMessage("§cEste jogador não possui uma loja!");
            return;
        }

        Shop shop = controller.getByName(offlinePlayer.getName());

        if (shop.getNote().containsKey(player.getName())) {
            player.sendMessage("§cVocê já avaliou esta loja!");
            return;
        }

        try {

            Integer.parseInt(args[2]);

        } catch (NumberFormatException e) {
            player.sendMessage("§cDigite apenas números válidos!");
            return;
        }

        int note = Integer.parseInt(args[2]);

        if (note < 1 || note > 5) {
            player.sendMessage("§cVocê só pode dar uma nota de 1 a 5!");
            return;
        }

        shop.getNote().put(player.getName(), note);

        player.sendMessage("§aVocê avaliou a loja de §f" + shop.getName() + "§a como " + note + "§a estrelas!");

    }
}
