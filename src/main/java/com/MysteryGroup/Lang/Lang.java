package com.MysteryGroup.Lang;

import com.MysteryGroup.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lang {
    private static final Map<String, String> language = new HashMap<>();

    public static void load(YamlConfiguration langYml) {
        for (Map.Entry<String, Object> entry : Objects.requireNonNull(langYml.getConfigurationSection(Objects.requireNonNull(Main.config.getString("lang")))).getValues(false).entrySet()) {
            language.put(Main.config.getString("lang") + "." + entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public static String getMessage(String target) {
        return language.get(Main.config.getString("lang") + "." + target);
    }
}
