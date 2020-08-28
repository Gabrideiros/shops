package me.gabrideiros.lojas.gui;

import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import dev.arantes.inventorymenulib.menus.PaginatedGUI;
import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.model.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;

@AllArgsConstructor
public class ShopsInventory {

    private final ShopController controller;

    public void openShops(Player player, FilterType type) {

        ArrayList<ItemButton> items = new ArrayList<>();

        controller.getElements().forEach($ -> items.add(new ItemButton(Material.SKULL_ITEM, "§aLoja de " + $.getName()).setHead($.getName())));

        PaginatedGUI paginatedGUI = new PaginatedGUIBuilder(
                "Lojas",
                 "xxxxxxxxx" +
                        "x#######x" +
                        "x#######x" +
                        "xxxxxxxxx" +
                        "<xxxxxxx>"
        )
                .setHotbarButton((byte) 4, new ItemButton(Material.ARROW, "§cFechar").addAction(ClickType.LEFT, event -> player.closeInventory()))

                .setNextPageItem(Material.ARROW, 1, "§6Próxima página")

                .setPreviousPageItem(Material.ARROW, 1, "§6Página anterior")

                .setContent(items)

                .build();

        paginatedGUI.show(player);
    }

    public void openShop(Shop shop, Player player) {

    }

}
