package org.gustaav.zoneEssential.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

public class FlyCommand {

    @CommandPermission("zoneesential.fly")
    @Command({"voar", "fly"})
    public void execute(CommandSender sender) {
        if(!(sender instanceof Player player)) {return;}

        if(player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            sendFormattedMessage(player, "${Colors.RED}Você parou de voar.");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            sendFormattedMessage(player, "${Colors.GREEN}Você começou a voar.");
        }

    }
}
