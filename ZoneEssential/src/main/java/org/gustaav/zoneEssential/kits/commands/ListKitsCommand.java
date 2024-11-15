package org.gustaav.zoneEssential.kits.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.commands.Command;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;

import java.awt.*;

public class ListKitsCommand extends Command {

    KitManager manager;

    public ListKitsCommand(ZoneEssential plugin) {
        super(
                "listkits",
                "Ver a lista de kits do servidor",
                "Use /listkits",
                new String[]{},
                "zoneessential.listkits");
        this.manager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof Player player)) {return;}

        if (args.length != 1) {
            player.sendMessage("§r");
            player.sendMessage(ChatColor.of(new Color(0x70ff45)) + "  Kits disponíveis:");
            boolean tem = false;
            for(KitModel kit : manager.getKits()) {
                if(player.hasPermission("zoneessential.kit." + kit.getKitType().toLowerCase())) {
                    player.sendMessage("  §8→ §f" + kit.getKitName() + " §7[" + kit.getKitType() + "]");
                    tem = true;
                }
            }
            if(!tem) {
                player.sendMessage("  §fVocê não tem acesso a nenhum kit.");
            }
            player.sendMessage("§r");
        } else {
            String tipo = args[0];
            for(KitModel kit : manager.getKits()) {
                if(kit.getKitType().toLowerCase().equals(tipo)) {
                    player.sendMessage("§eKit " + kit.getKitName() + "§e: " + kit.getListaItens());
                }
            }
        }
    }
}
