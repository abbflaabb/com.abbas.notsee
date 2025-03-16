package com.abbas.notsee.events;

import com.abbas.notsee.core.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import static org.bukkit.Bukkit.broadcastMessage;

public class Test implements Listener {
    

    int playersInBed = 0;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        playersInBed = playersInBed + 1;
        Bukkit.broadcastMessage(player.getName() + " is in bed");
        broadcastMessage("There are " + playersInBed + " players in bed");
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f); // Play sound when entering bed
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        playersInBed = playersInBed - 1;
        broadcastMessage("There are " + playersInBed + " players in bed");
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f); // Play sound when leaving bed
    }
}
