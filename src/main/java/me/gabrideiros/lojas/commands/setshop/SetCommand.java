package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.controllers.AdvertisingController;
import me.gabrideiros.lojas.controllers.ShopController;
import me.gabrideiros.lojas.enums.ConfirmType;
import me.gabrideiros.lojas.inventory.ShopInventory;
import me.gabrideiros.lojas.services.AdvertisingService;
import me.gabrideiros.lojas.services.ShopService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class SetCommand extends CommandBase {

    public SetCommand(Main plugin, ShopController controller, ShopService shopService, AdvertisingController advertisingController, AdvertisingService advertisingService, ShopInventory inventory) {
        super(plugin, "setloja", null);

        Map<String, ConfirmType> confirm = new HashMap<>();

        register(new CreateSubCommand(plugin, this, confirm));
        register(new DelSubCommand(plugin, this, confirm));
        register(new TimeSubCommand(plugin, this , controller));
        register(new RenewSubCommand(plugin, this, controller));
        register(new HomeSubCommand(plugin, this, controller));
        register(new ConfirmSubCommand(plugin, this, controller, advertisingController, shopService, advertisingService, confirm));
        register(new InfoSubCommand(plugin, this, controller, inventory));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!sender.hasPermission("loja.criar")) return true;

        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    "",
                    "§7 * §e/setloja criar §7 - Para criar uma loja.",
                    "§7 * §e/setloja deletar §7 - Para deletar sua loja.",
                    "§7 * §e/setloja home §7 - Para setar a localização de sua loja.",
                    "§7 * §e/setloja info §7 - Para setar os itens informativos de sua loja.",
                    "§7 * §e/setloja renovar §7 - Para renovar o tempo de sua loja.",
                    "§7 * §e/setloja tempo §7 - Para ver o tempo restante de sua loja.",
                    "§7 * §e/setloja confirmar §7 - Para confirmar uma ação.",
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
