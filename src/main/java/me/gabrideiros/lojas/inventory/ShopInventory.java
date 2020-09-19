package me.gabrideiros.lojas.inventory;

import me.gabrideiros.lojas.inventory.lib.PaginatedGUIBuilder;
import me.gabrideiros.lojas.inventory.lib.buttons.ItemButton;
import me.gabrideiros.lojas.inventory.lib.menus.InventoryGUI;
import me.gabrideiros.lojas.inventory.lib.menus.PaginatedGUI;
import me.gabrideiros.lojas.inventory.lib.utils.InventorySize;
import dev.dbassett.skullcreator.SkullCreator;
import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.listener.BaseListener;
import me.gabrideiros.lojas.models.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShopInventory {

    private final Main plugin;

    public void openShops(Player player, FilterType type) {

        List<Shop> shops = new ArrayList<>(plugin.getShopController().getElements());

        if (shops.size() < 1) {
            player.sendMessage("§cAinda não existe lojas criadas!");
            return;
        }

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
                                $.isPriority() ? "§b[Prioritária] §aLoja de §l" + $.getName() : "§aLoja de §l" + $.getName())
                                .setHead($.getName())
                                .setLore(
                                        "",
                                        "§7⇧ Total de Visitas: §f" + $.getVisits(),
                                        "§7✭ Nota média: " + $.getNotesString(),
                                        "§7⌛ Tempo: §f" + $.getTimeFormatted(),
                                        "",
                                        "§7Botão esquerdo: §fPara se teleportar",
                                        "§7Botão direito: §fPara ver informações")
                                .addAction(ClickType.LEFT, event -> teleport(player, $))
                                .addAction(ClickType.RIGHT, event -> { player.closeInventory(); openShop($, player); })
        ).collect(Collectors.toList());


        PaginatedGUI paginatedGUI = new PaginatedGUIBuilder(
                "Lojas",
                "xxxxxxxxx" +
                        "x#######x" +
                        "x#######x" +
                        "<#######>" +
                        "x#######x"
        )
                .setHotbarButton((byte) 3,
                new ItemButton(
                        Material.HOPPER,
                        0,
                        1,
                        "§eFiltrar lojas")
                        .setLore("§7Filtro selecionado:", "")
                        .addLore(Arrays.stream(FilterType.values()).map($ -> $.getType().equals(type.getType()) ? "§7 - §a" + $.getType() : "§7 - §f" + $.getType()).toArray(String[]::new))
                        .addLore("", "§7Clique §lAQUI §7para filtrar!")
                        .setDefaultAction(event -> openShops(player, type.next())
                ))

                .setHotbarButton((byte) 4,
                new ItemButton(
                        Material.ARROW,
                        0,
                        1,
                        "§4✘ §cFechar §4✘")
                        .addAction(ClickType.LEFT, event -> player.closeInventory()))

                .setNextPageItem(Material.ARROW, 1, "§cPróxima página →")

                .setPreviousPageItem(Material.ARROW, 1, "§c← Página anterior")

                .setButton(2, new ItemButton(SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6adcf96106613a33d3d2a464adb1b1a5c5e0cb11dce72926b599943e363df")).setName(""))

                .setButton(3, new ItemButton(SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/7bff8211e3d9d16b4ca2c20cd2a6f99cce8fe4d98ef5ca5516f51f25cf1c31")).setName(""))

                .setButton(4, new ItemButton(SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/9c342719a038268e36953aaaeb73eda82de681b23a47891a3ffe7fbe540a312")).setName(""))

                .setButton(5, new ItemButton(SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/42cd5a1b5288caaa21a6acd4c98ceafd4c1588c8b2026c88b70d3c154d39bab")).setName(""))

                .setButton(6, new ItemButton(SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/f38d2759569d515d2454d4a7891a94cc63ddfe72d03bfdf76f1d4277d590")).setName(""))

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

                .setButton(9, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(17, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(18, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(26, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(27, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(35, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(36, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))

                .setButton(44, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""))


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
                gui.setButton(i + 2, new ItemButton(Material.BARRIER, "§4✘ §cNada §4✘"));
            }
        }

        gui.setButton(13, new ItemButton(
                Material.SKULL_ITEM,
                3,
                1,
                "§a[§l$§a] Loja de §l" + shop.getName() + " §a[§l$§a]")
                .setHead(shop.getName())
        );

        gui.setButton(8, new ItemButton(
                Material.WOOL,
                5,
                1,
                "§a⇶ §lTeleportar-se",
                "§8➥§7 Clique para teleportar-se até a loja!")
                .setDefaultAction(event -> teleport(player, shop))
        );

        gui.setButton(26, new ItemButton(
                Material.MAP,
                "§6✭ Avaliar ✭",
                "§8➥§7 §7Clique para avaliar a loja!")
                .setDefaultAction(event ->  {
                        player.closeInventory();
                        player.performCommand("loja avaliar " + shop.getName());
                })
        );

        gui.setButton(21, new ItemButton(
                Material.COMPASS,
                0,
                1,
                "§c⇧ Visitações ⇧",
                "§7Esta loja possui: §f" + shop.getVisits() + "§f visitas")
        );

        gui.setButton(22, new ItemButton(
                Material.EMERALD,
                0,
                1,
                "§6✭ Nota ✭",
                "§7Nota média desta loja: §f" + shop.getNotesString())
        );

        gui.setButton(23, new ItemButton(
                Material.WATCH,
                0,
                1,
                "§a⌛ Tempo ⌛",
                "§7Esta loja esta aberta há: §f" + shop.getTimeFormatted())
        );

        gui.setButton(9, new ItemButton(
                Material.ARROW,
                0,
                1,
                "§c← Voltar")
                .setDefaultAction(event -> { player.closeInventory(); openShops(player, FilterType.VISITS); })
        );

        gui.setButton(0, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(1, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(7, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(10, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(11, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(12, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(14, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(15, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(16, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(17, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(18, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(19, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(20, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(24, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(25, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setDefaultAllCancell(true);

        gui.show(player);

    }

    public void openNote(Shop shop, Player player) {

        InventoryGUI gui = new InventoryGUI("Avaliar loja", InventorySize.ONE_ROW);

        gui.setButton(2,
                getNote(shop,
                        "http://textures.minecraft.net/texture/d55fc2c1bae8e08d3e426c17c455d2ff9342286dffa3c7c23f4bd365e0c3fe",
                        1,
                         player
                ));

        gui.setButton(3,
                getNote(shop,
                        "http://textures.minecraft.net/texture/dc61b04e12a879767b3b72d69627f29a83bdeb6220f5dc7bea2eb2529d5b097",
                        2,
                        player
                ));

        gui.setButton(4,
                getNote(shop,
                        "http://textures.minecraft.net/texture/6823f77558ca6060b6dc6a4d4b1d86c1a5bee7081677bbc336ccb92fbd3ee",
                        3,
                        player
                ));

        gui.setButton(5,
                getNote(shop,
                        "http://textures.minecraft.net/texture/91b9c4d6f7208b1424f8595bfc1b85ccaaee2c5b9b41e0f564d4e0aca959",
                        4,
                        player
                ));

        gui.setButton(6,
                getNote(shop,
                        "http://textures.minecraft.net/texture/bc1415973b42f8286f948e2140992b9a29d80965593b14553d644f4feafb7",
                        5,
                        player
                ));

        gui.setButton(0, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(1, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));

        gui.setButton(7, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));
        gui.setButton(8, new ItemButton(Material.STAINED_GLASS_PANE, 7, 1, ""));


        gui.setDefaultCancell(true);

        gui.show(player);
    }

    public void openInfo(Shop shop, Player player) {

        InventoryGUI gui = new InventoryGUI("Adicionar Item", InventorySize.ONE_ROW);

        for (int i = 0; i < 5; ++i) {

            try {
                gui.setButton(i + 2, getItems(shop, ItemButton.fromBase64(shop.getItems().get(i)), i));
            } catch (IndexOutOfBoundsException e) {
                gui.setButton(i + 2, getItems(shop, new ItemStack(Material.BARRIER), i).setName("§4✘ §cNada §4✘"));
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

        if (player.hasPermission("loja.bypass")) {
            player.teleport(shop.getLocation());
            player.sendMessage("§aVocê foi teleportado para a loja de §f" + shop.getName() + "§a.");

            if (!shop.getName().equals(player.getName())) shop.setVisits(shop.getVisits() + 1);
            return;
        }

        BaseListener.getTeleport().add(player.getName());
        player.closeInventory();
        player.sendMessage("§aVocê será teleportado em 3 segundos!");

        new BukkitRunnable() {

            @Override
            public void run() {

                if (BaseListener.getTeleport().contains(player.getName())) {
                    player.teleport(shop.getLocation());
                    player.sendMessage("§aVocê foi teleportado para a loja de §f" + shop.getName() + "§a.");

                    BaseListener.getTeleport().remove(player.getName());

                    if (!shop.getName().equals(player.getName())) shop.setVisits(shop.getVisits() + 1);
                }
            }
        }.runTaskLater(plugin, 20L * 3);
    }

    public ItemButton getNote(Shop shop, String skull, int note, Player player) {
        return new ItemButton(
                SkullCreator.itemFromUrl(skull))
                .setName("§a" + note + " estrela(s)")
                .setLore("§7Clique para avaliar esta loja!")
                .setDefaultAction(event -> {

                    player.closeInventory();

                    shop.getNote().put(player.getName(), note);
                    player.sendMessage("§aVocê avaliou a loja de §f" + shop.getName() + "§a como " + note + "§a estrela(s)!");
                });
    }

    public ItemButton getItems(Shop shop, ItemStack item, int position) {

        return new ItemButton(item)
                .addLore(
                        "",
                        "§7Botão esquerdo: §fPara adicionar um item",
                        "§7Botão direito: §fPara remover um item",
                        "§7Botão scroll: §fPara renomear um item"
                        )
                .addAction(ClickType.LEFT, event -> {

                    ItemStack cursor = event.getCursor();
                    if (cursor == null || cursor.getType() == Material.AIR) return;
                    event.setCurrentItem(new ItemButton(cursor.clone())
                            .addLore(
                                    "",
                                    "§7Botão esquerdo: §fPara adicionar um item",
                                    "§7Botão direito: §fPara remover um item",
                                    "§7Botão scroll: §fPara renomear um item"
                            )
                            .getItem());

                    shop.getItems().add(position, ItemButton.toBase64(cursor));
                })
                .addAction(ClickType.RIGHT, event -> {

                    event.setCurrentItem(new ItemButton(Material.BARRIER, "§4✘ §cNada §4✘")
                            .addLore(
                                    "",
                                    "§7Botão esquerdo: §fPara adicionar um item",
                                    "§7Botão direito: §fPara remover um item",
                                    "§7Botão scroll: §fPara renomear um item"
                            )
                            .getItem());
                    shop.getItems().remove(position);
                })
                .addAction(ClickType.MIDDLE, event ->  {

                    Player player = (Player) event.getWhoClicked();

                    player.closeInventory();

                    BaseListener.getNameMap().put(player.getName(), position);

                    player.sendMessage(new String[]{
                            "",
                            "§aDigite no chat o nome que deseja adicionar:",
                            "§7Caso deseja cancelar digite 'cancelar'!",
                            ""
                    });

                });
    }

}
