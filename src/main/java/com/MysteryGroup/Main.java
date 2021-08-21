package com.MysteryGroup;

import com.MysteryGroup.AllListeners.Listeners;
import com.MysteryGroup.AllListeners.MainListener;
import com.MysteryGroup.Commands.Login;
import com.MysteryGroup.Commands.Register;
import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Objects.User;
import com.MysteryGroup.TimeOuts.TimeOut;
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

public final class Main extends JavaPlugin {
    public HashMap<Player, Integer> needAuth = new HashMap<>();
    public List<User> allAuth = new ArrayList<>();
    public TimeOut timeOut = new TimeOut();


    public File jsonFile;
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
            Messages.SendMessageToConsole(ChatColor.RED + "can't read users file!!! Plugin will stopped!!!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        //регистрация евентов и команд
        Bukkit.getPluginManager().registerEvents(new MainListener(this), this);
        Objects.requireNonNull(getCommand("register")).setExecutor(new Register(this));
        Objects.requireNonNull(getCommand("login")).setExecutor(new Login(this));
        new Thread(() -> Bukkit.getPluginManager().registerEvents(new Listeners(this), this)).start();
        checkOnlinePlayers();
        Messages.SendMessageToConsole(ChatColor.GREEN+"AuthGate is working!");
    }

    //кик всех челов в стадии "защёл но не авторизовался" (случай если сервер reload, а чел не авторизовался)
    private void checkOnlinePlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            player.setGameMode(GameMode.SPECTATOR);
            String text;
            String message;
            needAuth.put(player, getConfig().getInt("wrong_pass_count"));
            if (registedUser(player.getUniqueId())) {
                player.sendMessage(Lang.getMessage("plz_login"));
                text = "/login [password]";
                message = Lang.getMessage("plz_login");
            } else {
                player.sendMessage(Lang.getMessage("plz_register"));
                text = "/reg [password] [password]";
                message = Lang.getMessage("plz_register");
            }
            //создание тайм аута
            if (getConfig().getBoolean("enable_time_out"))
                timeOut.createTask(player,
                        getConfig().getInt("time_out"), message,
                        getConfig().getInt("message_interval"),
                        text, "", getServer(), this);
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
            Messages.SendMessageToConsole(ChatColor.AQUA + "Creating new config...");
            saveDefaultConfig();
            Messages.SendMessageToConsole(ChatColor.AQUA + "Config created!");
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
        if(!Lang.load(YamlConfiguration.loadConfiguration(flang))) Bukkit.getPluginManager().disablePlugin(this);
    }

    private void JsonFileSetup() {
        jsonFile = new File(getDataFolder() + File.separator + "users.json");
        if (!jsonFile.exists()) {
            try {
                if (!jsonFile.createNewFile()) {
                    Messages.SendMessageToConsole(ChatColor.RED + "can't create users files!!! Plugin will be disable!!!");
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
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
