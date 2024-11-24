package org.gustaav.zoneEssential;

import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneEssential.commands.*;
import org.gustaav.zoneEssential.listeners.GenericEvents;
import org.gustaav.zoneEssential.manager.ConfigManager;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.manager.LocationManager;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;

public final class ZoneEssential extends JavaPlugin {

    KitManager kitManager;

    @Override
    public void onEnable() {

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).build();

        ConfigManager configManager = new ConfigManager(this);
        LocationManager locationManager = new LocationManager(configManager);
        kitManager = new KitManager(this);



        lamp.register(new FixCommand());
        lamp.register(new GamemodeCommand());
        lamp.register(new FlyCommand());
        lamp.register(new LocationCommand(locationManager));
        lamp.register(new SpawnCommand(locationManager));
        lamp.register(new KitCommand(kitManager, this));
        lamp.register(new LuzCommand());
        GenericEvents genericEvents = new GenericEvents(locationManager);
        lamp.register(genericEvents);

        getServer().getPluginManager().registerEvents(genericEvents, this);

    }

    @Override
    public void onDisable() {
        kitManager.saveAllKits();
    }

    public KitManager getKitManager() {
        return kitManager;
    }
}
