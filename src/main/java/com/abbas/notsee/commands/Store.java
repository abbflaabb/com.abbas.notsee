package com.abbas.notsee.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Store implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("store")) {
            if (Sender instanceof Player) {
                Player player = (Player) Sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l &9&lStore "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l Visit our store"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l https://discord.gg/qcjXaXTN6T"));
            }
            return true;
        }
        return false;
    }
}
