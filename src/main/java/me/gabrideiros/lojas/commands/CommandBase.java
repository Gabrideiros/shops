package me.gabrideiros.lojas.commands;

import me.gabrideiros.lojas.Main;
import org.bukkit.command.CommandExecutor;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandBase implements CommandExecutor {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    public CommandBase(Main plugin, String name, String permission, String... aliases) {

        plugin.getCommand(name).setExecutor(this);
    }

    protected void register(SubCommand subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
    }

    public Map<String, SubCommand> getSubCommandMap() {
        return subCommandMap;
    }

}