package me.gabrideiros.lojas.commands.advertising;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.services.AdvertisingService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdvertisingCommand extends CommandBase {

    public AdvertisingCommand(Main plugin, ShopController shopController, AdvertisingController advertisingController, AdvertisingService advertisingService) {
        super(plugin, "propaganda", null, null);

        register(new ConfirmSubCommand(plugin, this, shopController, advertisingController, advertisingService));
        register(new EditSubCommand(plugin, this, advertisingController));
        register(new CreateSubCommand(plugin, this, advertisingController));
        register(new DeleteSubCommand(plugin, this, advertisingController, advertisingService));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!sender.hasPermission("loja.criar")) return true;

        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    "",
                    "§7 * §e/propaganda criar§7 - Para criar uma propaganda.",
                    "§7 * §e/propaganda editar§7 - Para editar sua propaganda.",
                    "§7 * §e/propaganda confirmar §7 - Para confirmar uma ação.",
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
