package org.gustaav.zoneArmazem.warehouse.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ArmazemSoldEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Material material;
    private final int amount;
    private final double price;

    public ArmazemSoldEvent(Player player, Material material, int amount, double price) {
        this.player = player;
        this.material = material;
        this.amount = amount;
        this.price = price;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player player() {
        return player;
    }

    public double price() {
        return price;
    }

    public int amount() {
        return amount;
    }

    public Material material() {
        return material;
    }
}
