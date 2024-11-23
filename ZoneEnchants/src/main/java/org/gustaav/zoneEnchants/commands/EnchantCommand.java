package org.gustaav.zoneEnchants.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEnchants.view.EnchantGUI;
import revxrsal.commands.annotation.Command;

public class EnchantCommand {

    @Command({"encantar", "enchant", "encantamento"})
    public void enchantCommand(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }

        new EnchantGUI(player);
    }

}
