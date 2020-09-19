package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.enums.ConfirmType;
import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import java.util.Map;

public class ConfirmSubCommand extends SubCommand {

    private final ShopController shopController;
    private final AdvertisingController advertisingController;
    private final ShopService shopService;
    private final AdvertisingService advertisingService;


    private final Map<String, ConfirmType> confirm;
    private final Economy economy;

    public ConfirmSubCommand(Main plugin, CommandBase command, ShopController shopController, AdvertisingController advertisingController, ShopService shopService, AdvertisingService advertisingService, Map<String, ConfirmType> confirm, Economy economy) {

        super(plugin, command, "confirmar", null, null, "loja.criar");

        this.advertisingController = advertisingController;
        this.advertisingService = advertisingService;

        this.shopController = shopController;
        this.shopService = shopService;

        this.confirm = confirm;
        this.economy = economy;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (!confirm.containsKey(player.getName())) {
            player.sendMessage("§cVocê não possui nenhuma confirmação pendente!");
            return;
        }

        ConfirmType type = confirm.get(player.getName());

        switch (type) {
            case CREATE:

                if (shopController.getByPlayer(player) != null) {
                    player.sendMessage("§cVocê já possui uma loja!");
                    confirm.remove(player.getName());
                    return;
                }

                if (economy.getBalance(player) < 250000) {
                    player.sendMessage("§cVocê precisa de 250k para criar uma loja!");
                    return;
                }

                Shop shop = new Shop(player.getUniqueId(), player.getName(), player.getLocation());

                shopService.insert(shop);

                economy.withdrawPlayer(player, 250000);

                confirm.remove(player.getName());

                player.sendMessage("§aLoja criada em sua localização!");
                break;
            case DELETE:

                if (shopController.getByPlayer(player) == null) {
                    player.sendMessage("§cVocê não possui uma loja!");
                    confirm.remove(player.getName());
                    return;
                }

                Shop shop2 = shopController.getByPlayer(player);

                shopService.delete(shop2);

                Advertising advertising = advertisingController.getByPlayer(player);

                if (advertising != null)
                    advertisingService.delete(advertising);

                player.sendMessage("§cLoja deletada com sucesso!");

                confirm.remove(player.getName());

                break;
        }
    }
}
