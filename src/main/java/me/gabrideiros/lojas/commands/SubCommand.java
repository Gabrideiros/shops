package me.gabrideiros.lojas.commands;

import me.gabrideiros.lojas.Main;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final Main plugin;
    private final CommandBase command;
    private final String name;
    private final String usage;
    private final String description;
    private final String permission;

    public SubCommand(Main plugin, CommandBase command, String name, String usage, String description, String permission) {
        this.plugin = plugin;
        this.command = command;
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public Main getPlugin() { return plugin; }

    public CommandBase getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    protected boolean parseInt(String v) {
        try {
            Integer.parseInt(v);
            return true;
        }catch (NumberFormatException n) {
            return false;
        }
    }

    protected boolean parseDouble(String v) {
        try {
            Double.parseDouble(v);
            return true;
        }catch (NumberFormatException n) {
            return false;
        }
    }
}