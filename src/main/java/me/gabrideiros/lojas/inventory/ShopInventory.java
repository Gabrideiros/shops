package me.gabrideiros.lojas.inventory;

import dev.arantes.inventorymenulib.PaginatedGUIBuilder;
import dev.arantes.inventorymenulib.buttons.ItemButton;
import dev.arantes.inventorymenulib.menus.InventoryGUI;
import dev.arantes.inventorymenulib.menus.PaginatedGUI;
import dev.arantes.inventorymenulib.utils.InventorySize;
import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShopInventory {

    private final Main plugin;

    public void openShops(Player player, FilterType type) {

        List<Shop> shops = new ArrayList<>(plugin.getShopController().getElements());

        switch(type) {
            case VISITS:
                shops.sort((a1, a2) -> {
                    int priority = Boolean.compare(a2.isPriority(), a1.isPriority());
                    return priority == 0 ? Integer.compare(a2.getVisits(), a1.getVisits()) : priority;
                });
                break;
            case BEST:
                shops.sort((a1, a2) -> {
                    int priority = Boolean.compare(a2.isPriority(), a1.isPriority());
                    return priority == 0 ? Integer.compare(a2.getNotes(), a1.getNotes()) : priority;
                });
                break;
            case RECENT:
                shops.sort((a1, a2) -> {
                    int priority = Boolean.compare(a2.isPriority(), a1.isPriority());
                    return priority == 0 ? Long.compare(a2.getTime(), a1.getTime()) : priority;
                });
                break;
        }

        List<ItemButton> items = shops.stream().map($ ->
                        new ItemButton(Material.SKULL_ITEM,
                                3,
                                1,
                                $.isPriority() ? "§d[Prioritária] §aLoja de " + $.getName() : "§aLoja de " + $.getName())
                                .setHead($.getName())
                                .setLore(
                                        "",
                                        "§7Visitas: §f" + $.getVisits(), "",
                                        "§7Clique esquerdo: §fPara se teleportar",
                                        "§7Clique direito: §fPara ver informações")
                                .addAction(ClickType.LEFT, event -> teleport(player, $))
                                .addAction(ClickType.RIGHT, event -> { player.closeInventory(); openShop($, player); })
        ).collect(Collectors.toList());


        PaginatedGUI paginatedGUI = new PaginatedGUIBuilder(
                "Lojas",
                "xxxxxxxxx" +
                        "xxxxxxxxx" +
                        "x#######x" +
                        "<#######>" +
                        "xxxxxxxxx"
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

                .setButton(2, new ItemButton(ItemButton.getSkull("http://textures.minecraft.net/texture/6adcf96106613a33d3d2a464adb1b1a5c5e0cb11dce72926b599943e363df")).setName(""))

                .setButton(3, new ItemButton(ItemButton.getSkull("http://textures.minecraft.net/texture/7bff8211e3d9d16b4ca2c20cd2a6f99cce8fe4d98ef5ca5516f51f25cf1c31")).setName(""))

                .setButton(4, new ItemButton(ItemButton.getSkull("http://textures.minecraft.net/texture/9c342719a038268e36953aaaeb73eda82de681b23a47891a3ffe7fbe540a312")).setName(""))

                .setButton(5, new ItemButton(ItemButton.getSkull("http://textures.minecraft.net/texture/42cd5a1b5288caaa21a6acd4c98ceafd4c1588c8b2026c88b70d3c154d39bab")).setName(""))

                .setButton(6, new ItemButton(ItemButton.getSkull("http://textures.minecraft.net/texture/f38d2759569d515d2454d4a7891a94cc63ddfe72d03bfdf76f1d4277d590")).setName(""))

                .setButton(0, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(1, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(7, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(8, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(45, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(46, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(47, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(50, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(51, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(52, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(53, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

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

        gui.setButton(0, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(1, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(7, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(8, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(10, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(11, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(12, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(14, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(15, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(16, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(18, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(19, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(20, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(24, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(25, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(26, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

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

        gui.setButton(0, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(1, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(7, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(8, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

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
