# NotSee Plugin

A comprehensive Minecraft plugin for enhancing player join/quit events with custom messages, sounds and permissions.

![Version](https://img.shields.io/github/v/release/abbflaabb/NotSee?include_prereleases)
![Downloads](https://img.shields.io/github/downloads/abbflaabb/NotSee/total)
![License](https://img.shields.io/github/license/abbflaabb/NotSee)

## ✨ Features

### Core Features
- 🎮 Customizable join/quit messages for different ranks
- 🔊 Sound effects system with proximity playback
- 🎯 Permission-based message control
- 📍 Last location saving
- 👋 First join bonus messages
- 👥 Staff notifications system

### Message System
- Custom welcome messages
- Rank-specific join announcements (Admin, VIP)
- Silent quit functionality
- Staff-only notifications
- Configurable colors and formats

### Sound System  
- Configurable join/quit sounds
- Distance-based sound effects
- Adjustable volume and pitch
- Per-world sound settings

## 📥 Installation

1. Download latest `NotSee.jar` from [Releases](https://github.com/abbflaabb/NotSee/releases)
2. Place in your server's `plugins` folder
3. Restart server
4. Edit `plugins/NotSee/config.yml` as needed

## ⚙️ Configuration

### Main Configuration
```yaml
messages:
  welcome:
    text: "&a&lWelcome to &6&lOUR SERVER&a&l, &e{player}!"
    permission: "notsee.messages.welcome"
  vip-join:
    text: "&6&l>>> &e{player} &6&lhas joined! <<<"
    permission: "notsee.messages.vip"

sounds:
  join: "LEVEL_UP"
  quit: "ENDERMAN_TELEPORT"
  volume: 0.8
  pitch: 1.2
permissions:
  notsee.messages.*:        # All message permissions
  notsee.messages.welcome:  # See welcome messages
  notsee.messages.admin:    # Use admin join messages
  notsee.messages.vip:      # Use VIP join messages
  notsee.messages.staff:    # See staff notifications
  notsee.mehelp:           # Use help command
  notsee.mehelp.reload:    # Reload configuration

```
📋 Commands
Command
Description
Permission
Alias
/help
notsee.mehelp
/h
Shows support menu
notsee.mehelp
/s
🔑 Permission Groups
Default Players
See welcome messages
Use basic commands
Hear join/quit sounds
VIP Players
Custom VIP join message
All default permissions
Special welcome messages
Staff Members
Silent quit functionality
Admin join messages
Staff notifications
Configuration reload
