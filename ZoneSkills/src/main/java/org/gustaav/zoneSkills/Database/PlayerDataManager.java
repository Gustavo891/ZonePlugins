package org.gustaav.zoneSkills.Database;

import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneSkills.skills.SkillType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final MongoManager mongoManager;
    private static final double XP_MULTIPLIER = 1.1;

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

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
        Document document = mongoManager.getPlayerData(uuid);
        PlayerData playerData = new PlayerData(uuid);

        Document skills = (Document) document.get("skills");
        for (String skillName : skills.keySet()) {
            Document skillData = (Document) skills.get(skillName);
            int level = skillData.getInteger("level");
            int xp = skillData.getInteger("xp");

            playerData.setSkillData(SkillType.valueOf(skillName), level, xp);
        }

        playerDataMap.put(uuid, playerData);
    }

    public int getTotalLevel(UUID uuid) {
        int value = 0;
        for(SkillType skillType : SkillType.values()) {
            value += getLevel(uuid, skillType);
        }
        return value;
    }

    public SkillData getSkillData(UUID uuid, SkillType skillName) {
        PlayerData playerData = playerDataMap.get(uuid);
        if (playerData != null) {
            return playerData.getSkillData(skillName);
        }
        return new SkillData(1, 0); // Retorna um novo SkillData caso não encontre
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, id -> {
            loadPlayerData(uuid);
            return getPlayerData(uuid);
        });
    }

    public boolean addXp(UUID uuid, SkillType skillName, double xp) {
        PlayerData playerData = playerDataMap.get(uuid);

        if (playerData == null)
            return false;

        SkillData skillData = playerData.getSkillData(skillName);

        double currentXp = skillData.getXp();
        skillData.setXp((int) (currentXp + xp));

        int level = skillData.getLevel();
        double xpRequired = getXpRequiredForLevel(level);

        if (currentXp + xp >= xpRequired) {
            levelUp(uuid, skillName);
            return true;
        }
        return false;
    }


    private void levelUp(UUID uuid, SkillType skillName) {
        PlayerData playerData = playerDataMap.get(uuid);
        if (playerData != null) {
            SkillData skillData = playerData.getSkillData(skillName);
            skillData.setLevel(skillData.getLevel() + 1);
            skillData.setXp(0); // Reset XP após level up
        }
    }

    public int getLevel(UUID uuid, SkillType skillName) {
        PlayerData playerData = playerDataMap.get(uuid);
        SkillData skillData = playerData != null ? playerData.getSkillData(skillName) : new SkillData(1, 0);
        return skillData.getLevel();
    }

    public void setLevel(UUID uuid, SkillType skillName, int level) {
        PlayerData playerData = playerDataMap.get(uuid);
        SkillData skillData = playerData.getSkillData(skillName);
        skillData.setLevel(level);
        skillData.setXp(0);
    }

    public double getXp(UUID uuid, SkillType skillName) {
        PlayerData playerData = playerDataMap.get(uuid);
        SkillData skillData = playerData != null ? playerData.getSkillData(skillName) : new SkillData(1, 0);
        return skillData.getXp();
    }

    public double getXpRequiredForLevel(int level) {
        return 1000 * Math.pow(XP_MULTIPLIER, level - 1);
    }

    private void saveAllData() {
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            UUID uuid = entry.getKey();
            PlayerData playerData = entry.getValue();

            saveData(uuid, playerData);
        }
    }

    public void unloadPlayerData(UUID uuid) {
        PlayerData playerData = playerDataMap.remove(uuid);
        if (playerData != null) {
            // Converte PlayerData para Document e salva no MongoDB no formato desejado
            saveData(uuid, playerData);
        }
    }

    private void saveData(UUID uuid, PlayerData playerData) {
        Document skills = new Document();
        for (Map.Entry<SkillType, SkillData> entry : playerData.getAllSkills().entrySet()) {
            SkillType skillName = entry.getKey();
            SkillData skillData = entry.getValue();
            skills.put(skillName.toString(), new Document("level", skillData.getLevel())
                    .append("xp", skillData.getXp()));
        }
        Document document = new Document("uuid", playerData.getUuid().toString())
                .append("skills", skills);

        mongoManager.updatePlayerData(uuid, document);
    }
}
