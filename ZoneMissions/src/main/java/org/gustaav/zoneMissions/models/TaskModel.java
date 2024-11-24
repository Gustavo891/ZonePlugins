package org.gustaav.zoneMissions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter @Setter @AllArgsConstructor
public class TaskModel {

    String name;
    String description;
    int amount;
    TaskTypes type;

    String bossId; // apenas usado caso o tipo seja BOSS_MATADO
    Material material; // null caso seja qualquer material

}
