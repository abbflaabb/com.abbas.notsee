package com.abbas.notsee.dynamicweather;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class dynamicweather implements Listener {
    private final JavaPlugin plugin;

    public dynamicweather(JavaPlugin plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("dynamicweather")) {
            config.set("dynamicweather.enabled", true);
            config.set("dynamicweather.messages.welcome", "&6Welcome to the dynamic weather server!");
            config.set("dynamicweather.weather.type", "CLEAR");
            config.set("dynamicweather.weather.durations.clear", 6000);
            config.set("dynamicweather.weather.durations.rain", 6000);
            config.set("dynamicweather.weather.durations.thunder", 6000);
            config.set("dynamicweather.weather.durations.snow", 6000);
            config.set("dynamicweather.weather.durations.fog", 6000);
            config.set("dynamicweather.weather.durations.hail", 6000);
            config.set("dynamicweather.weather.durations.wind", 6000);
            Config.save();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        if (!config.getBoolean("dynamicweather.enabled", true)) {
            return;
        }

        String welcomeMessage = ColorUtils.colorize(config.getString("dynamicweather.messages.welcome", "Welcome to the dynamic weather server!"));
        player.sendMessage(welcomeMessage);

        String weatherType = config.getString("dynamicweather.weather.type", "CLEAR");
        int duration = config.getInt("dynamicweather.weather.durations." + weatherType.toLowerCase(), 6000);

        Weather weather;
        switch (weatherType.toUpperCase()) {
            case "RAIN":
                weather = new RainWeather();
                break;
            case "THUNDER":
                weather = new ThunderWeather();
                break;
            case "SNOW":
                weather = new SnowWeather();
                break;
            case "FOG":
                weather = new FogWeather();
                break;
            default:
                weather = new ClearWeather();
                break;
        }
        weather.apply(player.getWorld(), duration);
    }
}
