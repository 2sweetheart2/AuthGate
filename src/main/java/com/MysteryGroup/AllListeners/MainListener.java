package com.MysteryGroup.AllListeners;

import com.MysteryGroup.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    Main main;

    public MainListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if (!main.send_global_join_message) event.setJoinMessage("");
        Player player = event.getPlayer();
        if (main.send_local_join_message) sendLocalJoinMessage(player);
        if (!main.registedUser(player.getUniqueId())) player.sendMessage("plz registed");
        else player.sendMessage("plz login");
        main.needAuth.put(player, main.wrong_pass_count);
        player.setGameMode(GameMode.SPECTATOR);
    }

    private void sendLocalJoinMessage(Player player) {
        int range = main.range_local_join_message;
        for (Entity players : player.getNearbyEntities(range, range, range)) {
            players.sendMessage(ChatColor.YELLOW + player.getName() + "joined the game");
        }
    }

    @EventHandler
    public void Quit(PlayerQuitEvent event) {
        try {
            main.needAuth.remove(event.getPlayer());
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        try {
            main.needAuth.remove(event.getPlayer());
        } catch (Exception ignored) {
        }
    }
}


