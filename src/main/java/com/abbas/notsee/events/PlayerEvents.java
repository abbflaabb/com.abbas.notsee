package com.abbas.notsee.events;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {
    private final JavaPlugin plugin;
    private final Map<UUID, Long> lastLoginTimes = new HashMap<>();
    private final Map<String, MessageInfo> messages = new HashMap<>();
    private Sound joinSound;
    private Sound quitSound;
    private float volume;
    private float pitch;

    public PlayerEvents(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfiguration();
    }

    private void loadConfiguration() {
        // Load messages with permissions
        addMessage("welcome", "&a&lWelcome, &e{player} &a&lto the server!", "notsee.messages.welcome");
        addMessage("welcome-back", "&a&lWelcome back, &e{player}&a&l!", "notsee.messages.welcome-back");
        addMessage("goodbye", "&c&lGoodbye, &e{player}&c&l!", "notsee.messages.goodbye");
        addMessage("silent-quit", "&7{player} has left silently.", "notsee.messages.silentquit");
        addMessage("admin-join", "&4&lAdmin &e{player} &4&lhas joined.", "notsee.messages.admin");
        addMessage("vip-join", "&6&lVIP &e{player} &6&lhas joined.", "notsee.messages.vip");
        addMessage("staff-notify", "&7[Staff] {player} has joined the server.", "notsee.messages.staff");

        // Load sounds
        ConfigurationSection soundSection = plugin.getConfig().getConfigurationSection("sounds");
        if (soundSection == null) {
            soundSection = plugin.getConfig().createSection("sounds");
        }

        try {
            joinSound = Sound.valueOf(soundSection.getString("join", "ENDERMAN_TELEPORT"));
            quitSound = Sound.valueOf(soundSection.getString("quit", "ENDERMAN_TELEPORT"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config, using defaults.");
            joinSound = Sound.ENDERMAN_TELEPORT;
            quitSound = Sound.ENDERMAN_TELEPORT;
        }

        volume = (float) soundSection.getDouble("volume", 1.0);
        pitch = (float) soundSection.getDouble("pitch", 1.0);

        plugin.saveConfig();
    }

    private void addMessage(String key, String defaultText, String permission) {
        ConfigurationSection msgSection = plugin.getConfig().getConfigurationSection("messages");
        if (msgSection == null) {
            msgSection = plugin.getConfig().createSection("messages");
        }

        ConfigurationSection section = msgSection.getConfigurationSection(key);
        if (section == null) {
            section = msgSection.createSection(key);
        }

        section.addDefault("text", defaultText);
        section.addDefault("permission", permission);

        String text = ChatColor.translateAlternateColorCodes('&',
                section.getString("text", defaultText));
        String perm = section.getString("permission", permission);

        messages.put(key, new MessageInfo(text, perm));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Handle join messages based on permissions
        if (player.hasPermission(messages.get("admin-join").permission)) {
            event.setJoinMessage(formatMessage("admin-join", player));
            broadcastToPermitted("staff-notify", player);
        } else if (player.hasPermission(messages.get("vip-join").permission)) {
            event.setJoinMessage(formatMessage("vip-join", player));
        } else {
            if (player.hasPlayedBefore() && lastLoginTimes.containsKey(playerUUID)) {
                event.setJoinMessage(formatMessage("welcome-back", player));
            } else {
                event.setJoinMessage(formatMessage("welcome", player));
                sendFirstJoinMessage(player);
            }
        }

        playJoinSounds(player);
        lastLoginTimes.put(playerUUID, System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(messages.get("silent-quit").permission) || player.isFlying()) {
            broadcastToPermitted("silent-quit", player);
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage(formatMessage("goodbye", player));
        }

        playQuitSounds(player);
        saveLogoutLocation(player);
    }

    private void broadcastToPermitted(String messageKey, Player player) {
        MessageInfo info = messages.get(messageKey);
        String message = formatMessage(messageKey, player);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission(info.permission) && !online.equals(player)) {
                online.sendMessage(message);
            }
        }
    }

    private String formatMessage(String key, Player player) {
        return messages.get(key).text.replace("{player}", player.getName());
    }

    private void sendFirstJoinMessage(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ChatColor.GREEN + "Welcome to our server! Here's a quick guide:");
                player.sendMessage(ChatColor.YELLOW + "- Type /help for commands");
                player.sendMessage(ChatColor.YELLOW + "- Visit our website for more info");
                player.sendMessage(ChatColor.YELLOW + "- Have fun playing!");
            }
        }.runTaskLater(plugin, 60L);
    }

    private void playJoinSounds(Player player) {
        player.playSound(player.getLocation(), joinSound, volume, pitch);
        playSoundToNearbyPlayers(player, joinSound);
    }

    private void playQuitSounds(Player player) {
        player.playSound(player.getLocation(), quitSound, volume, pitch);
        playSoundToNearbyPlayers(player, quitSound);
    }

    private void playSoundToNearbyPlayers(Player source, Sound sound) {
        for (Player nearby : source.getWorld().getPlayers()) {
            if (nearby.getLocation().distance(source.getLocation()) <= 30 && !nearby.equals(source)) {
                nearby.playSound(nearby.getLocation(), sound, volume * 0.5f, pitch);
            }
        }
    }

    private void saveLogoutLocation(Player player) {
        if (plugin.getConfig().getBoolean("save-logout-location", true)) {
            Location loc = player.getLocation();
            String locationStr = String.format("%s,%f,%f,%f",
                    loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
            plugin.getConfig().set("player-data." + player.getUniqueId() + ".last-location", locationStr);
            plugin.saveConfig();
        }
    }

    private static class MessageInfo {
        final String text;
        final String permission;

        MessageInfo(String text, String permission) {
            this.text = text;
            this.permission = permission;
        }
    }

    public void reloadConfiguration() {
        plugin.reloadConfig();
        loadConfiguration();
    }
}
