package org.gustaav.zoneEssential.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

public class FixCommand {

    @Command({"fix", "reparar"})
    @CommandPermission("zoneessential.fix")
    @Description("Repare a ferramenta do seu inventario")
    public void fix(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!(itemInHand.getType().getMaxDurability() > 0)) {
            sendFormattedMessage(player, "${Colors.RED}Você precisa segurar um item que possa ser reparado!");
            return;
        }

        if(itemInHand.getDurability() != 0) {
            itemInHand.setDurability((short) 0);
            sendFormattedMessage(player, "${Colors.GREEN}Item reparado com sucesso.");
        } else {
            sendFormattedMessage(player, "${Colors.RED}Esse item não pode ser reparado.");
        }
    }

    @Command("fix *")
    @CommandPermission("zoneessential.fix.inventory")
    public void fixInventory(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        for(ItemStack item : player.getInventory().getContents()) {
            if(item.getType().getMaxDurability() > 0 && item.getDurability() != 0) {
                item.setDurability((short) 0);
            }
        }
        sendFormattedMessage(player, "${Colors.GREEN}Seu inventário foi reparado com sucesso.");
    }

}
