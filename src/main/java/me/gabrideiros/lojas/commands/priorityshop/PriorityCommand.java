package me.gabrideiros.lojas.commands.priorityshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.ShopController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class PriorityCommand extends CommandBase {

    public PriorityCommand(Main plugin, ShopController shopController) {
        super(plugin, "lojaprioritaria", null, null);

        register(new SetSubCommand(plugin, this, shopController));
        register(new DelSubCommand(plugin, this, shopController));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {


        if (!sender.hasPermission("loja.admin")) return true;

        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    "",
                    "§7 * §e/lojaprioritaria setar <nick> §7 - Para setar uma loja como prioritária.",
                    "§7 * §e/lojaprioritaria remover <nick> §7 - Para remover a prioridade de uma loja.",
                    ""
            });
            return true;
        }

        String key = args[0];

        if (!getSubCommandMap().containsKey(key)) return true;

        SubCommand subCommand = getSubCommandMap().get(key);

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) return true;

        subCommand.execute(sender, args);

        return true;

    }
}
