package com.angga7togk.placeholderapi;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.angga7togk.placeholderapi.placeholder.Placeholder;

public class PlaceholderAPI extends PluginBase {

  private final Map<String, Placeholder> placeholders = new HashMap<>();

  private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)(?::([^%]*))?%");

  private static PlaceholderAPI instance;

  @Override
  public void onLoad() {
    super.onLoad();
    instance = this;
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

  public static PlaceholderAPI getInstance() {
    return instance;
  }
}
