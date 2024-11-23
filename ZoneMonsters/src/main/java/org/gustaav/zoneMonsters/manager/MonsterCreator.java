package org.gustaav.zoneMonsters.manager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneMonsters.models.MonsterModel;
import org.gustaav.zoneMonsters.models.MonsterTypes;
import org.gustaav.zoneMonsters.models.RewardModel;

import java.util.List;

public class MonsterCreator {

    MonsterManager manager;

    MonsterModel profundo = new MonsterModel(
            "Profundo",
            "profundo",
            MonsterTypes.NORMAL,
            EntityType.DROWNED,
            100,
            List.of(
                    new RewardModel(
                            "Dinheiro",
                            "money add %player% 10000",
                            70),
                    new RewardModel(
                            "Afiada 3",
                            "givebook %player% afiada 3",
                            40),
                    new RewardModel(
                            "Afiada 4",
                            "givebook %player% afiada 4",
                            10),
                    new RewardModel(
                            "Durabilidade 2",
                            "givebook %player% durabilidade 2",
                            30)
            ),
            new ItemStack(Material.DROWNED_SPAWN_EGG)
    );

    public MonsterCreator(MonsterManager manager) {
        this.manager = manager;
    }

    public void loadMonsters() {
        manager.getMonsters().add(profundo);
    }



}
