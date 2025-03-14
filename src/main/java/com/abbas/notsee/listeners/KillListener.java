package com.abbas.notsee.listeners;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class KillListener implements Listener {

    public KillListener() {
        setupConfig();
    }
    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("kill-message")) {
            config.set("kill-message.enabled", true);
            config.set("kill-messages.permission", "notsee.kills");
            config.set("kill-messages.messages.killer", "&aYou killed &e%victim%&a! &7(+%points% points)");
            config.set("kill-messages.messages.victim", "&cYou were killed by &e%killer%&c! &7(-%points% points)");
            config.set("kill-messages.points", 10);
            config.set("kill-messages.sounds.kill", "PLAYER_LEVELUP");
            config.set("kill-messages.sounds.death", "PLAYER_DEATH");
            Config.save();
        }
    }
    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            FileConfiguration config = Config.getConfig();
            if (config.getBoolean("kill-message.enabled")) {
                int points = config.getInt("kill-messages.points");

                // Send messages
                String killerMessage = config.getString("kill-messages.messages.killer")
                        .replace("%victim%", victim.getName())
                        .replace("%points%", String.valueOf(points));
                killer.sendMessage(ColorUtils.colorize(killerMessage));

                String victimMessage = config.getString("kill-messages.messages.victim")
                        .replace("%killer%", killer.getName())
                        .replace("%points%", String.valueOf(points));
                victim.sendMessage(ColorUtils.colorize(victimMessage));

                // Play sounds
                killer.playSound(killer.getLocation(), Sound.valueOf(config.getString("kill-messages.sounds.kill")), 1.0f, 1.0f);
                victim.playSound(victim.getLocation(), Sound.valueOf(config.getString("kill-messages.sounds.death")), 1.0f, 1.0f);
            }
        }
    }

}
