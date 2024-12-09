package org.gustaav.zoneMissions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.gustaav.zoneMissions.ZoneMissions;
import org.gustaav.zoneMissions.models.PlayerModel;

import java.util.Iterator;

public class JoinLeaveEvent implements Listener {

    ZoneMissions plugin;
    public JoinLeaveEvent(ZoneMissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinLeave(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModel model = plugin.getMongoManager().loadPlayerModel(player);
        plugin.getManager().getPlayers().add(model);

        if(plugin.getManager().checkComplete(model)) {
            plugin.getManager().nextMission(model, player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Iterator<PlayerModel> iterator = plugin.getManager().getPlayers().iterator();

        while (iterator.hasNext()) {
            PlayerModel model = iterator.next();
            if (model.getUuid().equals(player.getUniqueId())) { // Use equals para comparar UUID
                plugin.getMongoManager().savePlayerModel(model);
                iterator.remove(); // Remove o elemento de forma segura
            }
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        plugin.getMongoManager().saveAllMissions();
    }


}
