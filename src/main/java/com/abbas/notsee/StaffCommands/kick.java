package com.abbas.notsee.StaffCommands;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Kick implements CommandExecutor {

    public Kick() {
        setupConfig();
    }

    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("kick")) {
            config.set("kick.default-reason", "&cYou have been kicked from the server!");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("kick")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (!player.hasPermission("NotSee.Kick")) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to use this command!"));
                    return true;
                }

                if (args.length < 1) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cUsage: /kick <player> [reason]"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cPlayer not found!"));
                    return true;
                }

                FileConfiguration config = Config.getConfig();
                String defaultReason = config.getString("kick.default-reason", "&cYou have been kicked from the server!");
                String reason = defaultReason;
                if (args.length > 1) {
                    reason = String.join(" ", args).substring(args[0].length()).trim();
                }

                target.kickPlayer(ColorUtils.translateColorCodes("&cYou are Kicked!\nReason: " + reason));
                player.sendMessage(ColorUtils.translateColorCodes("&aSuccessfully kicked " + target.getName() + " for: " + reason));

                // Log the kick event
                config.set("kick.log." + target.getName(), "Kicked by " + player.getName() + " for: " + reason);
                Config.save();

                return true;
            }
        }
        return false;
    }
}
