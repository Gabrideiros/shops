package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.controller.ShopController;
import me.gabrideiros.lojas.database.SQLManager;
import me.gabrideiros.lojas.enums.ConfirmType;
import me.gabrideiros.lojas.gui.ShopInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class SetCommand extends CommandBase {

    public SetCommand(Main plugin, ShopController controller, SQLManager sqlManager, ShopInventory inventory) {
        super(plugin, "setloja", null);

        Map<String, ConfirmType> confirm = new HashMap<>();

        register(new CreateSubCommand(plugin, this, confirm));
        register(new DelSubCommand(plugin, this, confirm));
        register(new TimeSubCommand(plugin, this , controller));
        register(new RenewSubCommand(plugin, this, controller));
        register(new HomeSubCommand(plugin, this, controller));
        register(new ConfirmSubCommand(plugin, this, controller, sqlManager, confirm));
        register(new InfoSubCommand(plugin, this, controller, inventory));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        String key = args[0];

        if (!getSubCommandMap().containsKey(key)) return true;

        SubCommand subCommand = getSubCommandMap().get(key);

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) return true;

        subCommand.execute(sender, args);

        return true;
    }
}
