package org.gustaav.zoneSkills.Database;

import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final MongoManager mongoManager;
    private static final double XP_MULTIPLIER = 1.2;

    private final Map<UUID, Document> playerCache = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin, MongoManager mongoManager) {
        this.mongoManager = mongoManager;

        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllData();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 100L);
    }

    public void loadPlayerData(UUID uuid) {
        Document playerData = mongoManager.getPlayerData(uuid);
        playerCache.put(uuid, playerData);
    }

    public void unloadPlayerData(UUID uuid) {
        Document playerData = playerCache.remove(uuid);
        if (playerData != null) {
            mongoManager.updatePlayerData(uuid, playerData);
        }
    }

    public Document getSkillData(UUID uuid, String skillName) {
        Document playerData = getPlayerData(uuid);
        Document skills = (Document) playerData.get("skills");
        return (Document) skills.getOrDefault(skillName, new Document("level", 1).append("xp", 0.0));
    }

    private Document getPlayerData(UUID uuid) {
        return playerCache.computeIfAbsent(uuid, id -> mongoManager.getPlayerData(uuid));
    }

    public boolean addXp(UUID uuid, String skillName, double xp) {
        Document playerData = getPlayerData(uuid);
        Document skills = (Document) playerData.get("skills");

        Document skillData = (Document) skills.getOrDefault(skillName, new Document("level", 1).append("xp", 0.0));
        skills.put(skillName, skillData);

        double currentXp = skillData.getDouble("xp");
        skillData.put("xp", currentXp + xp);

        int level = skillData.getInteger("level");
        double xpRequired = getXpRequiredForLevel(level);

        if (currentXp + xp >= xpRequired) {
            levelUp(uuid, skillName, skillData);
            return true;
        }
        return false;
    }

    private void levelUp(UUID uuid, String skillName, Document skillData) {
        int currentLevel = skillData.getInteger("level");
        skillData.put("level", currentLevel + 1);
        skillData.put("xp", 0.0);

    }

    public int getLevel(UUID uuid, String skillName) {
        Document skillData = getSkillData(uuid, skillName);
        return skillData.getInteger("level");
    }

    public double getXp(UUID uuid, String skillName) {
        Document skillData = getSkillData(uuid, skillName);
        return skillData.getDouble("xp");
    }

    public double getXpRequiredForLevel(int level) {
        return 1000 * Math.pow(XP_MULTIPLIER, level - 1);
    }

    private void saveAllData() {
        for (Map.Entry<UUID, Document> entry : playerCache.entrySet()) {
            mongoManager.updatePlayerData(entry.getKey(), entry.getValue());
        }
    }
}
