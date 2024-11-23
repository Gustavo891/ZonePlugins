package org.gustaav.zoneMines.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.utils.MessageUtil;

import java.util.*;
import java.util.stream.Collectors;

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

    public boolean sellItems(Player player, int index, List<ItemStack> items) {
        int totalAtual = 0;
        float value = 0;
        boolean vendeu = false;

        // Definir os itens a serem vendidos com base no índice
        List<ItemStack> itensParaVender;
        if (index == 1) {
            itensParaVender = Arrays.asList(player.getInventory().getContents());
        } else if (index == 2) {
            itensParaVender = items;
        } else {
            return false; // Caso o índice não seja 1 nem 2, retorna false
        }

        // Loop para percorrer os itens a serem vendidos
        for (ItemStack item : itensParaVender) {
            if (item != null) {
                for (SellModel model : getSellList()) {
                    if (model.getItem().getType() == item.getType()) {
                        vendeu = true;
                        int total = (index == 1) ? player.getInventory().all(item.getType()).values().stream().mapToInt(ItemStack::getAmount).sum() : item.getAmount();
                        totalAtual += total;
                        value += model.getValue() * total;

                        // Se for venda do inventário, remove o item
                        if (index == 1) {
                            player.getInventory().remove(item);
                        }
                    }
                }
            }
        }

        // Exibe a mensagem de sucesso ou erro
        if (vendeu) {
            if(index == 1) {
                player.sendMessage("§aVocê vendeu §7x" + SellModule.format(totalAtual) + " §aitens por §2$§f" + SellModule.format(value) + "§a.");
            } else {
                MessageUtil.sendFormattedMessage(player, String.format("${Colors.PURPLE_DARK}<b>Bomba </b><dark_gray>✦ <white>Você vendeu ${Colors.PURPLE_LIGHT}%s <white>itens por um total de <dark_green>$<green>%s<white>.", SellModule.format(totalAtual), SellModule.format(value)));
            }
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
                        List<ItemStack> inventoryItems = Arrays.stream(player.getInventory().getContents())
                                .filter(Objects::nonNull) // Remove espaços vazios
                                .collect(Collectors.toList());
                        sellItems(player, 1, inventoryItems);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 100L);
    }


}
