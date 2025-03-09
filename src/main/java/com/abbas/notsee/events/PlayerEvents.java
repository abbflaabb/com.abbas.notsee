package com.abbas.notsee.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
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
    private final FileConfiguration config;

    // Messages configuration with defaults
    private String welcomeMessage;
    private String welcomeBackMessage;
    private String goodbyeMessage;
    private String silentQuitMessage;
    private String adminJoinMessage;
    private String vipJoinMessage;

    // Sound configuration
    private Sound joinSound;
    private Sound quitSound;
    private float volume;
    private float pitch;

    public PlayerEvents(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadConfiguration();
    }

    private void loadConfiguration() {
        // Load messages from config or use defaults
        welcomeMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.welcome", "&a&lWelcome, &e{player} &a&lto the server!"));
        welcomeBackMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.welcome-back", "&a&lWelcome back, &e{player}&a&l!"));
        goodbyeMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.goodbye", "&c&lGoodbye, &e{player}&c&l!"));
        silentQuitMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.silent-quit", "&7{player} has left silently."));
        adminJoinMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.admin-join", "&4&lAdmin &e{player} &4&lhas joined."));
        vipJoinMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.vip-join", "&6&lVIP &e{player} &6&lhas joined."));

        // Load sound configuration
        try {
            joinSound = Sound.valueOf(config.getString("sounds.join", "ENDERMAN_TELEPORT"));
            quitSound = Sound.valueOf(config.getString("sounds.quit", "ENDERMAN_TELEPORT"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config, using defaults.");
            joinSound = Sound.ENDERMAN_TELEPORT;
            quitSound = Sound.ENDERMAN_TELEPORT;
        }

        volume = (float) config.getDouble("sounds.volume", 1.0);
        pitch = (float) config.getDouble("sounds.pitch", 1.0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Welcome the player with different messages based on circumstances
        if (player.hasPermission("notsee.admin")) {
            // Admin join message
            String message = adminJoinMessage.replace("{player}", player.getName());
            event.setJoinMessage(message);

            // Notify other admins silently
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.hasPermission("notsee.admin") && !online.equals(player)) {
                    online.sendMessage(ChatColor.GRAY + "[Staff] " + player.getName() + " has joined the server.");
                }
            }
        } else if (player.hasPermission("notsee.vip")) {
            // VIP join message
            String message = vipJoinMessage.replace("{player}", player.getName());
            event.setJoinMessage(message);
        } else {
            // Regular player messaging logic with first join detection
            if (player.hasPlayedBefore() && lastLoginTimes.containsKey(playerUUID)) {
                String message = welcomeBackMessage.replace("{player}", player.getName());
                event.setJoinMessage(message);
            } else {
                String message = welcomeMessage.replace("{player}", player.getName());
                event.setJoinMessage(message);

                // First time join bonus
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.GREEN + "Welcome to our server! Here's a quick guide:");
                        player.sendMessage(ChatColor.YELLOW + "- Type /help for commands");
                        player.sendMessage(ChatColor.YELLOW + "- Visit our website for more info");
                        player.sendMessage(ChatColor.YELLOW + "- Have fun playing!");
                    }
                }.runTaskLater(plugin, 60L); // 3 seconds delay
            }
        }

        // Play sound to the player
        player.playSound(player.getLocation(), joinSound, volume, pitch);

        // Play sound to nearby players
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby.getLocation().distance(player.getLocation()) <= 30 && !nearby.equals(player)) {
                nearby.playSound(nearby.getLocation(), joinSound, volume * 0.5f, pitch);
            }
        }

        // Update login time
        lastLoginTimes.put(playerUUID, System.currentTimeMillis());

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Set the quit message based on player status
        if (player.hasPermission("notsee.silentquit") || player.isFlying()) {
            // Silent quit for staff or flying players
            if (config.getBoolean("show-silent-quit-to-admins", true)) {
                String message = silentQuitMessage.replace("{player}", player.getName());
                for (Player admin : Bukkit.getOnlinePlayers()) {
                    if (admin.hasPermission("notsee.admin")) {
                        admin.sendMessage(message);
                    }
                }
            }
            event.setQuitMessage(null);
        } else {
            // Normal quit message
            String message = goodbyeMessage.replace("{player}", player.getName());
            event.setQuitMessage(message);
        }

        // Play sound to nearby players
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby.getLocation().distance(player.getLocation()) <= 30 && !nearby.equals(player)) {
                nearby.playSound(nearby.getLocation(), quitSound, volume, pitch);
            }
        }

        // Save player's last location for potential teleport-back feature
        if (config.getBoolean("save-logout-location", true)) {
            String locationStr = player.getLocation().getWorld().getName() + "," +
                    player.getLocation().getX() + "," +
                    player.getLocation().getY() + "," +
                    player.getLocation().getZ();
            plugin.getConfig().set("player-data." + player.getUniqueId() + ".last-location", locationStr);
            plugin.saveConfig();
        }

        // Clean up resources
        player.playSound(player.getLocation(), quitSound, volume, pitch);
    }

    // Method to get time since last login
    public String getTimeSinceLastLogin(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!lastLoginTimes.containsKey(playerUUID)) {
            return "First login";
        }

        long lastLogin = lastLoginTimes.get(playerUUID);
        long currentTime = System.currentTimeMillis();
        long diffInMillis = currentTime - lastLogin;

        // Convert to appropriate time unit
        long seconds = diffInMillis / 1000;
        if (seconds < 60) {
            return seconds + " seconds ago";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours ago";
        }

        long days = hours / 24;
        return days + " days ago";
    }

    // Additional utility methods
    public void reloadConfiguration() {
        plugin.reloadConfig();
        this.loadConfiguration();
    }

    public void broadcastAdminJoin(Player admin, boolean silent) {
        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("notsee.admin")) {
                    player.sendMessage(ChatColor.DARK_RED + "[Silent] " +
                            ChatColor.YELLOW + admin.getName() +
                            ChatColor.DARK_RED + " has joined silently.");
                }
            }
        } else {
            Bukkit.broadcastMessage(adminJoinMessage.replace("{player}", admin.getName()));
        }
    }
}