package org.gustaav.zoneEnchants.enchantment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class EnchantModel {

    String name;
    String id;
    Enchantment enchantment;
    String desc;
    @Getter
    List<String> conseguir;
    List<EnchantTypes> types;
    int maxLevel;

}
