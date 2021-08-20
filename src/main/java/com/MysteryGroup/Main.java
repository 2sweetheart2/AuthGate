package com.MysteryGroup;

import com.MysteryGroup.AllListeners.Listeners;
import com.MysteryGroup.AllListeners.MainListener;
import com.MysteryGroup.Commands.Login;
import com.MysteryGroup.Commands.Register;
import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Objects.User;
import com.MysteryGroup.TimeOuts.Time;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
    public Time time = new Time();


    public File jsonFile;
    private final Logger log = getLogger();
    public JsonStuff jsonStuff;
    public static FileConfiguration config;


    @Override
    public void onEnable() {
        cofigStuffs();
        langFilesStuffs();
        JsonFileSetup();
        //попытка чтение json файла с юзерами, в случае неудачи - выключение плагина
        try {
            allAuth = jsonStuff.read();
        } catch (Exception e) {
            log.log(Level.WARNING, "can't read users file!!! Plugin will stopped!!!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        //регистрация евентов и команд
        Bukkit.getPluginManager().registerEvents(new MainListener(this), this);
        Objects.requireNonNull(getCommand("register")).setExecutor(new Register(this));
        Objects.requireNonNull(getCommand("login")).setExecutor(new Login(this));
        new Thread(() -> Bukkit.getPluginManager().registerEvents(new Listeners(this), this)).start();
        checkOnlinePlayers();
    }

    //кик всех челов в стадии "защёл но не авторизовался" (случай если сервер reload, а чел не авторизовался)
    private void checkOnlinePlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for(Player player: players){
            player.setGameMode(GameMode.SPECTATOR);
            needAuth.put(player,getConfig().getInt("wrong_pass_count"));
            if(registedUser(player.getUniqueId())){
                player.sendMessage(Lang.getMessage("plz_login"));
            }else{
                player.sendMessage(Lang.getMessage("pls_register_by"));
            }
            //создание тайм аута
            if (getConfig().getBoolean("enable_time_out"))
                time.createTask(player,
                        getConfig().getInt("time_out"), "reg or login",
                        getConfig().getInt("message_interval"),
                        "title", "sub title", getServer(), this);
        }
    }

    @Override
    public void onDisable() {
        jsonStuff.save(allAuth);
    }

    public boolean registedUser(UUID uuid) {
        for (User user : allAuth) {
            if (user.uuid.equals(uuid)) return true;
        }
        return false;
    }

    public User getUserByUUID(UUID uuid) {
        for (User user : allAuth) {
            if (user.uuid.equals(uuid)) return user;
        }
        return null;
    }

    private void cofigStuffs() {
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            getLogger().info("Creating new config...");
            saveDefaultConfig();
            getLogger().info("Config created!");
        }
        config = getConfig();
    }

    private void langFilesStuffs() {
        File flang = new File(getDataFolder(), "lang.yml");
        if (!flang.exists()) {
            InputStream lang = Main.class.getResourceAsStream("/lang.yml");
            try {
                FileOutputStream fos = new FileOutputStream(flang);
                byte[] buff = new byte[65536];
                int n;
                while (true) {
                    assert lang != null;
                    if (!((n = lang.read(buff)) > 0)) break;
                    fos.write(buff, 0, n);
                    fos.flush();
                }
                fos.close();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e);
            }
        }
        Lang.load(YamlConfiguration.loadConfiguration(flang));
    }

    private void JsonFileSetup() {
        jsonFile = new File(getDataFolder() + File.separator + "users.json");
        if (!jsonFile.exists()) {
            try {
                if (!jsonFile.createNewFile()) {
                    log.log(Level.WARNING, "can't create users files\nServer will be restarting");
                    Bukkit.getServer().reload();
                } else {
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

}
