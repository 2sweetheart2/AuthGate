package com.MysteryGroup.AllListeners;

import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    Main main;

    public MainListener(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void login(PlayerLoginEvent e) {
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void join(PlayerJoinEvent event) {
        if (!main.getConfig().getBoolean("send_global_join_message")) event.setJoinMessage("");
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);

        //отправка локального сообщения
        if (main.getConfig().getBoolean("send_local_join_message")) sendLocalJoinMessage(player);

        //отправка сообщения о регистрации или входе
        if (!main.registedUser(player.getUniqueId())) player.sendMessage("plz registed");
        else player.sendMessage(Lang.getMessage("plz_login"));

        //добавление юзера в стадию "зашёл, но не авторизовался"
        if (!main.needAuth.containsKey(player)) main.needAuth.put(player, main.getConfig().getInt("wrong_pass_count"));

        //создание тайм аута
        if (main.getConfig().getBoolean("enable_time_out"))
            main.timeOut.createTask(player,
                    main.getConfig().getInt("time_out"), "reg or login",
                    main.getConfig().getInt("message_interval"),
                    "title", "sub title", main.getServer(), main);
    }

    private void sendLocalJoinMessage(Player player) {
        //поиск все ентити в радиусе RANGE и отправка им сообщения
        int range = main.getConfig().getInt("range_local_join_message");
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


