package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneMines.commands.Command;

public class AutoSellCommand extends Command {

    SellModule sellModule;

    public AutoSellCommand(SellModule sellModule) {
        super(
                "autovender",
                "Ative ou desativa o auto-vender.",
                "§cUse /autovender",
                new String[]{"sellauto"},
                "zonemines.autovender"
        );
        this.sellModule = sellModule;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            return;
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
