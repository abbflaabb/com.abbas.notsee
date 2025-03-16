package com.abbas.notsee.dynamicweather;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DynamicWeatherSetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ColorUtils.translateColorCodes("&cOnly players can use this command!"));
            return true;
        }

        if (args.length != 1) {
            commandSender.sendMessage(ColorUtils.translateColorCodes("&cUsage: /dynamicweather <type>"));
            return false;
        }
        String weatherType = args[0].toUpperCase();
        if (!weatherType.equals("CLEAR") && !weatherType.equals("RAIN") && !weatherType.equals("THUNDER") && !weatherType.equals("SNOW") && !weatherType.equals("FOG")) {
            commandSender.sendMessage("Invalid weather type. Valid types are: CLEAR, RAIN, THUNDER, SNOW, FOG.");
            return false;
        }
        FileConfiguration config = Config.getConfig();
        config.set("dynamicweather.weather.type", weatherType);
        Config.save();

        commandSender.sendMessage("Weather type set to " + weatherType + ".");
        return true;
    }
}
