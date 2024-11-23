package org.gustaav.zoneShop.Shop;

import org.bukkit.Material;

public class ShopProduct {

    private String name;
    private Material material;
    private int price;

    public ShopProduct(String name, Material material, int price) {
        this.name = name;
        this.material = material;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
