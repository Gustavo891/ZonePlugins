package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SellModule {

    private static final List<SellModel> sellList = List.of(
            new SellModel(new ItemStack(Material.LAPIS_LAZULI), "Lapis-Lazuli", 2),
            new SellModel(new ItemStack(Material.LAPIS_ORE), "Minérios de Lapis-Lazuli", 30),
            new SellModel(new ItemStack(Material.LAPIS_BLOCK), "Blocos de Lapis-Lazuli", 18),
            new SellModel(new ItemStack(Material.DIAMOND), "Diamantes", 40),
            new SellModel(new ItemStack(Material.DIAMOND_BLOCK), "Blocos de Diamante", 360),
            new SellModel(new ItemStack(Material.DIAMOND_ORE), "Minérios de Diamante", 600)
    );

    public static List<SellModel> getSellList() {
        return sellList;
    }

    public static String format(double amount) {
        if (amount < 1000) {
            return String.format("%.0f", amount);
        } else if (amount < 1_000_000) {
            return String.format("%.2fk", amount / 1000);
        } else if (amount < 1_000_000_000) {
            return String.format("%.2fM", amount / 1_000_000);
        } else if (amount < 1_000_000_000_000L) {
            return String.format("%.2fB", amount / 1_000_000_000);
        } else {
            return String.format("%.2fT", amount / 1_000_000_000_000L);
        }
    }

}
