package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.services.AdvertisingService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConfirmSubCommand extends SubCommand {

    private final ShopController shopController;
    private final AdvertisingController advertisingController;
    private final AdvertisingService advertisingService;
    private final Map<String, String> confirm;

    public ConfirmSubCommand(Main plugin, CommandBase command, ShopController shopController, AdvertisingController advertisingController, AdvertisingService advertisingService, Map<String, String> confirm) {
        super(plugin, command, "confirmar", null, null, "loja.criar");

        this.shopController = shopController;
        this.advertisingController = advertisingController;
        this.advertisingService = advertisingService;

        this.confirm = confirm;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (!confirm.containsKey(player.getName())) {
            player.sendMessage("§cVocê não possui nenhuma confirmação pendente!");
            return;
        }

        String message = confirm.get(player.getName());

        if (advertisingController.getByPlayer(player) != null) {

            Advertising advertising = advertisingController.getByPlayer(player);

            advertising.setMessage(message);

            confirm.remove(player.getName());

            player.sendMessage("§aPropaganda editada com sucesso!");
            return;
        }

        if (shopController.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja!");
            return;
        }

        Advertising advertising = new Advertising(player.getName(), message);
        advertisingService.insert(advertising);

        player.sendMessage("§aPropaganda criada com sucesso!");
        confirm.remove(player.getName());


    }
}
