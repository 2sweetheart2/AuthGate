package com.MysteryGroup.Commands;

import com.MysteryGroup.Main;
import com.MysteryGroup.Messages;
import com.MysteryGroup.Objects.User;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Login implements CommandExecutor {

    Main main;

    public Login(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only for Players!");
            return true;
        }
        if(args.length<=0) return false;
        Player player = (Player) sender;
        if(!main.needAuth.containsKey(player)){
            player.sendMessage("You already login!");
            return true;
        }
        if(!main.registedUser(player.getUniqueId())){
            Messages.SendMessageToPlayer(player,"Please, register by command /reg or /register [password] [password]");
            return true;
        }
        User user = main.getUserByUUID(player.getUniqueId());
        if(!args[0].toLowerCase().trim().equals(user.password)){
            main.needAuth.put(player,main.needAuth.get(player)-1);
            player.sendMessage("Wrong password!");
            if(main.needAuth.get(player)<=0){
                player.kickPlayer("Wrong passwords!");
            }
            return true;
        }
        player.sendMessage("Login success!");
        main.needAuth.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
