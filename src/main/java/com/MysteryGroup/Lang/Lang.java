package com.MysteryGroup.Lang;

import com.MysteryGroup.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lang {
    static FileConfiguration config;

    public Lang(Main main) {
        Lang.config = main.langConfig;
    }

    private static Map<String, String> language = new HashMap<String, String>();

    public static void load(YamlConfiguration langYml) {
        for (Map.Entry<String, Object> entry : Objects.requireNonNull(langYml.getConfigurationSection(Objects.requireNonNull(config.getString("lang")))).getValues(false).entrySet()) {
            language.put(config.getString("lang") + "." + entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public static String getMessage(String target) {
        return language.get(config.getString("lang") + "." + target);
    }
}
