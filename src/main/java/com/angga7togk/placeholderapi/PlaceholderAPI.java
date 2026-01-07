package com.angga7togk.placeholderapi;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import net.lldv.llamaeconomy.LlamaEconomy;
import net.lldv.llamaeconomy.components.api.API;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.angga7togk.placeholderapi.placeholder.Placeholder;

public class PlaceholderAPI extends PluginBase {

  private final Map<String, Placeholder> placeholders = new HashMap<>();

  private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)(?::([^%]*))?%");

  private static PlaceholderAPI INSTANCE;

  public static PlaceholderAPI get() {
    return INSTANCE;
  }

  @Override
  public void onLoad() {
    INSTANCE = this;
    registerDefaults();
  }

  public void registerPlaceholder(String identifier, Placeholder placeholder) {
    placeholders.put(identifier.toLowerCase(), placeholder);
  }

  public String processPlaceholders(Player player, String text) {
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
    StringBuffer result = new StringBuffer();

    while (matcher.find()) {
      String identifier = matcher.group(1).toLowerCase();
      String paramsRaw = matcher.group(2);

      Placeholder placeholder = placeholders.get(identifier);
      if (placeholder != null) {
        String[] params = paramsRaw != null ? paramsRaw.split(",") : new String[0];
        String replacement = placeholder.process(player, params);
        matcher.appendReplacement(result, replacement != null ? replacement : "");
      }
    }

    matcher.appendTail(result);
    return result.toString();
  }

  public void register(String identifier, Placeholder placeholder) {
    placeholders.put(identifier.toLowerCase(), placeholder);
  }

  @Deprecated
  public static PlaceholderAPI getInstance() {
    return INSTANCE;
  }

  private void registerDefaults() {
    this.register("player", (player, params) -> player.getName());
    this.register("player_displayname", (player, params) -> player.getDisplayName());
    this.register("player_nametag", (player, params) -> player.getNameTag());
    this.register("player_uuid", (player, params) -> player.getUniqueId().toString());
    this.register("player_ping", (player, params) -> String.valueOf(player.getPing()));
    this.register("player_level", (player, params) -> player.getLevel().getName());
    this.register("player_health", (player, params) -> String.valueOf(player.getHealth()));
    this.register("player_max_health", (player, params) -> String.valueOf(player.getMaxHealth()));
    this.register("player_saturation",
        (player, params) -> String.valueOf(player.getFoodData().getFoodSaturationLevel()));
    this.register("player_food", (player, params) -> String.valueOf(player.getFoodData().getLevel()));
    this.register("player_max_food", (player, params) -> String.valueOf(player.getFoodData().getMaxLevel()));
    this.register("player_gamemode", (player, params) -> String.valueOf(player.getGamemode()));
    this.register("player_item", (player, params) -> player.getInventory().getItemInHand().getName());
    this.register("player_offhand", (player, params) -> player.getOffhandInventory().getItem(0).getName());
    this.register("player_exp", (player, params) -> String.valueOf(player.getExperience()));
    this.register("player_exp_level", (player, params) -> String.valueOf(player.getExperienceLevel()));
    this.register("player_version", (player, params) -> String.valueOf(player.getGameVersion().toString()));
    this.register("player_protocol", (player, params) -> String.valueOf(player.getGameVersion().getProtocol()));

    this.register("server_online", (player, params) -> String.valueOf(getServer().getOnlinePlayers().size()));
    this.register("server_max_players", (player, params) -> String.valueOf(getServer().getMaxPlayers()));
    this.register("server_motd", (player, params) -> getServer().getMotd());
    this.register("server_tps", (player, params) -> String.valueOf(getServer().getTicksPerSecond()));
    this.register("server_tick", (player, params) -> String.valueOf(getServer().getTick()));
    this.register("server_difficulty", (player, params) -> String.valueOf(getServer().getDifficulty()));
    this.register("server_version", (player, params) -> ProtocolInfo.MINECRAFT_VERSION_NETWORK);
    this.register("server_protocol", (player, params) -> String.valueOf(ProtocolInfo.CURRENT_PROTOCOL));
    this.register("player_pos", (p, arg) -> {
      if (arg.length == 0)
        return p.getFloorX() + " " + p.getFloorY() + " " + p.getFloorZ();
      return switch (arg[0]) {
        case "x" -> String.valueOf(p.getFloorX());
        case "y" -> String.valueOf(p.getFloorY());
        case "z" -> String.valueOf(p.getFloorZ());
        default -> "unknown";
      };
    });
    this.register("time", (player, params) -> {
      Date now = new Date();

      // default
      String format = "yyyy-MM-dd HH:mm:ss";
      TimeZone timeZone = TimeZone.getDefault();

      if (params.length > 0 && !params[0].isEmpty()) {
        format = params[0];
      }

      if (params.length > 1 && !params[1].isEmpty()) {
        timeZone = TimeZone.getTimeZone(params[1]);
      }

      SimpleDateFormat sdf = new SimpleDateFormat(format);
      sdf.setTimeZone(timeZone);
      return sdf.format(now);
    });

    /** Luckperms Placeholders */
    if (this.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
      LuckPerms luckPerms = LuckPermsProvider.get();
      this.register("luckperms_prefix", (player, params) -> {
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String prefix = user.getCachedData().getMetaData(luckPerms.getContextManager().getQueryOptions(player))
            .getPrefix();
        return prefix != null ? prefix : "";
      });
      this.register("luckperms_suffix", (player, params) -> {
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String suffix = user.getCachedData().getMetaData(luckPerms.getContextManager().getQueryOptions(player))
            .getSuffix();
        return suffix != null ? suffix : "";
      });
      this.register("luckperms_primary_group", (player, params) -> {
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        return user.getPrimaryGroup();
      });
    }

    // Llama Economy Placeholder
    if (this.getServer().getPluginManager().getPlugin("LlamaEconomy") != null) {
      API llama = LlamaEconomy.getAPI();
      this.register("llamaeconomy_money", (player, params) -> {
        double money = llama.getMoney(player);

        // kalau ga ada params → angka biasa
        if (params == null || params.length == 0) {
          return String.valueOf((long) money);
        }

        String param = params[0].toLowerCase();

        switch (param) {
          case "id":
            return formatCurrency(money, "id", "ID");
          case "us":
            return formatCurrency(money, "en", "US");
          case "jp":
            return formatCurrency(money, "ja", "JP");
          case "sg":
            return formatCurrency(money, "en", "SG");
          default:
            return String.valueOf((long) money);
        }
      });
    }

  }

  private String formatCurrency(double amount, String lang, String country) {
    Locale locale = new Locale(lang, country);
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    return format.format(amount);
  }
}
