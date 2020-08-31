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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class ShopInventory {

    private final Main plugin;

    public void openShops(Player player, FilterType type) {

        ArrayList<ItemButton> items = new ArrayList<>();

        List<Shop> shops = new ArrayList<>(plugin.getController().getElements());

        switch(type) {
            case VISITS:
                shops.sort(Comparator.comparing(Shop::isPriority).thenComparing(Shop::getVisits));
                break;
            case BEST:
                shops.sort(Comparator.comparing(Shop::isPriority).thenComparing(Shop::getNotes));
                break;
            case RECENT:
                shops.sort(Comparator.comparing(Shop::isPriority).thenComparing(Shop::getTime));
                break;
        }

        shops.forEach($ ->
                items.add(
                        new ItemButton(Material.SKULL_ITEM,
                                3,
                                1,
                                $.isPriority() ? "§5[Prioritária] §aLoja de " + $.getName() : "§aLoja de " + $.getName()).setHead($.getName())
                                .setLore(
                                        "",
                                        "§7Visitas: §f" + $.getVisits(), "",
                                        "§7Clique esquerdo: §fPara se teleportar",
                                        "§7Clique direito: §fPara ver informações")
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
                        "§aFiltrar lojas")
                        .setLore("§7Filtro selecionado:", "")
                        .addLore(Arrays.stream(FilterType.values()).map($ -> $.getType().equals(type.getType()) ? "§7 - §a" + $.getType() : "§7 - §f" + $.getType()).toArray(String[]::new))
                        .addLore("", "§7Clique para filtrar!")
                        .setDefaultAction(event -> openShops(player, type.next())
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

    public void openShop(Shop shop, Player player) {

        InventoryGUI gui = new InventoryGUI("Loja de " + shop.getName(), InventorySize.THREE_ROWS);

        for (int i = 0; i < 5; ++i) {

            try {
                gui.setButton(i + 2, new ItemButton(ItemButton.fromBase64(shop.getItems().get(i))));
            } catch (IndexOutOfBoundsException e) {
                gui.setButton(i + 2, new ItemButton(Material.BARRIER, "§c=//="));
            }
        }

        gui.setButton(13, new ItemButton(
                Material.SKULL_ITEM,
                3,
                1,
                "§aLoja de " + shop.getName())
                .setHead(shop.getName())
        );

        gui.setButton(17, new ItemButton(
                Material.WOOL,
                5,
                1,
                "§aTeleportar-se",
                "§7Clique para teleportar-se até a loja!")
                .setDefaultAction(event -> teleport(player, shop))
        );

        gui.setButton(21, new ItemButton(
                Material.COMPASS,
                0,
                1,
                "§aVisitações",
                "§7Esta loja possui: §f" + shop.getVisits() + "§f visitas")
        );

        gui.setButton(22, new ItemButton(
                Material.EMERALD,
                0,
                1,
                "§aNota",
                "§7Nota média desta loja: §f" + shop.getNotesString())
        );

        gui.setButton(23, new ItemButton(
                Material.WATCH,
                0,
                1,
                "§aTempo",
                "§7Esta loja esta aberta há: §f" + shop.getTimeFormatted())
        );

        gui.setButton(9, new ItemButton(
                Material.ARROW,
                0,
                1,
                "§aVoltar")
                .setDefaultAction(event -> { player.closeInventory(); openShops(player, FilterType.VISITS); })
        );

        gui.setDefaultAllCancell(true);

        gui.show(player);

    }

    public void openInfo(Shop shop, Player player) {

        InventoryGUI gui = new InventoryGUI("Adicionar Item", InventorySize.ONE_ROW);

        for (int i = 0; i < 5; ++i) {

            try {
                gui.setButton(i + 2, getItems(shop, ItemButton.fromBase64(shop.getItems().get(i)), i));
            } catch (IndexOutOfBoundsException e) {
                gui.setButton(i + 2, getItems(shop, new ItemStack(Material.BARRIER), i).setName("§c=//="));
            }
        }

        gui.setDefaultCancell(true);

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

    public ItemButton getItems(Shop shop, ItemStack item, int position) {

        return new ItemButton(item)
                .addLore("", "§7Clique com um item para adicionar!")
                .setDefaultAction(event -> {
                    ItemStack cursor = event.getCursor();
                    if (cursor == null || cursor.getType() == Material.AIR) return;
                    event.setCurrentItem(new ItemButton(cursor.clone()).addLore("", "§7Clique com um item para adicionar!").getItem());

                    shop.getItems().add(position, ItemButton.toBase64(cursor));
                });
    }

}
