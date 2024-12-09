package org.gustaav.zoneMines;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneBoosters.BoosterAPI;
import org.gustaav.zoneEnchants.EnchantAPI;
import org.gustaav.zoneMines.Listener.MinesListener;
import org.gustaav.zoneMines.commands.MineCommand;
import org.gustaav.zoneMines.commands.Sell.AutoSellCommand;
import org.gustaav.zoneMines.commands.Sell.SellCommand;
import org.gustaav.zoneMines.modules.SellModule;
import org.gustaav.zoneMines.commands.compact.CompactCommand;
import org.gustaav.zoneMines.commands.Explosivo;
import org.gustaav.zoneMines.explosives.ExplosivoListener;
import org.gustaav.zoneMines.managers.classic.LapisManager;
import org.gustaav.zoneMines.managers.PlaceholderAPI;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public final class ZoneMines extends JavaPlugin {

    LapisManager lapisManager;
    SellModule sellModule;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        sellModule = new SellModule(this);
        lapisManager = new LapisManager(this, sellModule);
        getServer().getPluginManager().registerEvents(lapisManager, this);
        getServer().getPluginManager().registerEvents(new MinesListener(sellModule), this);

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this).build();

        new PlaceholderAPI(lapisManager).register();

        getServer().getPluginManager().registerEvents(new ExplosivoListener(this, lapisManager, sellModule), this);

        lamp.register(new Explosivo());
        lamp.register(new CompactCommand(this));
        lamp.register(new SellCommand(sellModule));
        lamp.register(new AutoSellCommand(sellModule));
        lamp.register(new MineCommand(lapisManager));
    }

    public ZoneMines getZoneMines() {
        return this;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Economy getEconomy() {
        return econ;
    }


}
