package com.MysteryGroup;

import com.MysteryGroup.AllListeners.Listeners;
import com.MysteryGroup.AllListeners.MainListener;
import com.MysteryGroup.Commands.Login;
import com.MysteryGroup.Commands.Register;
import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Objects.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    public HashMap<Player, Integer> needAuth = new HashMap<>();
    public List<User> allAuth = new ArrayList<>();
    public int wrong_pass_count;
    public boolean send_global_join_message;
    public boolean send_local_join_message;
    public int range_local_join_message;
    public File jsonFile;
    private Logger log = getLogger();
    public JsonStuff jsonStuff;
    private File langConfigFile = new File(getDataFolder()+File.separator+"lang.yml");
    public FileConfiguration langConfig;

    @Override
    public void onEnable() {
        FileStuffs();
        try {
            allAuth = jsonStuff.read();
        }catch (Exception e){
            e.printStackTrace();
            log.log(Level.WARNING,"can't read users file!!! Server will stopped!!!");
            Bukkit.getServer().shutdown();
        }
        Bukkit.getPluginManager().registerEvents(new MainListener(this), this);
        Objects.requireNonNull(getCommand("register")).setExecutor(new Register(this));
        Objects.requireNonNull(getCommand("login")).setExecutor(new Login(this));
        new Thread(() -> Bukkit.getPluginManager().registerEvents(new Listeners(this), this)).start();
        send_global_join_message = getConfig().getBoolean("send_global_join_message");
        send_local_join_message = getConfig().getBoolean("send_local_join_message");
        wrong_pass_count = getConfig().getInt("wrong_pass_count");
        range_local_join_message = getConfig().getInt("range_local_join_message");
    }

    private void FileStuffs(){
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            getLogger().info("Creating new config...");
            saveDefaultConfig();
            getLogger().info("Config created!");
        }
        if(!langConfigFile.exists()){
            try {
                langConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        langConfig = YamlConfiguration.loadConfiguration(langConfigFile);
        langConfig.options().copyDefaults(true);
        File flang = new File(getDataFolder(), "lang.yml");
        if (!flang.exists()) {
            InputStream lang = Main.class.getResourceAsStream("/lang.yml");
            try {
                FileOutputStream fos = new FileOutputStream(flang);
                byte[] buff = new byte[65536];
                int n;
                while ((n = lang.read(buff)) > 0) {
                    fos.write(buff, 0, n);
                    fos.flush();
                }
                fos.close();
                buff = null;
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e);
            }
        }
        Lang.load(YamlConfiguration.loadConfiguration(flang));
        jsonFile = new File(getDataFolder() + File.separator + "users.json");
        if (!jsonFile.exists()) {
            try {
                if (!jsonFile.createNewFile()) {
                    log.log(Level.WARNING, "can't create users files\nServer will be restarting");
                    Bukkit.getServer().reload();
                }
                else{
                    jsonStuff = new JsonStuff(jsonFile);
                    jsonStuff.save(allAuth);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonStuff = new JsonStuff(jsonFile);
    }

    @Override
    public void onDisable() {
        jsonStuff.save(allAuth);
    }

    public boolean registedUser(UUID uuid) {
        for(User user : allAuth){
            if(user.uuid.equals(uuid)) return true;
        }
        return false;
    }

    public User getUserByUUID(UUID uuid) {
        for(User user : allAuth){
            if(user.uuid.equals(uuid)) return user;
        }
        return null;
    }
}
