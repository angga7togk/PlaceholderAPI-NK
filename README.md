# PlaceholderAPI for Nukkit-MOT

**PlaceholderAPI** is a simple library for handling placeholders in Nukkit-MOT plugins. It supports both static and dynamic placeholders with parameters.

## API

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.angga7togk</groupId>
    <artifactId>PlaceholderAPI-NK</artifactId>
    <version>Latest</version>
</dependency>
```

## Features

- **StaticPlaceholder**: Placeholders without parameters.
- **VisitorPlaceholder**: Placeholders with parameters.
- Supports parsing parameters using `String[]` for dynamic placeholders.


## How to Use

### 1. Register a Static Placeholder

You can register a static placeholder using the `StaticPlaceholder` class:

```java
PlaceholderAPI.register("server_name", new StaticPlaceholder(() -> "My Nukkit Server"));
```

### 2. Register a Dynamic Placeholder with Parameters

For placeholders with parameters, use the `VisitorPlaceholder` class:

```java
PlaceholderAPI.register("player_stat", new VisitorPlaceholder((player, params) -> {
    if (params.length == 0) {
        return "No stat provided!";
    }
    String stat = params[0];
    switch (stat) {
        case "kills":
            return "10 Kills"; // Replace with your logic
        case "deaths":
            return "5 Deaths"; // Replace with your logic
        default:
            return "Unknown stat: " + stat;
    }
}));
```

### 3. Replace Placeholders in Strings

Use the `translate` method to replace placeholders in strings:

```java
String message = "Welcome to %server_name%! Your stats: Kills: %player_stat:kills%, Deaths: %player_stat:deaths%.";
String processedMessage = PlaceholderAPI.translate(player, message);
player.sendMessage(processedMessage);
```

**Output:**

```
Welcome to My Nukkit Server! Your stats: Kills: 10 Kills, Deaths: 5 Deaths.
```

## Placeholder Syntax

1. **Static Placeholders**
   - Do not accept parameters.
   - Example: `%server_name%`.

2. **Dynamic Placeholders**
   - Accept parameters separated by colons.
   - Example: `%player_stat:kills%`, `%player_stat:deaths%`.

## Full Code Example

```java
@Override
public void onEnable() {
    // Register a static placeholder
    PlaceholderAPI.register("server_name", new StaticPlaceholder(() -> "My Nukkit Server"));

    // Register a dynamic placeholder
    PlaceholderAPI.register("player_stat", new VisitorPlaceholder((player, params) -> {
        if (params.length == 0) {
            return "No stat provided!";
        }
        String stat = params[0];
        switch (stat) {
            case "kills":
                return "10 Kills"; // Replace with your logic
            case "deaths":
                return "5 Deaths"; // Replace with your logic
            default:
                return "Unknown stat: " + stat;
        }
    }));

    getLogger().info("PlaceholderAPI initialized!");
}

public void sendExampleMessage(Player player) {
    String message = "Welcome to %server_name%! Your stats: Kills: %player_stat:kills%, Deaths: %player_stat:deaths%.";
    String processedMessage = PlaceholderAPI.translate(player, message);
    player.sendMessage(processedMessage);
}
```

## Placeholders

### Default
- **%player%**: The players username
- **%player_nametag%** - The players nametag
- **%player_displayname%**: The players displayname
- **%player_uuid%**: The players uuid
- **%player_ping%**: The players ping
- **%player_level%**: The name of the world the player is in
- **%player_health%**: The players health
- **%player_max_health%**: The players max health
- **%player_saturation%**: The players food saturation
- **%player_food%**: The players food level
- **%player_max_food%**: The players max food
- **%player_gamemode%**: The players gamemode (numerical)
- **%player_exp%**: The players experience
- **%player_exp_level%**: The players experience level
- **%player_platform%**: The players platform
- **%player_pos%**: The players position (also can do player_pos;x for just x etc.)
- **%player_item%** - The item the player is holding in his hand
- **%player_offhand%** - The item the player is holding in his offhand
- **%player_version%** - The players version
- **%player_protocol%** - The players protocol
- **%server_online%**: The amount of players on the server
- **%server_max_players%**: The player limit of the server
- **%server_motd%**: The servers message of the day
- **%server_tps%**: The servers tps
- **%server_tick%** - The servers current tick
- **%server_difficulty%** - The servers difficulty
- **%server_git%** - The servers git version
- **%server_version%** - The servers minecraft version
- **%server_protocol%** - The servers protocol version
- **%time%**: The servers date time (also can do `%time;HH:mm:ss;Asia/Jakarta%` or other format and timezone)

### LuckPerms

- **%luckperms_prefix%** – Player prefix from LuckPerms  
- **%luckperms_suffix%** – Player suffix from LuckPerms  
- **%luckperms_primary_group%** – Player primary group

### LLamaEconomy

- **%llamaeconomy_money%** – Player balance (raw number)
- **%llamaeconomy_money;id%** – Balance formatted as Indonesian Rupiah
- **%llamaeconomy_money;us%** – Balance formatted as USD
- **%llamaeconomy_money;jp%** – Balance formatted as JPY
- **%llamaeconomy_money;sg%** – Balance formatted as SGD
- **%llamaeconomy_money;short%** – Short format (1.2K, 3.4M)



## Installation

1. Add the `PlaceholderAPI` class to your Nukkit project.
2. Register placeholders using `register`.
3. Replace placeholders in strings using `translate`.

## License

This project is licensed under the [MIT License](LICENSE).
