package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneMines.commands.Command;

public class SellCommand extends Command {

    SellModule sellModule;

    public SellCommand(SellModule sellModule) {
        super(
                "vender",
                "Venda os itens do seu inventário",
                "§cUse /vender",
                new String[]{"sell"},
                "zonemines.vender"
        );
        this.sellModule = sellModule;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }

        if(!sellModule.sellItems(player)) {
            player.sendMessage("§cVocê não possui nenhum minério para vender.");
        }

    }
}
