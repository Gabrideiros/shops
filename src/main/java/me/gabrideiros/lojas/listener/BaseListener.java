package me.gabrideiros.lojas.listener;

import dev.arantes.inventorymenulib.buttons.ItemButton;
import lombok.Getter;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class BaseListener implements Listener {

    private final Main plugin;

    private final ShopController shopController;

    private final ShopInventory inventory;

    @Getter
    public static List<String> teleport;
    @Getter
    public static Map<String, Integer> nameMap;
    @Getter
    public static List<String> messageList;

    @Getter
    public static Map<String, String> confirmMap;


    public BaseListener(Main plugin, ShopController shopController, ShopInventory inventory) {
        this.plugin = plugin;
        this.shopController = shopController;
        this.inventory = inventory;

        teleport = new ArrayList<>();
        nameMap = new HashMap<>();
        messageList = new ArrayList<>();
        confirmMap = new HashMap<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();

        if (!teleport.contains(player.getName())) return;

        teleport.remove(player.getName());
        player.sendMessage("§cO teleporte foi cancelado!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        String message = e.getMessage();

        if (messageList.contains(player.getName())) {

            e.setCancelled(true);

            if (message.equalsIgnoreCase("cancelar")) {
                messageList.remove(player.getName());
                player.sendMessage("§cAção cancelada com sucesso!");
                return;
            }


            confirmMap.put(player.getName(), message.replace("&", "§"));
            player.sendMessage("§aDigite '/propaganda confirmar' caso realmente deseja editar ou criar sua propaganda!");

            String name = player.getName();

            new BukkitRunnable() {
                @Override
                public void run() {
                    confirmMap.remove(name);
                }
            }.runTaskLater(plugin, 20 * 30);

        }

        else if (nameMap.containsKey(player.getName())) {

            e.setCancelled(true);

            if (message.equalsIgnoreCase("cancelar")) {
                nameMap.remove(player.getName());
                player.sendMessage("§cAção cancelada com sucesso!");
                return;
            }

            int position = nameMap.get(player.getName());

            Shop shop = shopController.getByPlayer(player);

            ItemStack item = ItemButton.fromBase64(shop.getItems().get(position));

            item = new ItemButton(item.clone()).setName(message.replace("&", "§")).getItem();

            shop.getItems().set(position, ItemButton.toBase64(item));

            nameMap.remove(player.getName());

            inventory.openInfo(shop, player);

        }
    }
}
