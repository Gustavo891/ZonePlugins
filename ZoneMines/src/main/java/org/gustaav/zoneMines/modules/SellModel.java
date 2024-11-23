package org.gustaav.zoneMines.modules;

import org.bukkit.inventory.ItemStack;

public class SellModel {

   private ItemStack item;
   private int value;
   private String translation;

    public SellModel(ItemStack item, String translation, int value) {
        this.item = item;
        this.translation = translation;
        this.value = value;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
