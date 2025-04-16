# PlaceholderAPI for Nukkit

**PlaceholderAPI** is a simple library for handling placeholders in Nukkit plugins. It supports both static and dynamic placeholders with parameters.

## Features

- **StaticPlaceholder**: Placeholders without parameters.
- **VisitorPlaceholder**: Placeholders with parameters.
- Supports parsing parameters using `String[]` for dynamic placeholders.

## How to Use

### 1. Register a Static Placeholder

You can register a static placeholder using the `StaticPlaceholder` class:

```java
PlaceholderAPI.registerPlaceholder("server_name", new StaticPlaceholder(() -> "My Nukkit Server"));
```

### 2. Register a Dynamic Placeholder with Parameters

For placeholders with parameters, use the `VisitorPlaceholder` class:

```java
PlaceholderAPI.registerPlaceholder("player_stat", new VisitorPlaceholder((player, params) -> {
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

Use the `processPlaceholders` method to replace placeholders in strings:

```java
String message = "Welcome to %server_name%! Your stats: Kills: %player_stat:kills%, Deaths: %player_stat:deaths%.";
String processedMessage = PlaceholderAPI.processPlaceholders(player, message);
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
    PlaceholderAPI.registerPlaceholder("server_name", new StaticPlaceholder(() -> "My Nukkit Server"));

    // Register a dynamic placeholder
    PlaceholderAPI.registerPlaceholder("player_stat", new VisitorPlaceholder((player, params) -> {
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
    String processedMessage = PlaceholderAPI.processPlaceholders(player, message);
    player.sendMessage(processedMessage);
}
```

## Installation

1. Add the `PlaceholderAPI` class to your Nukkit project.
2. Register placeholders using `registerPlaceholder`.
3. Replace placeholders in strings using `processPlaceholders`.

## License

This project is licensed under the [MIT License](LICENSE).
