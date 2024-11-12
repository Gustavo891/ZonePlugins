package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.commands.Command;

public class SellCommand extends Command {

    public SellCommand() {
        super(
                "vender",
                "Venda os itens do seu inventário",
                "§cUse /vender",
                new String[]{"sell"},
                "zonemines.vender"
        );
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }

        int totalAtual = 0;
        float value = 0;
        boolean vendeu = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                for(SellModel model : SellModule.getSellList()) {
                    if(model.getItem().getType() == item.getType()) {
                        vendeu = true;
                        int total = player.getInventory().all(item.getType()).values().stream()
                                .mapToInt(ItemStack::getAmount)
                                .sum();
                        totalAtual += total;
                        value += model.getValue() * total;
                        player.getInventory().remove(item);
                    }
                }
            }
        }
        if(vendeu) {
            player.sendMessage("§aVocê vendeu §7x" + SellModule.format(totalAtual) + " §aitens por §2$§f" + SellModule.format(value) + "§a.");
            ZoneMines.getEconomy().depositPlayer(player, value);
        } else {
            player.sendMessage("§cVocê não possui nenhum minério para vender.");
        }
    }
}
