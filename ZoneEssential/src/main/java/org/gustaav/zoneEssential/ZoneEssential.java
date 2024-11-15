package org.gustaav.zoneEssential;

import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneEssential.commands.*;
import org.gustaav.zoneEssential.kits.commands.*;
import org.gustaav.zoneEssential.listeners.Event;
import org.gustaav.zoneEssential.manager.ConfigManager;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.manager.LocationManager;

public final class ZoneEssential extends JavaPlugin {

    KitManager kitManager;

    @Override
    public void onEnable() {

        ConfigManager configManager = new ConfigManager(this);
        LocationManager locationManager = new LocationManager(configManager);
        kitManager = new KitManager(this);

        new LocationCommand(locationManager);
        new SpawnCommand(locationManager);
        new FixCommand();
        new GamemodeCommand();
        new FlyCommand();

        new CriarKitCommand(this);
        new KitCommand(this);
        new EditarKitCommand(this);
        new RemoverKitCommand(this);
        new ListKitsCommand(this);
        new DarkitCommand(this);

        getServer().getPluginManager().registerEvents(new Event(locationManager), this);

    }

    @Override
    public void onDisable() {
        kitManager.saveAllKits();
    }

    public KitManager getKitManager() {
        return kitManager;
    }
}
