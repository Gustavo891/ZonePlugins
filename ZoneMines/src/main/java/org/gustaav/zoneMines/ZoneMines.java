package org.gustaav.zoneMines;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneMines.Listener.MinesListener;
import org.gustaav.zoneMines.commands.Sell.AutoSellCommand;
import org.gustaav.zoneMines.commands.Sell.SellCommand;
import org.gustaav.zoneMines.commands.Sell.SellModule;
import org.gustaav.zoneMines.commands.compact.CompactCommand;
import org.gustaav.zoneMines.managers.LapisManager;

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
        lapisManager = new LapisManager(this);
        getServer().getPluginManager().registerEvents(lapisManager, this);
        getServer().getPluginManager().registerEvents(new MinesListener(sellModule), this);

        new CompactCommand();
        new SellCommand(sellModule);
        new AutoSellCommand(sellModule);

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
