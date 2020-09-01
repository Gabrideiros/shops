package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class CreateSubCommand extends SubCommand {

    private final AdvertisingController advertisingController;
    private final Map<String, String> confirm;
    private final Economy economy;

    public CreateSubCommand(Main plugin, CommandBase command, AdvertisingController advertisingController, Map<String, String> confirm, Economy economy) {
        super(plugin, command, "criar", "§c[!] Utilize /propaganda criar <mensagem>.", null, "loja.criar");

        this.advertisingController = advertisingController;

        this.confirm = confirm;
        this.economy = economy;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(getUsage());
            return;
        }

        if (advertisingController.getByPlayer(player) != null) {
            player.sendMessage("§cVocê já possui uma propaganda!");
            return;
        }

        if (economy.getBalance(player) < 250000) {
            player.sendMessage("§cVocê precisa de 250k para criar uma propaganda!");
            return;
        }

        if (advertisingController.getElements().size() == 4) {
            player.sendMessage("§cO limite de propagandas do servidor foi atingido!");
            return;
        }

        args[0] = "";
        String message = String.join(" ", args);

        confirm.put(player.getName(), message);

        player.sendMessage("§aDigite '/propaganda confirmar' caso realmente deseja criar uma propaganda!");

        String name = player.getName();

        new BukkitRunnable() {
            @Override
            public void run() {

                confirm.remove(name);
            }
        }.runTaskLater(getPlugin(), 20 * 30);
    }
}
