package org.gustaav.zoneSkills.Database;

import org.gustaav.zoneSkills.skills.SkillType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final Map<SkillType, SkillData> skills;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.skills = new HashMap<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public SkillData getSkillData(SkillType skillName) {
        return skills.computeIfAbsent(skillName, k -> new SkillData(1, 0)); // Padrão: nível 1 e xp 0
    }

    public void setSkillData(SkillType skillName, int level, int xp) {
        skills.put(skillName, new SkillData(level, xp));
    }

    public Map<SkillType, SkillData> getAllSkills() {
        return skills;
    }
}
