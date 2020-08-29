package me.gabrideiros.lojas.gui;

import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import dev.arantes.inventorymenulib.menus.InventoryGUI;
import dev.arantes.inventorymenulib.menus.PaginatedGUI;
import dev.arantes.inventorymenulib.utils.InventorySize;
import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShopsInventory {

    private final Main plugin;

    public void openShops(Player player, FilterType type) {

        ArrayList<ItemButton> items = new ArrayList<>();

        Set<Shop> shops = plugin.getController().getElements();

        shops.forEach($ ->
                items.add(
                        new ItemButton(Material.SKULL_ITEM,
                                3,
                                1,
                                "§aLoja de " + $.getName()).setHead($.getName())
                                .addAction(ClickType.LEFT, event -> teleport(player, $))
                                .addAction(ClickType.RIGHT, event -> { player.closeInventory(); openShop($, player);})
                ));

        PaginatedGUI paginatedGUI = new PaginatedGUIBuilder(
                "Lojas",
                 "xxxxxxxxx" +
                        "x#######x" +
                        "x#######x" +
                        "xxxxxxxxx" +
                        "<xxxxxxx>"
        )

                .setHotbarButton((byte) 3,
                new ItemButton(
                        Material.HOPPER,
                        0,
                        1,
                        "§aFiltrar lojas", Arrays.asList(FilterType.values()).stream().map($ -> $.getType().equals(type.getType()) ? "§7 - §a" + $.getType() : "§7 - §f" + $.getType()).collect(Collectors.toList()).toArray(new String[0])).setDefaultAction(event -> openShops(player, type.next())
                ))

                .setHotbarButton((byte) 4,
                new ItemButton(
                        Material.ARROW,
                        0,
                        1,
                        "§aFechar")
                        .addAction(ClickType.LEFT, event -> player.closeInventory()))

                .setNextPageItem(Material.ARROW, 1, "§aPróxima página")

                .setPreviousPageItem(Material.ARROW, 1, "§aPágina anterior")

                .setContent(items)

                .setDefaultAllCancell(true)

                .build();

        paginatedGUI.show(player);
    }

    private void openShop(Shop shop, Player player) {

        InventoryGUI gui = new InventoryGUI("Loja de " + shop.getName(), InventorySize.FIVE_ROWS);

        gui.setButton(36, new ItemButton(
                Material.ARROW,
                0,
                1,
                "§aVoltar")
                .setDefaultAction(event -> { player.closeInventory(); openShops(player, FilterType.VISITS); })
        );

        gui.setDefaultAllCancell(true);

        gui.show(player);

    }

    private void teleport(Player player, Shop shop) {

        player.closeInventory();
        player.sendMessage("§aVocê será teleportado em 3 segundos!");

        new BukkitRunnable() {

            @Override
            public void run() {

                player.teleport(shop.getLocation());
                player.sendMessage("§aVocê foi teleportado para a loja de §f" + shop.getName() + "§a.");

                if (!shop.getName().equals(player.getName())) shop.setVisits(shop.getVisits() + 1);

            }
        }.runTaskLater(plugin, 20L * 3);
    }

}
