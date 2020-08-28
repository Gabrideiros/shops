package me.gabrideiros.lojas.commands;

import lombok.AllArgsConstructor;
import me.gabrideiros.lojas.enums.FilterType;
import me.gabrideiros.lojas.gui.ShopsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ShopsCommand implements CommandExecutor {

    private final ShopsInventory inventory;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        inventory.openShops(player, FilterType.VISITS);

        return false;
    }
}
