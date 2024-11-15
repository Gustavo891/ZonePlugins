package org.gustaav.zoneEssential.kits;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitModel {

    private String kitName;
    private String kitType;
    private int delay; // delay do kit em segundos
    private List<ItemStack> listaItens;

    public KitModel(String kitName, String kitType, int delay, List<ItemStack> listaItens) {
        this.kitName = kitName;
        this.kitType = kitType;
        this.delay = delay;
        this.listaItens = listaItens;
    }

    public String getKitName() {
        return kitName;
    }

    public String getKitType() {
        return kitType;
    }

    public int getDelay() {
        return delay;
    }

    public List<ItemStack> getListaItens() {
        return listaItens;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setListaItens(List<ItemStack> listaItens) {
        this.listaItens = listaItens;
    }
}
