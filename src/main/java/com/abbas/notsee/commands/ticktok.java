package com.abbas.notsee.commands;

import com.abbas.notsee.core.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ticktok implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ticktok")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ColorUtils.translateColorCodes("&3&l &9&lTicktok "));
                player.sendMessage(ColorUtils.translateColorCodes("&3&l Follow me on ticktok"));
                player.sendMessage(ColorUtils.translateColorCodes("&3&l www.ticktok.com/Cala"));
            }
        }
        return false;
    }
}
