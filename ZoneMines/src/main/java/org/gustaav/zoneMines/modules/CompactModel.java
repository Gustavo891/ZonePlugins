package org.gustaav.zoneMines.modules;

import org.bukkit.inventory.ItemStack;

public class CompactModel {

    private ItemStack base;
    private ItemStack result;
    private int recipe;

    public CompactModel(ItemStack base, int recipe, ItemStack result) {
        this.base = base;
        this.recipe = recipe;
        this.result = result;
    }

    public ItemStack getBase() {
        return base;
    }

    public void setBase(ItemStack base) {
        this.base = base;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }
}
