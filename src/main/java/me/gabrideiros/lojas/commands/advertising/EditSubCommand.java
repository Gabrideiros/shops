package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.listener.BaseListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EditSubCommand extends SubCommand {

    private final AdvertisingController advertisingController;

    public EditSubCommand(Main plugin, CommandBase command, AdvertisingController advertisingController) {
        super(plugin, command, "editar", "§c[!] Utilize /propaganda editar <mensagem>.", null, "loja.propaganda");

        this.advertisingController = advertisingController;

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

        BaseListener.messageList.add(player.getName());

        player.sendMessage(new String[]{
                "",
                "§aDigite no chat a mensagem que deseja adicionar:",
                "§7Caso deseja cancelar digite 'cancelar'!",
                ""
        });

        String name = player.getName();

        new BukkitRunnable() {
            @Override
            public void run() {

                BaseListener.getMessageList().remove(name);
            }
        }.runTaskLater(getPlugin(), 20 * 30);
    }
}
