package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.listener.BaseListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateSubCommand extends SubCommand {

    private final AdvertisingController advertisingController;

    public CreateSubCommand(Main plugin, CommandBase command, AdvertisingController advertisingController) {
        super(plugin, command, "criar", null, null, "loja.propaganda");

        this.advertisingController = advertisingController;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;


        if (advertisingController.getByPlayer(player) != null) {
            player.sendMessage("§cVocê já possui uma propaganda!");
            return;
        }

        if (advertisingController.getElements().size() == 4) {
            player.sendMessage("§cO limite de propagandas do servidor foi atingido!");
            return;
        }


        BaseListener.getMessageList().add(player.getName());

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
