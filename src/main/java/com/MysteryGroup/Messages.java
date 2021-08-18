package com.MysteryGroup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages {

    /**
     * send message to player by class Player and also if this player is online
     * @param targer target player
     * @param message message
     * @return true - if the message was delivered successfully
     */
    public static boolean SendMessageToPlayer(Player targer,String message){
        if(!targer.isOnline()) return false;
        targer.sendMessage(message);
        return true;
    }

    /**
     * get player on server by nick name, and if he is online and he has, send message him
     * @param nickName nick name of player
     * @param message message
     * @return true - if the message was delivered successfully
     */
    public static boolean SendMessageToPlayer(String nickName,String message){
        Player player = Bukkit.getPlayer(nickName);
        if(player== null || !player.isOnline()) return false;
        player.sendMessage(message);
        return true;
    }

    /**
     * get player on server by UUID, and if he has and he is online - send message
     * @param uuid UUID of player
     * @param message message
     * @return true - if the message was delivered successfully
     */
    public static boolean SendMessageToPlayer(UUID uuid, String message){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null || !player.isOnline()) return false;
        player.sendMessage(message);
        return true;
    }

    /**
     * Send message to console
     * @param message message
     * @param level can be null. If null - level = default
     */
    public static void SendMessageToConsole(String message, Level level){
        Logger log = Bukkit.getLogger();
        if(level == null) log.info(message);
        else log.log(level,message);
    }

}
