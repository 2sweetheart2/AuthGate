package com.MysteryGroup.Commands;

import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Main;
import com.MysteryGroup.Objects.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Base64;

public class Register implements CommandExecutor {
    Main main;

    public Register(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //только для игроков
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for Players!");
            return true;
        }
        Player player = (Player) sender;
        //проверка на стадию "защёл, но не авторизовался"
        if (!main.needAuth.containsKey(player)) {
            player.sendMessage(Lang.getMessage("already_auth"));
            return true;
        }
        //проверка зарегестрирован ли юзер
        if (main.registedUser(player.getUniqueId())) {
            player.sendMessage(Lang.getMessage("already_registed"));
            return true;
        }
        if (args.length != 2) return false;
        String pass1 = args[0].toLowerCase().trim();
        String pass2 = args[1].toLowerCase().trim();
        //если два пароля не верны
        if (!pass1.equals(pass2)) {
            player.sendMessage(Lang.getMessage("must_match_pass"));
            return true;
        }
        //добавление ко всем авторизованным (кодировка пароля от плохого админа)
        main.allAuth.add(new User(player.getUniqueId(), player.getName(), Base64.getEncoder().encodeToString(pass1.getBytes())));
        //удаление из стадии "защёл, но не авторизовался"
        main.needAuth.remove(player);
        //удаление тайм аута
        main.timeOut.stopTask(player);
        player.sendMessage(Lang.getMessage("success_registered"));
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.getConsoleSender().sendMessage(String.format("The player '%s' has logged in",player.getName()));
        //челебас почему-то разучается ходить
        player.setWalkSpeed(0.2f);
        return true;
    }
}
