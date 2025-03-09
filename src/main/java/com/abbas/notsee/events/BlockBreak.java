package com.abbas.notsee.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBreak implements Listener {
    private final JavaPlugin plugin;

    public BlockBreak(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onS(BlockBreakEvent event) {
        event.setCancelled(true);
        Player p = event.getPlayer();
        p.sendMessage(ChatColor.RED + "You cannot break blocks here!" + ChatColor.GRAY + " | "
                +  ChatColor.GOLD + plugin.getName() + " " + ChatColor.GRAY + "v" + plugin.getDescription().getVersion() + " by Cala");
        if (p.hasPermission("notsee.breakblocks")) {
            event.setCancelled(false);}

    }
}
