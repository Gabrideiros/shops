package me.gabrideiros.lojas.commands.shops;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.gui.ShopInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopsCommand extends CommandBase {

    private final ShopInventory inventory;

    public ShopsCommand(Main plugin, ShopInventory inventory) {
        super(plugin, "lojas", null, null);

        this.inventory = inventory;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        inventory.openShops(player, FilterType.VISITS);

        return false;
    }
}
