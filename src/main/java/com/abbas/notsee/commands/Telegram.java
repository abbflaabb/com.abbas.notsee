package com.abbas.notsee.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Telegram implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("telegram")) {
            if (Sender instanceof Player) {
                Player player = (Player) Sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l &9&lTelegram "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l Follow me on telegram"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l @NoSee1058"));
            }
            return true;
        }
        return false;
    }
}
