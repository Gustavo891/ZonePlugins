package org.gustaav.zoneEnchants.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import org.gustaav.zoneEnchants.view.EnchantGUI;
import revxrsal.commands.annotation.Command;

public class EnchantCommand {

    EnchantConfig enchantConfig;

    public EnchantCommand(EnchantConfig enchantConfig) {
        this.enchantConfig = enchantConfig;
    }

    @Command({"encantar", "enchant", "encantamento"})
    public void enchantCommand(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }

        EnchantGUI enchantGui = new EnchantGUI(player, enchantConfig);
        enchantGui.loadGui();
    }

}
