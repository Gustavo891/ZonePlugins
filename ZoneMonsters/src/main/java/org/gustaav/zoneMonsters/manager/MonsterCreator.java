package org.gustaav.zoneMonsters.manager;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneMonsters.models.MonsterModel;
import org.gustaav.zoneMonsters.models.MonsterTypes;
import org.gustaav.zoneMonsters.models.RewardModel;
import revxrsal.commands.Lamp;

import java.util.List;

public class MonsterCreator {

    MonsterManager manager;

    MonsterModel saqueador = new MonsterModel(
            "Saqueador",
            "saqueador",
            MonsterTypes.NORMAL,
            EntityType.PILLAGER,
            1000,
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
    MonsterModel mumia = new MonsterModel(
            "Múmia",
            "mumia",
            MonsterTypes.NORMAL,
            EntityType.HUSK,
            5000,
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
            new ItemStack(Material.HUSK_SPAWN_EGG)
    );
    MonsterModel guardiaodopatano = new MonsterModel(
            "Guardião do Pântano",
            "pantano",
            MonsterTypes.NORMAL,
            EntityType.BOGGED,
            10000,
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
            new ItemStack(Material.BOGGED_SPAWN_EGG)
    );
    MonsterModel brutos = new MonsterModel(
            "Brutos",
            "brutos",
            MonsterTypes.INCOMUM,
            EntityType.PIGLIN_BRUTE,
            25000,
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
            new ItemStack(Material.PIGLIN_BRUTE_SPAWN_EGG)
    );
    MonsterModel hoglin = new MonsterModel(
            "Hoglin",
            "hoglin",
            MonsterTypes.INCOMUM,
            EntityType.HOGLIN,
            50000,
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
            new ItemStack(Material.HOGLIN_SPAWN_EGG)
    );
    MonsterModel devastador = new MonsterModel(
            "Devastador",
            "devastador",
            MonsterTypes.RARO,
            EntityType.RAVAGER,
            100000,
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
            new ItemStack(Material.RAVAGER_SPAWN_EGG)
    );

    public MonsterCreator(MonsterManager manager) {
        this.manager = manager;
    }

    public void loadMonsters() {
        manager.getMonsters().addAll(List.of(saqueador, mumia, guardiaodopatano, brutos, hoglin, devastador));
    }


}
