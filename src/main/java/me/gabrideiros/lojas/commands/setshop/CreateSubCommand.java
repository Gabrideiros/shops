package me.gabrideiros.lojas.commands.setshop;

import me.gabrideiros.lojas.Main;
import me.gabrideiros.lojas.commands.CommandBase;
import me.gabrideiros.lojas.commands.SubCommand;
import me.gabrideiros.lojas.enums.ConfirmType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CreateSubCommand extends SubCommand {

    private final Map<String, ConfirmType> confirm;

    public CreateSubCommand(Main plugin, CommandBase command, Map<String, ConfirmType> confirm) {

        super(plugin, command, "criar", null, null, "loja.criar");

        this.confirm = confirm;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        confirm.put(player.getName(), ConfirmType.CREATE);

        player.sendMessage("§aDigite '/setloja confirmar' caso realmente deseja criar uma loja!");

    }
}
