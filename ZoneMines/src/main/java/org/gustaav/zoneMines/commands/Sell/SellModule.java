package org.gustaav.zoneMines.commands.Sell;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMines.ZoneMines;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellModule {

    ZoneMines plugin;
    private List<UUID> autoSellList = new ArrayList<UUID>();
    private final List<SellModel> sellList = List.of(
            new SellModel(new ItemStack(Material.LAPIS_LAZULI), "Lapis-Lazuli", 2),
            new SellModel(new ItemStack(Material.LAPIS_ORE), "Minérios de Lapis-Lazuli", 30),
            new SellModel(new ItemStack(Material.LAPIS_BLOCK), "Blocos de Lapis-Lazuli", 18),
            new SellModel(new ItemStack(Material.DIAMOND), "Diamantes", 40),
            new SellModel(new ItemStack(Material.DIAMOND_BLOCK), "Blocos de Diamante", 360),
            new SellModel(new ItemStack(Material.DIAMOND_ORE), "Minérios de Diamante", 600)
    );

    public SellModule(ZoneMines plugin) {
        this.plugin = plugin;
        autoSell();
    }

    public List<SellModel> getSellList() {
        return sellList;
    }

    public List<UUID> getAutoSellList() {
        return autoSellList;
    }

    public void setAutoSellList(List<UUID> autoSellList) {
        this.autoSellList = autoSellList;
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

    public boolean sellItems(Player player) {
        int totalAtual = 0;
        float value = 0;
        boolean vendeu = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                for(SellModel model : getSellList()) {
                    if(model.getItem().getType() == item.getType()) {
                        vendeu = true;
                        int total = player.getInventory().all(item.getType()).values().stream()
                                .mapToInt(ItemStack::getAmount)
                                .sum();
                        totalAtual += total;
                        value += model.getValue() * total;
                        player.getInventory().remove(item);
                    }
                }
            }
        }
        if(vendeu) {
            player.sendMessage("§aVocê vendeu §7x" + SellModule.format(totalAtual) + " §aitens por §2$§f" + SellModule.format(value) + "§a.");
            ZoneMines.getEconomy().depositPlayer(player, value);
            return true;
        } else {
            return false;
        }
    }

    public void autoSell() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : autoSellList) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        sellItems(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 100L);
    }


}
