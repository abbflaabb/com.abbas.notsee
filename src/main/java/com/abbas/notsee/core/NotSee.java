package com.abbas.notsee.core;

import com.abbas.notsee.StaffCommands.Kick;
import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.commands.*;
import com.abbas.notsee.events.DropItem;
import com.abbas.notsee.events.Test;
import com.abbas.notsee.listeners.JoinListener;
import com.abbas.notsee.listeners.KillListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class NotSee extends JavaPlugin {
    private Logger logger = getLogger();
    private JavaPlugin plugin;

    @Override
    public void onEnable() {
        logger.info("Plugin enabled");

        // Additional startup tasks can be performed here
        initializeCommands();
        registerEvents();
        // Initialize config

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
        }

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
        getCommand("Youtube").setExecutor(new YoutubeCommand());
        getCommand("ticktok").setExecutor(new ticktok());
        getCommand("about").setExecutor(new About()); // Register the About command
        getCommand("discord").setExecutor(new Discord()); // Register the Discord command
        getCommand("telegram").setExecutor(new Telegram()); // Register the telegram command
        getCommand("build").setExecutor(new Build()); // Register the Build command
        getCommand("Kick").setExecutor(new Kick()); // Register the Kick command
    }

    private void registerEvents() {
        // Register your events here
        PluginManager p = Bukkit.getPluginManager();

        p.registerEvents(new JoinListener(),this);
        p.registerEvents(new DropItem(), this);
        p.registerEvents(new KillListener(), this);
        p.registerEvents(new Test(),this);
    }


}
