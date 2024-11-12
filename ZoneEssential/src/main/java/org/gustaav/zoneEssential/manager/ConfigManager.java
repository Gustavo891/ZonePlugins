package org.gustaav.zoneEssential.manager;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.gustaav.zoneEssential.ZoneEssential;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final ZoneEssential plugin;
    private final FileConfiguration config;

    public ConfigManager(ZoneEssential plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public Location loadSpawnLocation() {
        if (config.contains("spawn")) {
            return new Location(
                    plugin.getServer().getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"),
                    (float) config.getDouble("spawn.yaw"),
                    (float) config.getDouble("spawn.pitch")
            );
        }
        return null;
    }

    public void saveSpawnLocation(Location location) {
        if (location != null) {
            config.set("spawn.world", location.getWorld().getName());
            config.set("spawn.x", location.getX());
            config.set("spawn.y", location.getY());
            config.set("spawn.z", location.getZ());
            config.set("spawn.yaw", location.getYaw());
            config.set("spawn.pitch", location.getPitch());
            plugin.saveConfig();
        }
    }

    public Map<String, Location> loadWarpLocations() {
        Map<String, Location> warpLocations = new HashMap<>();
        if (config.contains("warps")) {
            for (String name : config.getConfigurationSection("warps").getKeys(false)) {
                warpLocations.put(name, new Location(
                        plugin.getServer().getWorld(config.getString("warps." + name + ".world")),
                        config.getDouble("warps." + name + ".x"),
                        config.getDouble("warps." + name + ".y"),
                        config.getDouble("warps." + name + ".z"),
                        (float) config.getDouble("warps." + name + ".yaw"),
                        (float) config.getDouble("warps." + name + ".pitch")
                ));
            }
        }
        return warpLocations;
    }

    public void saveWarpLocations(Map<String, Location> warpLocations) {
        for (Map.Entry<String, Location> entry : warpLocations.entrySet()) {
            String name = entry.getKey();
            Location location = entry.getValue();
            config.set("warps." + name + ".world", location.getWorld().getName());
            config.set("warps." + name + ".x", location.getX());
            config.set("warps." + name + ".y", location.getY());
            config.set("warps." + name + ".z", location.getZ());
            config.set("warps." + name + ".yaw", location.getYaw());
            config.set("warps." + name + ".pitch", location.getPitch());
        }
        plugin.saveConfig();
    }


}
