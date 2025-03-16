package com.abbas.notsee.core;

import com.abbas.notsee.TestConfig.Config;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossListener implements Listener {
    private final JavaPlugin plugin;
    private final BukkitAudiences adventure;
    private final Map<UUID, BossBar> playerBossBars;
    private float progress = 1.0f;
    private boolean increasing = false;

    public BossListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.adventure = BukkitAudiences.create(plugin);
        this.playerBossBars = new HashMap<>();
        setupConfig();
        startProgressUpdater();
    }

    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("bossbar")) {
            config.set("bossbar.enabled", true);
            config.set("bossbar.messages.welcome", "&6Welcome to the Server!");
            config.set("bossbar.messages.time", "&bServer Time: %time%");
            config.set("bossbar.messages.players", "&aPlayers Online: %count%");
            config.set("bossbar.colors.welcome", "PURPLE");
            config.set("bossbar.colors.time", "BLUE");
            config.set("bossbar.colors.players", "GREEN");
            config.set("bossbar.update-interval", 100);
            config.set("bossbar.progress-speed", 0.01);
            Config.save();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Config.getConfig();

        if (!config.getBoolean("bossbar.enabled", true)) {
            return;
        }

        String welcomeMessage = ColorUtils.colorize(config.getString("bossbar.messages.welcome", "Welcome to the Server!"));
        BossBar bossBar = BossBar.bossBar(
                Component.text(welcomeMessage, NamedTextColor.GOLD, TextDecoration.BOLD),
                1.0f,
                BossBar.Color.valueOf(config.getString("bossbar.colors.welcome", "PURPLE")),
                BossBar.Overlay.PROGRESS
        );

        playerBossBars.put(player.getUniqueId(), bossBar);
        Audience audience = adventure.player(player);
        audience.showBossBar(bossBar);

        long updateInterval = config.getLong("bossbar.update-interval", 100L);
        new BukkitRunnable() {
            private int phase = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }

                switch (phase) {
                    case 0:
                        String timeMessage = ColorUtils.colorize(config.getString("bossbar.messages.time")
                                .replace("%time%", String.valueOf(player.getWorld().getTime())));
                        updateBossBar(player, timeMessage, "bossbar.colors.time");
                        break;
                    case 1:
                        String playerMessage = ColorUtils.colorize(config.getString("bossbar.messages.players")
                                .replace("%count%", String.valueOf(plugin.getServer().getOnlinePlayers().size())));
                        updateBossBar(player, playerMessage, "bossbar.colors.players");
                        break;
                    case 2:
                        String personalMessage = ColorUtils.colorize(config.getString("bossbar.messages.welcome")
                                .replace("%player%", player.getName()));
                        updateBossBar(player, personalMessage, "bossbar.colors.welcome");
                        break;
                }

                phase = (phase + 1) % 3;
            }
        }.runTaskTimer(plugin, updateInterval, updateInterval);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BossBar bossBar = playerBossBars.remove(player.getUniqueId());
        if (bossBar != null) {
            Audience audience = adventure.player(player);
            audience.hideBossBar(bossBar);
        }
    }

    private void startProgressUpdater() {
        float speed = (float) Config.getConfig().getDouble("bossbar.progress-speed", 0.01);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (increasing) {
                    progress += speed;
                    if (progress >= 1.0f) {
                        progress = 1.0f;
                        increasing = false;
                    }
                } else {
                    progress -= speed;
                    if (progress <= 0.0f) {
                        progress = 0.0f;
                        increasing = true;
                    }
                }

                for (Map.Entry<UUID, BossBar> entry : playerBossBars.entrySet()) {
                    entry.getValue().progress(progress);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void updateBossBar(Player player, String message, String colorKey) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar != null) {
            FileConfiguration config = Config.getConfig();
            BossBar.Color color = BossBar.Color.valueOf(
                    config.getString(colorKey, "PURPLE").toUpperCase()
            );
            bossBar.name(Component.text(message));
            bossBar.color(color);
        }
    }

    public void cleanup() {
        if (adventure != null) {
            adventure.close();
        }
        playerBossBars.clear();
    }
}
