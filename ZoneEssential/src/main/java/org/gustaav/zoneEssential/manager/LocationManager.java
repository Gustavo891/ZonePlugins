package org.gustaav.zoneEssential.manager;

import org.bukkit.Location;

import java.util.Map;

public class LocationManager {

    private final ConfigManager configManager;
    private Map<String, Location> warpLocations;
    private Location spawnLocation;

    public LocationManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.warpLocations = configManager.loadWarpLocations();
        this.spawnLocation = configManager.loadSpawnLocation();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        configManager.saveSpawnLocation(location);
    }

    public Location getWarpLocation(String name) {
        return warpLocations.get(name);
    }

    public void setWarpLocation(String name, Location location) {
        warpLocations.put(name, location);
        configManager.saveWarpLocations(warpLocations);
    }

}
