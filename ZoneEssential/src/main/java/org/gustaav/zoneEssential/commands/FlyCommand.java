package org.gustaav.zoneEssential.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends Command {

    public FlyCommand() {
        super(
                "voar",
                "Comando para voar.",
                "Use /voar",
                new String[]{"fly"},
                "zoneessential.voar"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {return;}

        if(player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("§cVocê não está mais voando.");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§eVocê começou a voar.");
        }

    }
}
