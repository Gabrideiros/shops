package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.listener.BaseListener;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.services.AdvertisingService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConfirmSubCommand extends SubCommand {

    private final ShopController shopController;
    private final AdvertisingController advertisingController;
    private final AdvertisingService advertisingService;

    public ConfirmSubCommand(Main plugin, CommandBase command, ShopController shopController, AdvertisingController advertisingController, AdvertisingService advertisingService) {
        super(plugin, command, "confirmar", null, null, "loja.propaganda");

        this.shopController = shopController;
        this.advertisingController = advertisingController;
        this.advertisingService = advertisingService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (!BaseListener.getConfirmMap().containsKey(player.getName())) {
            player.sendMessage("§cVocê não possui nenhuma confirmação pendente!");
            return;
        }

        String message = BaseListener.getConfirmMap().get(player.getName());

        if (advertisingController.getByPlayer(player) != null) {

            Advertising advertising = advertisingController.getByPlayer(player);

            advertising.setMessage(message);

            BaseListener.getConfirmMap().remove(player.getName());

            player.sendMessage("§aPropaganda editada com sucesso!");
            return;
        }

        if (shopController.getByPlayer(player) == null) {
            player.sendMessage("§cVocê não possui uma loja!");
            BaseListener.getConfirmMap().remove(player.getName());
            return;
        }

        Advertising advertising = new Advertising(player.getUniqueId(), player.getName(), message);
        CompletableFuture.runAsync(() -> advertisingService.insert(advertising));

        player.sendMessage("§aPropaganda criada com sucesso!");
        BaseListener.getConfirmMap().remove(player.getName());


    }
}
