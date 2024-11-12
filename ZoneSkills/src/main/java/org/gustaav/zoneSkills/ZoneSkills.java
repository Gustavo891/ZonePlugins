package org.gustaav.zoneSkills;

import org.bukkit.plugin.java.JavaPlugin;

public final class ZoneSkills extends JavaPlugin {

    private MongoManager mongoManager;
    private PlayerDataManager playerDataManager;

    private MiningSkill miningSkill;

    @Override
    public void onEnable() {

        mongoManager = new MongoManager();
        playerDataManager = new PlayerDataManager(this, mongoManager);

        miningSkill = new MiningSkill(this, playerDataManager);

        getServer().getPluginManager().registerEvents(miningSkill, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerDataManager), this);
        getLogger().info("ZoneSkills habilitado!");
    }

    @Override
    public void onDisable() {
        // Fecha a conexão com o MongoDB
        mongoManager.close();
        getLogger().info("ZoneSkills desabilitado!");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public MiningSkill getMiningSkill() {
        return miningSkill;
    }
}
