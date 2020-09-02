package me.gabrideiros.lojas.controllers;

import me.gabrideiros.lojas.models.Shop;
import me.gabrideiros.lojas.utils.Cache;
import org.bukkit.entity.Player;

public class ShopController extends Cache<Shop> implements Controller<Shop> {

    @Override
    public Shop getByName(String key) {
        return get(shop -> shop.getName().equals(key));
    }

    @Override
    public Shop getByPlayer(Player player) {
        return get(shop -> shop.getUuid().equals(player.getUniqueId()));
    }
}
