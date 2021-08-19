package com.MysteryGroup.AllListeners;

import com.MysteryGroup.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class Listeners implements Listener {
    Main main;

    public Listeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (main.needAuth.containsKey(player)) event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (main.needAuth.containsKey(player)) e.setCancelled(true);
    }

    @EventHandler
    public void changeGamemode(PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        if (main.needAuth.containsKey(player)) e.setCancelled(true);
    }

    @EventHandler
    public void tp(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (main.needAuth.containsKey(player)) e.setCancelled(true);
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (main.needAuth.containsKey(player)) {
            String command = event.getMessage().replace("/", "");
            if (!command.startsWith("login") || !command.startsWith("reg") || !command.startsWith("register")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (main.needAuth.containsKey(player)) e.setCancelled(true);
    }

}
