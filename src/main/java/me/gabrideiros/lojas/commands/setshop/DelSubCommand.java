package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.util.CommandBase;
import me.gabrideiros.lojas.commands.util.SubCommand;
import me.gabrideiros.lojas.enums.ConfirmType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if (!player.hasPermission("loja.criar")) return;

        confirm.put(player.getName(), ConfirmType.DELETE);

        player.sendMessage("§cCaso realmente deseja deletar sua loja, digite /setloja confirmar!");

    }
}
