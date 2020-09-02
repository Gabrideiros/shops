package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.services.AdvertisingService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class DeleteSubCommand extends SubCommand {

    private final AdvertisingController advertisingController;
    private final AdvertisingService advertisingService;


    public DeleteSubCommand(Main plugin, CommandBase command,  AdvertisingController advertisingController, AdvertisingService advertisingService) {
        super(plugin, command, "deletar", "§c[!] Utilize /propaganda deletar <nick>.", null, "loja.admin");

        this.advertisingController = advertisingController;
        this.advertisingService = advertisingService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(getUsage());
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (advertisingController.getByName(offlinePlayer.getName()) == null) {
            sender.sendMessage("§cEste jogador não possui uma propaganda!");
            return;
        }

        Advertising advertising = advertisingController.getByName(offlinePlayer.getName());

        advertisingService.delete(advertising);

        sender.sendMessage("§cPropaganda deletada com sucesso!");
    }
}