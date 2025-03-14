package com.abbas.notsee.commands;

import com.abbas.notsee.core.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class YoutubeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("Youtube")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;


                player.sendMessage(ColorUtils.translateColorCodes("&3&l &9&lYoutube "));
                player.sendMessage(ColorUtils.translateColorCodes("&3&l SubScribe to my channel"));
                player.sendMessage(ColorUtils.translateColorCodes("&3&l www.youtube.com/Cala"));
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes("&c the Command Must Run by Player"));
            }

        }
        return false;
    }
}
