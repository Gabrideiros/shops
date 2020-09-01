package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.enums.ConfirmType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class DelSubCommand extends SubCommand {

    private final Map<String, ConfirmType> confirm;

    public DelSubCommand(Main plugin, CommandBase command, Map<String, ConfirmType> confirm) {
        super(plugin, command, "deletar", null, null, "loja.criar");

        this.confirm = confirm;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        confirm.put(player.getName(), ConfirmType.DELETE);

        player.sendMessage("§cDigite '/setloja confirmar' caso realmente deseja deletar sua loja!");

        String name = player.getName();

        new BukkitRunnable() {
            @Override
            public void run() {

                confirm.remove(name);
            }
        }.runTaskLater(getPlugin(), 20 * 30);
    }
}
