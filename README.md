# NotSee Plugin

A comprehensive Minecraft plugin that enhances player join/quit events and provides a customizable help system.

## Features

### Join/Quit System
- Custom join messages for different player ranks (Admin, VIP, Regular)
- Silent quit functionality for staff members
- Customizable sound effects on join/quit
- First-time join bonus messages
- Last location saving system
- Staff notifications for admin joins

### Help System
- Paginated help menu
- Customizable commands and descriptions
- Color-coded command display
- Beta tag support for new features
- Search functionality
- Reload command for live updates

## Installation

1. Download the latest release
2. Place the jar file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/NotSee/config.yml`

## Configuration

### Main Configuration
```
yaml
messages:
  welcome: "&a&lWelcome, &e{player} &a&lto the server!"
  welcome-back: "&a&lWelcome back, &e{player}&a&l!"
  goodbye: "&c&lGoodbye, &e{player}&c&l!"
  silent-quit: "&7{player} has left silently."
  admin-join: "&4&lAdmin &e{player} &4&lhas joined."
  vip-join: "&6&lVIP &e{player} &6&lhas joined."

sounds:
  join: "ENDERMAN_TELEPORT"
  quit: "ENDERMAN_TELEPORT"
  volume: 1.0
  pitch: 1.0

settings:
  show-silent-quit-to-admins: true
  save-logout-location: true
  helpTitle: "BedWars Help"
  pageSize: 10
  showBetaTag: true
```
