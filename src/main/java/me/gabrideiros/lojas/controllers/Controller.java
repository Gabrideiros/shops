package me.gabrideiros.lojas.controllers;

import org.bukkit.entity.Player;

public interface Controller<T> {

    T getByName(String key);

    T getByPlayer(Player player);

}
