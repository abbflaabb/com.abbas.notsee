package com.abbas.notsee.commands;

import com.abbas.notsee.TestConfig.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeHelpTestCommand implements CommandExecutor {

    private final Map<String, CommandInfo> commands = new HashMap<>();
    private final JavaPlugin plugin;

    public MeHelpTestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        loadCommandsFromConfig();
    }

    private void loadCommandsFromConfig() {
        // Ensure config is setup
        Config.setup();

        // Check if commands section exists, if not create default commands
        if (!Config.getConfig().isConfigurationSection("commands")) {
            createDefaultCommands();
        } else {
            // Load commands from config
            ConfigurationSection commandsSection = Config.getConfig().getConfigurationSection("commands");
            for (String key : commandsSection.getKeys(false)) {
                ConfigurationSection cmdSection = commandsSection.getConfigurationSection(key);

                String command = cmdSection.getString("command", "/unknown");
                ChatColor color = getChatColorByName(cmdSection.getString("color", "WHITE"));
                String description = cmdSection.getString("description", "No description");
                boolean isBeta = cmdSection.getBoolean("beta", false);

                registerCommand(command, color, description, isBeta);
            }
        }
    }

    private void createDefaultCommands() {
        // Create default commands section in config
        ConfigurationSection commandsSection = Config.getConfig().createSection("commands");

        // Add default commands - using the commands from your existing implementation
        addDefaultCommand(commandsSection, "help", "/help", "RED", "Displays this message", false);
        addDefaultCommand(commandsSection, "bw fly", "/bw fly", "GOLD", "Toggles fly mode in BedWars", true);
        addDefaultCommand(commandsSection, "bw join", "/bw joinArena <arenaName>", "AQUA", "Joins a BedWars arena", false);
        addDefaultCommand(commandsSection, "bw leave", "/bw leave", "GREEN", "Leaves the BedWars arena", false);
        addDefaultCommand(commandsSection, "bw stats", "/bw stats", "DARK_PURPLE", "Displays your BedWars stats", false);
        addDefaultCommand(commandsSection, "share", "/share", "LIGHT_PURPLE", "Shares your item with your team", true);
        addDefaultCommand(commandsSection, "bw map", "/bw map", "ITALIC", "Displays the name of the map", true);
        addDefaultCommand(commandsSection, "sping", "/Sping", "DARK_BLUE", "Show your ping", true);
        addDefaultCommand(commandsSection, "sstats", "/SStats", "GOLD", "Show your stats", true);
        addDefaultCommand(commandsSection, "bwheal", "/bwheal", "DARK_RED", "Heals you in BedWars", true);
        addDefaultCommand(commandsSection, "bwdiscord", "/bwdiscord", "DARK_GREEN", "Displays the Discord link", true);
        addDefaultCommand(commandsSection, "bwmenu", "/bwmenu", "DARK_AQUA", "Opens the BedWars menu", true);

        // Create settings section for customization
        ConfigurationSection settingsSection = Config.getConfig().createSection("settings");
        settingsSection.set("helpTitle", "BedWars Help");
        settingsSection.set("pageSize", 10);
        settingsSection.set("showBetaTag", true);

        // Save the config
        Config.save();

        // Load the commands into memory
        for (String key : commandsSection.getKeys(false)) {
            ConfigurationSection cmdSection = commandsSection.getConfigurationSection(key);

            String command = cmdSection.getString("command");
            ChatColor color = getChatColorByName(cmdSection.getString("color"));
            String description = cmdSection.getString("description");
            boolean isBeta = cmdSection.getBoolean("beta");

            registerCommand(command, color, description, isBeta);
        }
    }

    private void addDefaultCommand(ConfigurationSection parent, String key, String command,
                                   String colorName, String description, boolean isBeta) {
        ConfigurationSection cmdSection = parent.createSection(key);
        cmdSection.set("command", command);
        cmdSection.set("color", colorName);
        cmdSection.set("description", description);
        cmdSection.set("beta", isBeta);
    }

    private ChatColor getChatColorByName(String name) {
        try {
            return ChatColor.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid color name in config: " + name);
            return ChatColor.WHITE; // Default color
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("help")) {
            if (!commandSender.hasPermission("notsee.mehelp")) {
                commandSender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            // Check for reload command
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("notsee.mehelp.reload")) {
                    commands.clear();
                    Config.reload();
                    loadCommandsFromConfig();
                    commandSender.sendMessage(ChatColor.GREEN + "Help commands reloaded from config!");
                    return true;
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You don't have permission to reload the help configuration.");
                    return true;
                }
            }

            if (isPlayer(commandSender)) {
                displayHelp((Player) commandSender, args);
            } else {
                commandSender.sendMessage("MeHelpCommand - Player-only command");
            }
            return true;
        }
        return false;
    }

    private void displayHelp(Player player, String[] args) {
        // Filter commands based on args if provided
        List<CommandInfo> filteredCommands = filterCommands(args);

        // Display filtered commands
        if (filteredCommands.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No commands match your filter.");
            return;
        }

        // Get settings from config
        int pageSize = Config.getConfig().getInt("settings.pageSize", 10);
        String helpTitle = Config.getConfig().getString("settings.helpTitle", "BedWars Help");
        boolean showBetaTag = Config.getConfig().getBoolean("settings.showBetaTag", true);

        // Add pagination if there are many commands
        int page = 1;
        int totalPages = (int) Math.ceil((double) filteredCommands.size() / pageSize);

        // Parse page number if provided
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;
            } catch (NumberFormatException ignored) {
                // Not a number, might be a filter instead
            }
        }

        // Display header with page info if multiple pages
        if (totalPages > 1) {
            player.sendMessage(ChatColor.YELLOW + "=== " + helpTitle + " (Page " + page + "/" + totalPages + ") ===");
        } else {
            player.sendMessage(ChatColor.YELLOW + "=== " + helpTitle + " ===");
        }

        // Calculate start and end index for pagination
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, filteredCommands.size());

        // Display commands
        for (int i = startIdx; i < endIdx; i++) {
            CommandInfo cmdInfo = filteredCommands.get(i);
            String betaTag = (cmdInfo.isBeta && showBetaTag) ? ChatColor.RED + " (BETA)" : "";
            player.sendMessage(cmdInfo.color + cmdInfo.command + ChatColor.GRAY + " - " + cmdInfo.description + betaTag);
        }
    }

    private List<CommandInfo> filterCommands(String[] args) {
        List<CommandInfo> filtered = new ArrayList<>();

        // If no args or first arg is a page number, return all commands
        if (args.length == 0 || isInteger(args[0])) {
            filtered.addAll(commands.values());
            return filtered;
        }

        // Skip if "reload" command
        if (args[0].equalsIgnoreCase("reload")) {
            return filtered;
        }

        // Filter by search term
        String searchTerm = args[0].toLowerCase();

        for (CommandInfo cmdInfo : commands.values()) {
            if (cmdInfo.command.toLowerCase().contains(searchTerm) ||
                    cmdInfo.description.toLowerCase().contains(searchTerm)) {
                filtered.add(cmdInfo);
            }
        }

        return filtered;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void registerCommand(String command, ChatColor color, String description, boolean isBeta) {
        commands.put(command, new CommandInfo(command, color, description, isBeta));
    }

    private boolean isPlayer(CommandSender commandSender) {
        return commandSender instanceof Player;
    }

    private static class CommandInfo {
        final String command;
        final ChatColor color;
        final String description;
        final boolean isBeta;

        CommandInfo(String command, ChatColor color, String description, boolean isBeta) {
            this.command = command;
            this.color = color;
            this.description = description;
            this.isBeta = isBeta;
        }
    }
}