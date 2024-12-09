package org.gustaav.zoneMissions;

import org.bukkit.entity.Player;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.PlayerModel;

import java.util.Optional;

public class MissionAPI {

    private static MissionAPI instance;
    private MissionManager manager;

    public MissionAPI(MissionManager manager) {
        this.manager = manager;
        instance = this;
    }

    public static MissionAPI getInstance () {
        return instance;
    }

    public MissionManager getManager() {
        return manager;
    }

    public PlayerModel getPlayerModel(Player player) {
        for(PlayerModel playerModel : manager.getPlayers()) {
            if(playerModel.getUuid() == player.getUniqueId()) return playerModel;
        }
        return null;
    }


}
