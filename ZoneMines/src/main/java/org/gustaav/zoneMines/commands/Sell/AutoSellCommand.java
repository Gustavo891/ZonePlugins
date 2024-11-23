package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneMines.modules.SellModule;
import revxrsal.commands.annotation.Command;

public class AutoSellCommand{

    SellModule sellModule;

    public AutoSellCommand(SellModule sellModule) {
        this.sellModule = sellModule;
    }

    @Command("autovender")
    public void autoSell(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if(!(player.hasPermission("zonemines.autovender"))) {
            player.sendMessage("§cSem permissão.");
        }
        if(sellModule.getAutoSellList().contains(player.getUniqueId())) {
            sellModule.getAutoSellList().remove(player.getUniqueId());
            player.sendMessage("§cVocê desativou o auto-vender.");
        } else {
            sellModule.getAutoSellList().add(player.getUniqueId());
            player.sendMessage("§aVocê ativou o auto-vender.");
        }
    }
}
