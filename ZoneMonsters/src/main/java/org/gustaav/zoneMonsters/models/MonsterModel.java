package org.gustaav.zoneMonsters.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MonsterModel {

    private String name;
    private String id;
    private MonsterTypes monsterType;
    private EntityType monsterEntity;
    private int health;
    private List<RewardModel> rewards;
    private ItemStack monsterItem;

}
