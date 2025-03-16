package com.abbas.notsee.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Build implements CommandExecutor {

    private final Set<Player> buildModePlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("build")) {
            if (sender.hasPermission("notsee.build")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    if (args.length == 0) {
                        toggleBuildMode(player);
                    } else if (args[0].equalsIgnoreCase("on")) {
                        setBuildMode(player, true);
                    } else if (args[0].equalsIgnoreCase("off")) {
                        setBuildMode(player, false);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /build [on|off]");
                    }
                    return true;
                } else {
                    sender.sendMessage("This command can only be run by a player.");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return false;
            }
        }
        return false;
    }

    private void toggleBuildMode(Player player) {
        if (buildModePlayers.contains(player)) {
            setBuildMode(player, false);
        } else {
            setBuildMode(player, true);
        }
    }

    private void setBuildMode(Player player, boolean enable) {
        if (enable) {
            buildModePlayers.add(player);
            player.sendMessage(ChatColor.GREEN + "Build mode enabled.");
        } else {
            buildModePlayers.remove(player);
            player.sendMessage(ChatColor.RED + "Build mode disabled.");
        }
    }
}
