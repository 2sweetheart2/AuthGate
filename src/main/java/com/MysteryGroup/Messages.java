package com.MysteryGroup;

import org.bukkit.Bukkit;

public class Messages {

    public static void SendMessageToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage("[AuthGate] "+message);
    }

}
