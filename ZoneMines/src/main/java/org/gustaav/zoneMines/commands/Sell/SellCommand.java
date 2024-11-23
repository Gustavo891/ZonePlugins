package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneMines.modules.SellModule;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;

public class SellCommand {

    SellModule sellModule;

    public SellCommand(SellModule sellModule) {
        this.sellModule = sellModule;
    }

    @Command("vender")
    public void vender(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }

        if(!(player.hasPermission("zonemines.vender"))) {
            player.sendMessage("§cSem permissão.");
        }

        // Garante que os itens nulos sejam filtrados corretamente
        ItemStack[] contents = player.getInventory().getContents();
        if (contents.length == 0) {
            player.sendMessage("§cVocê não possui nenhum minério para vender.");
            return;
        }

        // Chama o método sellItems
        if (!sellModule.sellItems(player, 1, new ArrayList<ItemStack>())) {
            player.sendMessage("§cVocê não possui nenhum minério para vender.");
        }

    }

}
