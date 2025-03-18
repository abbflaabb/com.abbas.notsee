package com.abbas.notsee.StaffCommands;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Ban implements CommandExecutor {

    public Ban() {
        setupConfig();
    }

    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("ban")) {
            config.set("ban.default-reason", "&cBanned by Admin!");
            Config.save();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ban")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("NotSee.Ban")) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to use this command!"));
                    return true;
                }

                if (args.length < 1) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cUsage: /ban <player> [reason]"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cPlayer not found!"));
                    return true;
                }

                FileConfiguration config = Config.getConfig();
                String defaultReason = config.getString("ban.default-reason", "&cBanned by Admin!");
                String reason = defaultReason;
                if (args.length > 1) {
                    reason = String.join(" ", args).substring(args[0].length()).trim();
                }

                Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, player.getName());
                target.kickPlayer(ColorUtils.translateColorCodes("&cYou are Banned!\nReason: " + reason));

                player.sendMessage(ColorUtils.translateColorCodes("&aSuccessfully banned " + target.getName() + " for: " + reason));
                return true;
            }
        }
        return false;
    }
}
