package org.gustaav.zoneMonsters.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RewardModel {

    private String name;
    private String commandToExecute;
    private double chance;

}
