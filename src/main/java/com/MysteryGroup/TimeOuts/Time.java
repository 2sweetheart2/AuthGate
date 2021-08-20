package com.MysteryGroup.TimeOuts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Time {
    private List<Player> players = new ArrayList<>();

    public void createTask(Player target, Integer time, String message, Integer messageInterval, String title, String subTitle, Server server, Plugin plugin) {
        //тут хз что объяснять ибо да, это надо просто принять и забыть этот ужас
        server.getScheduler().runTaskAsynchronously(plugin, () -> {
            Bukkit.getLogger().info("thread work");
            long timeStart = System.currentTimeMillis();
            target.sendTitle(ChatColor.GREEN + title, subTitle);
            long interval = System.currentTimeMillis();
            while (System.currentTimeMillis() / 1000 - timeStart / 1000 < time) {
                if (System.currentTimeMillis() - interval >= messageInterval * 1000) {
                    target.sendMessage(message);
                    interval = System.currentTimeMillis();
                }
                if (players.contains(target)) return;
            }
            if (!players.contains(target)) {
                server.getScheduler().runTask(plugin, () -> target.kickPlayer("Time out!"));
                players.remove(target);
            }
        });
    }

    public void stopTask(Player player) {
        if (!players.contains(player)) players.add(player);
    }

}
