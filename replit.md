# TaczRoyale - Minecraft Paper Plugin

## Overview
TaczRoyale is a battle royale style Minecraft plugin for Paper servers (1.20.x). It adds a shrinking border system similar to popular battle royale games.

## Project Structure
- `src/main/java/me/gapple/taczRoyale/` - Java source files
  - `TaczRoyale.java` - Main plugin class
  - `borderManager.java` - Manages the shrinking border
  - `lootManager.java` - Handles loot distribution
  - `teamManager.java` - Team management
  - `commandManager/` - Command handlers
- `src/main/resources/plugin.yml` - Plugin configuration
- `build.gradle` - Gradle build configuration
- `build/libs/` - Compiled JAR output

## Build System
- **Language**: Java 17+
- **Build Tool**: Gradle 8.8
- **Target Platform**: Paper MC 1.20.1

## Building
Run the "Build Plugin" workflow or execute:
```bash
./gradlew build --no-daemon
```

The compiled plugin JAR will be at: `build/libs/taczRoyale-1.0-SNAPSHOT.jar`

## Commands
- `/royaleborder <start|debug|check>` - Control the battle royale border (requires OP)

## Installation
1. Build the plugin
2. Copy `build/libs/taczRoyale-1.0-SNAPSHOT.jar` to your Paper server's `plugins/` folder
3. Restart the server

## Recent Changes
- December 10, 2025: Initial Replit environment setup
