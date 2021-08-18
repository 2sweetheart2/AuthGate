package com.MysteryGroup;

import com.MysteryGroup.Commands.Login;
import com.MysteryGroup.Commands.Register;
import com.MysteryGroup.Objects.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class Main extends JavaPlugin {
    public HashMap<Player,Integer> needAuth = new HashMap<>();
    public HashMap<UUID, User> allAuth = new HashMap<>();
    public int wrong_pass_count;
    public boolean send_global_join_message;
    public boolean send_local_join_message;
    public int range_local_join_message;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MainListener(this),this);
        Objects.requireNonNull(getCommand("register")).setExecutor(new Register(this));
        Objects.requireNonNull(getCommand("login")).setExecutor(new Login(this));
        new Thread(()->Bukkit.getPluginManager().registerEvents(new Listeners(this),this)).start();
        File file = new File(getDataFolder()+File.separator+"config.yml");
        if(!file.exists()){
            file.mkdir();
            Messages.SendMessageToConsole("Creating new config...",null);
            getConfig().options().copyDefaults(true);
        }
        try {
            send_global_join_message = getConfig().getBoolean("send_global_join_message");
            send_local_join_message = getConfig().getBoolean("send_local_join_message");
            wrong_pass_count = getConfig().getInt("wrong_pass_count");
            range_local_join_message = getConfig().getInt("range_local_join_message");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    public boolean registedUser(UUID uuid){
        return allAuth.containsKey(uuid);
    }

    public void registerUser(User user){
        this.allAuth.put(user.uuid,user);
    }

    public User getUserByUUID(UUID uuid){
        return allAuth.getOrDefault(uuid, null);
    }
}
