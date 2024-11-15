package org.gustaav.zoneEssential.kits.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.commands.Command;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;

public class RemoverKitCommand extends Command {

    KitManager manager;

    public RemoverKitCommand(ZoneEssential plugin) {
        super(
                "removerkit",
                "Remover um kit no servidor",
                "Use /removerkit {tipo}",
                new String[]{},
                "zoneessential.removerkit");
        this.manager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {return;}

        if (args.length != 1) {
            sender.sendMessage("§cUso correto: /removerkit {kit}");
            return;
        }

        String tipo = args[0].toLowerCase();
        for(KitModel kit : manager.getKits()) {
            if(kit.getKitType().equalsIgnoreCase(tipo)) {
                player.sendMessage("§cO kit '§f" + kit.getKitName() + "§c' foi removido.");
                manager.removeKit(kit);
                return;
            }
        }
        player.sendMessage("§cKit não encontrado.");

    }
}
