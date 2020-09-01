package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class EditSubCommand extends SubCommand {

    private final AdvertisingController advertisingController;
    private final Map<String, String> confirm;

    public EditSubCommand(Main plugin, CommandBase command, AdvertisingController advertisingController, Map<String, String> confirm) {
        super(plugin, command, "editar", "§c[!] Utilize /propaganda editar <mensagem>.", null, "loja.criar");

        this.advertisingController = advertisingController;

        this.confirm = confirm;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(getUsage());
            return;
        }

        if (advertisingController.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma propaganda!");
            return;
        }

        args[0] = "";
        String message = String.join(" ", args);

        confirm.put(player.getName(), message);

        player.sendMessage("§aDigite '/propaganda confirmar' caso realmente deseja editar sua propaganda!");

        String name = player.getName();

        new BukkitRunnable() {
            @Override
            public void run() {

                confirm.remove(name);
            }
        }.runTaskLater(getPlugin(), 20 * 30);

    }
}
