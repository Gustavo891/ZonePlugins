package org.gustaav.zoneEssential.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.manager.LocationManager;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;

import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

@Command("spawn")
public class SpawnCommand {

    private final LocationManager locationManager;

    public SpawnCommand(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Command("spawn")
    public void goSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        Location spawnLocation = locationManager.getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            sendFormattedMessage(player, "${Colors.PURPLE_LIGHT}Você foi enviado para o spawn.");
        } else {
            sendFormattedMessage(player, "${Colors.RED_NORMAL}O local de spawn ainda não foi definido");
        }

    }

    @Command("spawn set")
    public void setSpawn(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        locationManager.setSpawnLocation(player.getLocation());
        sendFormattedMessage(player, "${Colors.PURPLE_NORMAL}O local de spawn foi definido para sua localização.");
    }

    @Command("spawn <jogador>")
    public void sendPlayer(CommandSender sender, @Named("jogador") Player target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        if(target == null || !target.isOnline()) {
            sendFormattedMessage(player, "${Colors.RED_NORMAL}O jogador não foi encontrado.");
            return;
        }
        Location spawnLocation = locationManager.getSpawnLocation();
        if (spawnLocation != null) {
            target.teleport(spawnLocation);
            sendFormattedMessage(target, "${Colors.PURPLE_LIGHT}Você foi enviado para o spawn.");
            sendFormattedMessage(player, "${Colors.PURPLE_LIGHT}Você enviou o jogador ${Colors.WHITE}" + target.getName() + "${Colors.PURPLE_LIGHT} para o spawn.");
        } else {
            sendFormattedMessage(player, "${Colors.RED_NORMAL}O local de spawn ainda não foi definido");
        }

    }

}
