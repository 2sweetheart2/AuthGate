package com.MysteryGroup.Commands;

import com.MysteryGroup.Lang.Lang;
import com.MysteryGroup.Main;
import com.MysteryGroup.Messages;
import com.MysteryGroup.Objects.User;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Base64;

public class Login implements CommandExecutor {

    Main main;

    public Login(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //проверка на игрока
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for Players!");
            return true;
        }
        // если нет пароля
        if (args.length <= 0) return false;
        Player player = (Player) sender;
        //если авторизован
        if (!main.needAuth.containsKey(player)) {
            player.sendMessage("You already login!");
            return true;
        }
        //если не зарегестрирован
        if (!main.registedUser(player.getUniqueId())) {
            Messages.SendMessageToPlayer(player, Lang.getMessage("pls_register_by"));
            return true;
        }
        //получение объекта User через uuid игрока
        User user = main.getUserByUUID(player.getUniqueId());
        assert user != null;
        // -1 к попытка для ввода пароля. Если осталось 0 или меньше - кик
        // декодировка пароля и сохранение его в памяти ОТ ПЛОХОГО ШКОЛОАДМИНА:)
        byte[] decodedBytes = Base64.getDecoder().decode(user.password);
        if (!args[0].toLowerCase().trim().equals(new String(decodedBytes))) {
            main.needAuth.put(player, main.needAuth.get(player) - 1);
            player.sendMessage("Wrong password!");
            if (main.needAuth.get(player) <= 0) {
                player.kickPlayer("Wrong passwords!");
            }
            return true;
        }
        player.sendMessage("Login success!");
        //убирает тайм аут игроку
        main.timeOut.stopTask(player);
        //убирает его из стадии "зашёл, но не авторизовался"
        main.needAuth.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
