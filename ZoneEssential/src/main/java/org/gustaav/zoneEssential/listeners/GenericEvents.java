package org.gustaav.zoneEssential.listeners;

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

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().isOp()) {
            return;
        }
        if(event.getPlayer().getWorld() == manager.getSpawnLocation().getWorld()) {
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
