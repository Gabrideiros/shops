package me.gabrideiros.lojas.controllers;

import me.gabrideiros.lojas.models.Advertising;
import me.gabrideiros.lojas.utils.Cache;
import org.bukkit.entity.Player;

public class AdvertisingController extends Cache<Advertising> implements Controller<Advertising> {

    @Override
    public Advertising getByName(String key) {
        return get($ -> $.getName().equals(key));
    }

    @Override
    public Advertising getByPlayer(Player player) {
        return get($ -> $.getUuid().equals(player.getUniqueId()));
    }
}
