package org.gustaav.zonePacotes.packages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter @AllArgsConstructor
public class RewardModel {
    String id;
    String displayName;
    String rarity;
    String command;
    List<String> lore;
    Material material;
    String head; //Caso seja uma cabeça
    int weight;

}
