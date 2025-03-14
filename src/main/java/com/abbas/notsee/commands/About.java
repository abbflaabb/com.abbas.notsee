package com.abbas.notsee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class About implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("This is the NotSee plugin. Version 1.0. Developed by Abbas.");
            return true;
        }
        sender.sendMessage("This command can only be run by a player.");
        return false;
    }
}
