package org.gustaav.zoneEssential.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneEssential.gui.locations.MinesGUI;
import org.gustaav.zoneEssential.manager.LocationManager;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

@Command("warp")
@CommandPermission("zoneessential.warp")
public class LocationCommand {

    private final LocationManager locationManager;

    public LocationCommand(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Command("warp <local>")
    public void warp(CommandSender sender, String local) {
        local = local.toLowerCase();
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        Location warpLocation = locationManager.getWarpLocation(local);
        if (warpLocation != null) {
            if(!player.hasPermission("zoneessential.warp." + local.toLowerCase())) {
                sendFormattedMessage(player, "${Colors.RED}Sem permissão.");
            }
            player.teleport(warpLocation);
            sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Você foi enviado para: '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}'.", local));

        } else {
            sendFormattedMessage(player, String.format("${Colors.RED}O local '${Colors.WHITE}%s${Colors.RED}' não existe.", local));
        }

    }

    @CommandPermission("zoneessential.admin")
    @Command("warp set <local>")
    public void set(CommandSender sender, String local) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        local = local.toLowerCase();
        locationManager.setWarpLocation(local, player.getLocation());
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}O local '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}' foi definido com sucesso.", local));

    }

    @Command({"minas", "mina"})
    public void minas(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }
        MinesGUI minesGUI = new MinesGUI(player);

    }

}
