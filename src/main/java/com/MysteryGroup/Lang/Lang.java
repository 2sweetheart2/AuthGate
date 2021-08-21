package com.MysteryGroup.Lang;

import com.MysteryGroup.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lang {
    private static final Map<String, String> language = new HashMap<>();

    public static boolean load(YamlConfiguration langYml) {
        try {
            for (Map.Entry<String, Object> entry : Objects.requireNonNull(langYml.getConfigurationSection(Objects.requireNonNull(Main.config.getString("lang")))).getValues(false).entrySet()) {
                language.put(Main.config.getString("lang") + "." + entry.getKey(), String.valueOf(entry.getValue()));
            }
            return true;
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Can't load language. Check lang param in config and language in lang.yml");
            return false;
        }
    }

    public static String getMessage(String target) {
        return language.get(Main.config.getString("lang") + "." + target);
    }
}
