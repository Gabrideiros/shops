package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.enums.ConfirmType;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import java.util.Map;

public class ConfirmSubCommand extends SubCommand {

    private final ShopController controller;
    private final SQLManager sqlManager;


    private final Map<String, ConfirmType> confirm;

    public ConfirmSubCommand(Main plugin, CommandBase command, ShopController controller, SQLManager sqlManager, Map<String, ConfirmType> confirm) {

        super(plugin, command, "confirmar", null, null, null);

        this.controller = controller;
        this.sqlManager = sqlManager;
        this.confirm = confirm;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (!player.hasPermission("loja.criar")) return;

        if (!confirm.containsKey(player.getName())) {
            player.sendMessage("§cVocê não possui nenhuma confirmação pendente!");
            return;
        }

        ConfirmType type = confirm.get(player.getName());

        switch (type) {
            case CREATE:

                if (controller.getByPlayer(player) != null) {
                    player.sendMessage("§cVocê ja possui uma loja!");
                    return;
                }

                Shop shop = new Shop(player.getName(), player.getLocation());

                CompletableFuture.runAsync(() -> sqlManager.insertShop(shop));

                player.sendMessage("§aLoja criada em sua localização!");
                confirm.remove(player.getName());
                break;
            case DELETE:

                if (controller.getByPlayer(player) == null) {
                    player.sendMessage("§cVocê não possui uma loja!");
                    return;
                }

                Shop shop2 = controller.getByPlayer(player);

                sqlManager.delete(shop2);

                player.sendMessage("§cLoja deletada com sucesso!");

                confirm.remove(player.getName());

                break;
        }
    }
}
