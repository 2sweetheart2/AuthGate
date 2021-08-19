package com.MysteryGroup.Commands;

import com.MysteryGroup.Main;
import com.MysteryGroup.Messages;
import com.MysteryGroup.Objects.User;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Register implements CommandExecutor {
    Main main;

    public Register(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for Players!");
            return true;
        }
        Player player = (Player) sender;
        if (!main.needAuth.containsKey(player)) {
            player.sendMessage("You already registered");
            return true;
        }
        if (main.registedUser(player.getUniqueId())) {
            player.sendMessage("You already registered!");
            return true;
        }
        if (args.length != 2) return false;
        String pass1 = args[0].toLowerCase().trim();
        String pass2 = args[1].toLowerCase().trim();
        if (!pass1.equals(pass2)) {
            player.sendMessage("Passwords must match!");
            return true;
        }
        main.allAuth.add(new User(player.getUniqueId(), player.getName(), pass1));
        main.needAuth.remove(player);
        Messages.SendMessageToPlayer(player, "Welcome to the server!");
        player.setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
