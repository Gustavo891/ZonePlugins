package org.gustaav.zoneSkills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gustaav.zoneSkills.Database.PlayerDataManager;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public PlayerListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        playerDataManager.loadPlayerData(playerUUID);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        playerDataManager.unloadPlayerData(playerUUID);
    }
}
