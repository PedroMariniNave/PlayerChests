package com.zpedroo.playerchests.commands;

import com.zpedroo.playerchests.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (args.length > 0 && sender.hasPermission("chests.admin")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                Menus.getInstance().openOtherChestsMenu(player, target);
                return true;
            }
        }

        Menus.getInstance().openMainMenu(player);
        return false;
    }
}