package com.abbas.notsee.events;

import com.abbas.notsee.TestConfig.Config;
import com.abbas.notsee.core.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropItem implements Listener {

    public DropItem() {
        setupConfig();
    }

    private void setupConfig() {
        FileConfiguration config = Config.getConfig();
        if (!config.isConfigurationSection("drop-items")) {
            config.set("drop-items.permission", "notsee.dropitems");
            config.set("drop-items.messages.denied", "&cYou don't have permission to drop items!");
            config.set("drop-items.messages.allowed", "&aYou dropped &e%amount%x &6%item%");
            config.set("drop-items.sounds.success", "ITEM_PICKUP");
            config.set("drop-items.sounds.denied", "VILLAGER_NO");
            Config.save();
        }
    }

    @EventHandler
    public void ODropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        FileConfiguration config = Config.getConfig();

        String permission = config.getString("drop-items.permission", "notsee.dropitems");
        String denyMessage = config.getString("drop-items.messages.denied");
        String allowMessage = config.getString("drop-items.messages.allowed");

        event.setCancelled(true);

        if (p.hasPermission(permission)) {
            event.setCancelled(false);
            p.playSound(p.getLocation(), Sound.valueOf(config.getString("drop-items.sounds.success")), 1.0F, 1.0F);

            String message = allowMessage
                    .replace("%amount%", String.valueOf(item.getAmount()))
                    .replace("%item%", item.getType().toString().toLowerCase());
            p.sendMessage(ColorUtils.translateColorCodes(message));
        } else {
            p.playSound(p.getLocation(), Sound.valueOf(config.getString("drop-items.sounds.denied")), 1.0F, 1.0F);
            p.sendMessage(ColorUtils.translateColorCodes(denyMessage));
        }
    }
}
