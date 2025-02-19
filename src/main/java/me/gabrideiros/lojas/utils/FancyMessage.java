package me.gabrideiros.lojas.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FancyMessage {

    private TextComponent main;
    private List<TextComponent> components;

    public FancyMessage(String text) {
        main = new TextComponent(text);
        components = new ArrayList<>();
    }

    public FancyMessage text(String text) {
        components.add(new TextComponent(text));
        return this;
    }

    public FancyMessage hover(String text) {
        TextComponent comp = components.get(components.size() - 1);
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(text).create()));
        return this;
    }

    public FancyMessage command(String command) {
        TextComponent comp = components.get(components.size() - 1);
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    public FancyMessage url(String url) {
        TextComponent comp = components.get(components.size() - 1);
        comp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    public void send(Player player) {
        components.forEach(component -> main.addExtra(component));
        player.spigot().sendMessage(main);
    }

}