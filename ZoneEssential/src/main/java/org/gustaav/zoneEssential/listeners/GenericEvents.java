package org.gustaav.zoneEssential.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.gustaav.zoneEssential.manager.LocationManager;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GenericEvents implements Listener {

    LocationManager manager;

    public GenericEvents(LocationManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(manager.getSpawnLocation());
        event.setJoinMessage(null);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(manager.getSpawnLocation());
    }
    @EventHandler
    public void DeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    List<UUID> build = new ArrayList<UUID>();

    @CommandPermission("zoneesential.build.admin")
    @Command("build")
    public void buildCommand(CommandSender sender) {
        if(sender instanceof Player player) {
            if(build.contains(player.getUniqueId())) {
                build.remove(player.getUniqueId());
                player.sendMessage("§cModo build desativado.");
            } else {
                build.add(player.getUniqueId());
                player.sendMessage("§aModo build ativado.");
            }
        }
    }



    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if(build.contains(event.getPlayer().getUniqueId())) {
            return;
        }
        if(event.getPlayer().getWorld() != Bukkit.getWorld("plots")) {
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void blockPlace(BlockPlaceEvent event) {
        if(event.getPlayer().isOp()) {
            return;
        }
        if(event.getPlayer().getWorld() == manager.getSpawnLocation().getWorld()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void damageEvent(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(player.getWorld() == manager.getSpawnLocation().getWorld()) {
                event.setCancelled(true);
            }
        }
    }

}
