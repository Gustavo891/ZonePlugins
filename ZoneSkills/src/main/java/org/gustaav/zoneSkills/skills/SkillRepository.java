package org.gustaav.zoneSkills.skills;

import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.skills.list.MiningSkill;

public class SkillRepository {

    MiningSkill miningSkill;

    public SkillRepository(ZoneSkills plugin, PlayerDataManager playerDataManager) {
        this.miningSkill = new MiningSkill(plugin, playerDataManager);
    }

    public MiningSkill getMiningSkill() {
        return miningSkill;
    }
}
