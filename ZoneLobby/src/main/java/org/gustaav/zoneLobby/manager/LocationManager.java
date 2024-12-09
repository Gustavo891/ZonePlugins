package org.gustaav.zoneLobby.manager;

import lombok.Getter;
import org.bukkit.Location;

import java.util.Map;

public class LocationManager {

    private final ConfigManager configManager;
    @Getter
    private Location rankupLocation;
    @Getter
    private Location spawnLocation;

    public LocationManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.spawnLocation = configManager.loadSpawnLocation();
        this.rankupLocation = configManager.loadRankUPLocation();
    }

    public void setRankupLocation(Location rankupLocation) {
        this.rankupLocation = rankupLocation;
        configManager.saveRankUPLocation(rankupLocation);
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        configManager.saveSpawnLocation(location);
    }

}
