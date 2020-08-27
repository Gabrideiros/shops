package me.gabrideiros.lojas.controller;

import org.bukkit.entity.Player;

public interface Controller<T> {

    T getByName(String key);

    T getByPlayer(Player player);

}
