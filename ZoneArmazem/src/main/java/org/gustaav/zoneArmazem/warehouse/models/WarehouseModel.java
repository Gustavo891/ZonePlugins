package org.gustaav.zoneArmazem.warehouse.models;

import com.plotsquared.core.plot.Plot;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

public class WarehouseModel {

    private Plot plot;
    private Boolean mode;
    private int nivel;
    private final Map<Material, Integer> drops = new HashMap<>();

    public WarehouseModel(Plot plot, int nivel, Boolean mode) {
        this.plot = plot;
        this.nivel = nivel;
        this.mode = mode;
    }

    public Plot getPlotId() {
        return plot;
    }

    public void setPlotId(Plot plotId) {
        this.plot = plotId;
    }

    public Boolean getMode() {
        return mode;
    }

    public void setMode(Boolean mode) {
        this.mode = mode;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Map<Material, Integer> getDrops() {
        return drops;
    }

    public void setDrops(Map<Material, Integer> drops) {
        this.drops.clear();
        this.drops.putAll(drops);
    }

    public void addDrop(Material material, int amount) {
        if (drops.containsKey(material)) {
            drops.put(material, drops.get(material) + amount);
        } else {
            drops.put(material, amount);
        }
    }

    public void removeDrop(Material material) {
        drops.remove(material);
    }

    public void clearDrops() {
        drops.clear();
    }
}
