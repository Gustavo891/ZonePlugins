package org.gustaav.zoneEssential.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.gustaav.zoneEssential.manager.LocationManager;

public class Event implements Listener {

    LocationManager manager;

    public Event(LocationManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(manager.getSpawnLocation());
        event.joinMessage(null);
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.quitMessage(null);
    }
    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(manager.getSpawnLocation());
    }
    @EventHandler
    public void DeathEvent(PlayerDeathEvent event) {
        event.deathMessage(null);
    }

}
