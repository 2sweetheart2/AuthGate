package com.MysteryGroup;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class Listeners implements Listener {
    Main main;
    public Listeners(Main main){
        this.main = main;
    }

    @EventHandler
    public void move(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(main.needAuth.containsKey(player)) event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(main.needAuth.containsKey(player)) e.setCancelled(true);
    }
//
//    @EventHandler
//    public void dropItem(PlayerDropItemEvent e){
//        Player player = e.getPlayer();
//        if(main.needAuth.contains(player)) e.setCancelled(true);
//    }
//
//    @EventHandler
//    public void giveDamage(PlayerItemDamageEvent e){
//        Player player = e.getPlayer();
//        if(main.needAuth.contains(player)) e.setCancelled(true);
//    }
//
    @EventHandler
    public void chat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(main.needAuth.containsKey(player)) e.setCancelled(true);
    }

}
