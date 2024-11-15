package org.gustaav.zoneSkills;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.gustaav.zoneSkills.Database.MongoManager;
import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.commands.SkillAdminCommand;
import org.gustaav.zoneSkills.commands.SkillsCommand;
import org.gustaav.zoneSkills.listeners.PlayerListener;
import org.gustaav.zoneSkills.skills.MiningSkill;

public final class ZoneSkills extends JavaPlugin {

    private MongoManager mongoManager;
    private PlayerDataManager playerDataManager;

    private static Permission perms = null;

    private MiningSkill miningSkill;

    @Override
    public void onEnable() {

        setupPermissions();
        mongoManager = new MongoManager(this);
        playerDataManager = new PlayerDataManager(this, mongoManager);

        miningSkill = new MiningSkill(this, playerDataManager);

        getServer().getPluginManager().registerEvents(miningSkill, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(playerDataManager), this);
        getLogger().info("ZoneSkills habilitado!");

        registerCommands();

    }

    @Override
    public void onDisable() {
        // Fecha a conexão com o MongoDB
        mongoManager.close();
        getLogger().info("ZoneSkills desabilitado!");
    }

    public void registerCommands() {
        new SkillAdminCommand(playerDataManager);
        new SkillsCommand(this);
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        perms = rsp.getProvider();
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public MiningSkill getMiningSkill() {
        return miningSkill;
    }

    public Permission getPermissions() {
        return perms;
    }
}
