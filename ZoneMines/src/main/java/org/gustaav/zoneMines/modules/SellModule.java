package org.gustaav.zoneMines.modules;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.utils.MessageUtil;

import java.util.*;
import java.util.stream.Collectors;

public class SellModule {

    List<ProductModel> items = List.of(
            new ProductModel("cobre_1", "Fragmentos de Cobre", 5)
    );

    ZoneMines plugin;
    @Setter
    @Getter
    private List<UUID> autoSellList = new ArrayList<UUID>();
    @Getter
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

    public boolean sell(Player player, ItemStack item) {

        if(item.getItemMeta().getPersistentDataContainer().has(plugin.getDropIdentifier())) {
            String type = item.getItemMeta().getPersistentDataContainer().get(plugin.getDropIdentifier(), PersistentDataType.STRING);
            switch (type) {
                case "cobre_1":
                    int amount = item.getAmount();
                    Optional<ProductModel> productModel = items.stream().filter(produto -> produto.identifier().equalsIgnoreCase(type)).findAny();
                    if (productModel.isPresent()) {
                        ProductModel produto = productModel.get();
                        double lucro = produto.value() * amount;
                        ZoneMines.getEconomy().depositPlayer(player, lucro);
                        player.sendMessage(String.format("§aVocê vendeu §7x%s §f%s §apor um total de §2$§f%s§a.", amount, produto.translation(), format(lucro)));
                        return true;
                    } else {
                        return false;
                    }
                case null, default:
                    return false;
            }
        }
        return false;
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
