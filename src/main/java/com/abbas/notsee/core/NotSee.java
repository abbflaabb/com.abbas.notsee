package com.abbas.notsee.core;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.commands.MeHelpTestCommand;
import com.abbas.notsee.events.BlockBreak;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class NotSee extends JavaPlugin {
    private Logger logger = getLogger();

    @Override
    public void onEnable() {
        logger.info("Plugin enabled");

        // Additional startup tasks can be performed here
        initializeCommands();
        registerEvents();

        saveDefaultConfig();

        // Setup our custom config handler
        Config.setup();

        // Optional: If you want to create a default config with predefined values
        if (!Config.getConfig().isConfigurationSection("commands")) {
            getLogger().info("Creating default help command configuration...");
            // The MeHelpCommand constructor will create default values when initialized
        }
    }

    @Override
    public void onDisable() {
        logger.info("Plugin disabled");
    }

    private void initializeCommands() {
        // Register your commands here
        getCommand("help").setExecutor(new MeHelpTestCommand(this));


    }

    private void registerEvents() {
        // Register your events here
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new BlockBreak(this),this);
    }


}
