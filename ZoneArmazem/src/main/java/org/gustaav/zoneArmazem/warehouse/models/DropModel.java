package org.gustaav.zoneArmazem.warehouse.models;

import org.bukkit.Material;

public class DropModel {

    private Material drop;
    private double value;
    private String name;

    public DropModel(Material drop, String name, double value) {
        this.drop = drop;
        this.name = name;
        this.value = value;
    }

    public Material getDrop() {
        return drop;
    }

    public void setDrop(Material drop) {
        this.drop = drop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
