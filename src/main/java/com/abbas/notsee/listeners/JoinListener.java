package com.abbas.notsee.listeners;

import com.abbas.notsee.core.ColorUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(p.getUniqueId());

        String JoinPrefix = "&7[&a+&7] ";

        if (user != null) {
            String playerprefix = user.getCachedData().getMetaData().getPrefix();
            if (playerprefix == null) {
                playerprefix = "";
            }
            String joinText = ColorUtils.translateColorCodes(JoinPrefix + playerprefix + p.getName());
            joinText = PlaceholderAPI.setPlaceholders(p, joinText);
            e.setJoinMessage(joinText);
        }

        // Additional logic to handle teleport bow on player join
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(p.getUniqueId());

        String QuitPrefix = "&7[&a-&7] ";

        if (user != null) {
            String playerprefix = user.getCachedData().getMetaData().getPrefix();
            if (playerprefix == null) {
                playerprefix = "";
            }
            String quitText = ColorUtils.translateColorCodes(QuitPrefix + playerprefix + p.getName());
            quitText = PlaceholderAPI.setPlaceholders(p, quitText);
            e.setQuitMessage(quitText);
        }

        // Additional logic to handle teleport bow on player quit
    }
}
