package org.gustaav.zoneEconomy.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gustaav.zoneEconomy.manager.EconomyManager;

public class JoinQuit implements Listener {

    EconomyManager economyManager;

    public JoinQuit(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        economyManager.loadPlayerEconomy(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        economyManager.savePlayerEconomy(player.getUniqueId());
    }

}
