package org.gustaav.zoneEssential.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.manager.LocationManager;

public class LocationCommand extends Command {

    private final LocationManager locationManager;

    public LocationCommand(LocationManager locationManager) {
        super(
                "warp",
                "Ir para uma warp",
                "§cUse /warp (local).",
                new String[]{"location", "local"},
                ""
        );
        this.locationManager = locationManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }

        if(args.length == 1) {
            String warpName = args[0].toLowerCase();
            Location warpLocation = locationManager.getWarpLocation(warpName);
            if (warpLocation != null) {
                if(!player.hasPermission("zoneessential.warp." + warpName.toLowerCase())) {
                    player.sendMessage("§cSem permissão.");
                }
                player.teleport(warpLocation);
                player.sendMessage(ChatColor.GREEN + "Você foi teleportado para " + warpName + "!");
            } else {
                player.sendMessage(ChatColor.RED + "O local " + warpName + " não existe.");
            }
        } else if (args.length == 2) {
            if(!player.hasPermission("zoneessential.admin")) {
                player.sendMessage("§cSem permissão.");
            }
            if(args[0].equalsIgnoreCase("set")) {
                String warpName = args[1].toLowerCase();
                locationManager.setWarpLocation(warpName, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Local " + warpName + " definido com sucesso!");
            } else {
                player.sendMessage(ChatColor.RED + "Uso correto: /warp set <nome>");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Uso correto: /warp <nome>");

        }
    }
}
