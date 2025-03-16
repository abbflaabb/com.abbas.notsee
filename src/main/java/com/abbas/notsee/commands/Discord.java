package com.abbas.notsee.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("discord")) {
            if (Sender instanceof Player) {
                Player player = (Player) Sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l &9&lDiscord "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l Join our discord server"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l www.discord.com/Cala"));
            }
            return true;
        }

        return false;
    }
}
