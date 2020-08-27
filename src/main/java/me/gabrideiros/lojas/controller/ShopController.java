package me.gabrideiros.lojas.controller;

import me.gabrideiros.lojas.model.Shop;
import me.gabrideiros.lojas.util.Cache;
import org.bukkit.entity.Player;

public class ShopController extends Cache<Shop> implements Controller<Shop> {

    @Override
    public Shop getByName(String key) {
        return get(shop -> shop.getName().equals(key));
    }

    @Override
    public Shop getByPlayer(Player player) {
        return get(shop -> shop.getName().equals(player.getName()));
    }
}
