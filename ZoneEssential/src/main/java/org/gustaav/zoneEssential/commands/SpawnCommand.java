package org.gustaav.zoneEssential.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.manager.LocationManager;

public class SpawnCommand extends Command {

    private final LocationManager locationManager;

    public SpawnCommand(LocationManager locationManager) {
        super(
                "spawn",
                "Ir para o spawn",
                "§cUse /spawn.",
                new String[]{},
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
            locationManager.setSpawnLocation(player.getLocation());
            if(!player.hasPermission("nerdzone.setspawn")) {
                player.sendMessage("§cSem permissão.");
            }
            player.sendMessage(ChatColor.GREEN + "Local de spawn definido com sucesso!");
            return;
        } else {
            Location spawnLocation = locationManager.getSpawnLocation();
            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                player.sendMessage(ChatColor.GREEN + "Você foi teleportado para o spawn!");
            } else {
                player.sendMessage(ChatColor.RED + "O local de spawn não está definido.");
            }
        }
    }

}
