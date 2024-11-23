package org.gustaav.zoneRanks.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gustaav.zoneRanks.rank.RankManager;

public class RankUpdateEvent implements Listener {

    RankManager rankManager;

    public RankUpdateEvent(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        rankManager.loadPlayerRank(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        rankManager.removePlayer(player.getUniqueId());
    }

}
