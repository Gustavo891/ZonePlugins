package org.gustaav.zoneEssential.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FixCommand extends Command {

    public FixCommand() {
        super(
                "reparar",
                "Repare o item em sua mão",
                "Use /reparar",
                new String[]{"fix"},
                "zoneessential.reparar"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return;
        }
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!(itemInHand.getType().getMaxDurability() > 0)) {
            player.sendMessage("§cVocê precisa segurar um item que possa ser reparado!");
            return;
        }

        if(itemInHand.getDurability() != 0) {
            itemInHand.setDurability((short) 0);
            player.sendMessage("§aItem reparado com sucesso.");
        } else {
            player.sendMessage("§cEsse item não pode ser reparado.");
        }

    }
}
