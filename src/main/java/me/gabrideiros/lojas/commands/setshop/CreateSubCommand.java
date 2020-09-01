package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.enums.ConfirmType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class CreateSubCommand extends SubCommand {

    private final Map<String, ConfirmType> confirm;
    private final Economy economy;

    public CreateSubCommand(Main plugin, CommandBase command, Map<String, ConfirmType> confirm, Economy economy) {

        super(plugin, command, "criar", null, null, "loja.criar");

        this.confirm = confirm;
        this.economy = economy;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        if (economy.getBalance(player) < 250000) {
            player.sendMessage("§cVocê precisa de 250k para criar uma loja!");
            return;
        }

        confirm.put(player.getName(), ConfirmType.CREATE);

        player.sendMessage("§aDigite '/setloja confirmar' caso realmente deseja criar uma loja!");

        String name = player.getName();

        new BukkitRunnable() {
            @Override
            public void run() {

                confirm.remove(name);
            }
        }.runTaskLater(getPlugin(), 20 * 30);
    }
}
