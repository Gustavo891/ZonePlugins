package org.gustaav.zoneEssential.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.gustaav.zoneEssential.utils.MessageUtil;
import revxrsal.commands.annotation.Command;

public class LuzCommand {

    @Command({"luz", "lanterna"})
    public void luz(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            MessageUtil.sendFormattedMessage(player, "${Colors.RED_NORMAL}Você desativou a sua lanterna.");
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            MessageUtil.sendFormattedMessage(player, "${Colors.GREEN_LIGHT}Você ativou a sua lanterna.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
        }
    }

}
