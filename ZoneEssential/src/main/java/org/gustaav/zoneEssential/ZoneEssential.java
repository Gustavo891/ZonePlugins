package org.gustaav.zoneEssential;

import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneEssential.commands.LocationCommand;
import org.gustaav.zoneEssential.commands.SpawnCommand;
import org.gustaav.zoneEssential.listeners.Event;
import org.gustaav.zoneEssential.manager.ConfigManager;
import org.gustaav.zoneEssential.manager.LocationManager;

public final class ZoneEssential extends JavaPlugin {

    @Override
    public void onEnable() {

        ConfigManager configManager = new ConfigManager(this);
        LocationManager locationManager = new LocationManager(configManager);

        new LocationCommand(locationManager);
        new SpawnCommand(locationManager);

        getServer().getPluginManager().registerEvents(new Event(locationManager), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
