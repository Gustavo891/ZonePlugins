package org.gustaav.zoneEnchants.enchantment;

import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class EnchantModel {

    String name;
    String id;
    Enchantment enchantment;
    String desc;
    List<EnchantTypes> types;
    int maxLevel;

    public EnchantModel(String name, String id, Enchantment enchantment, String desc, List<EnchantTypes> types, int maxLevel) {
        this.name = name;
        this.id = id;
        this.enchantment = enchantment;
        this.desc = desc;
        this.types = types;
        this.maxLevel = maxLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public List<EnchantTypes> getTypes() {
        return types;
    }

    public void setTypes(List<EnchantTypes> types) {
        this.types = types;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }
}
