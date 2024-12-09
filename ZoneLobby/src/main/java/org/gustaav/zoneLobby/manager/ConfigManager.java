package org.gustaav.zoneLobby.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.model.ServerModel;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final ZoneLobby plugin;
    private final FileConfiguration config;

    public ConfigManager(ZoneLobby plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public Map<String, ServerModel> loadServersFromConfig() {
        Map<String, ServerModel> serverCache = new HashMap<>();
        ConfigurationSection serversSection = plugin.getConfig().getConfigurationSection("servers");
        if (serversSection != null) {
            for (String serverKey : serversSection.getKeys(false)) {
                ConfigurationSection serverSection = serversSection.getConfigurationSection(serverKey);
                if (serverSection != null) {
                    serverCache.put(serverKey, new ServerModel(
                                serverSection.getString("display-name", "Desconhecido"),
                            serverSection.getString("proxy-name"),
                            serverSection.getString("ip"),
                            serverSection.getInt("port"),
                            serverSection.getBoolean("options.maintenance", false),
                            serverSection.getBoolean("options.deployment", false),
                            serverSection.getBoolean("options.beta-vip", false),
                            false
                    ));
                }
            }
            Bukkit.getLogger().info("Loaded: " + serverCache.values());
            return serverCache;
        }
        return serverCache;
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
    public Location loadRankUPLocation() {
        if (config.contains("rankup")) {
            return new Location(
                    plugin.getServer().getWorld(config.getString("rankup.world")),
                    config.getDouble("rankup.x"),
                    config.getDouble("rankup.y"),
                    config.getDouble("rankup.z"),
                    (float) config.getDouble("rankup.yaw"),
                    (float) config.getDouble("rankup.pitch")
            );
        }
        return null;
    }
    public void saveRankUPLocation(Location location) {
        if (location != null) {
            config.set("rankup.world", location.getWorld().getName());
            config.set("rankup.x", location.getX());
            config.set("rankup.y", location.getY());
            config.set("rankup.z", location.getZ());
            config.set("rankup.yaw", location.getYaw());
            config.set("rankup.pitch", location.getPitch());
            plugin.saveConfig();
        }
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



}
