package org.gustaav.zoneMines.containers;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.SellModel;
import org.gustaav.zoneMines.modules.SellModule;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class LapisSell {

    int lapisVenda = 2;
    int lapisOre = 20;
    int lapisBlock = 18;
    int diamanteVenda = 40;
    int diamanteOre = 360;
    private final ChestGui inventory;
    private final Player player;

    SellModule sellModule;

    public LapisSell(Player player, SellModule sellModule) {
        this.inventory = new ChestGui(3, "Venda dos Minerios:");
        this.player = player;

        inventory.setOnGlobalClick(e -> {
            e.setCancelled(true);});

        this.sellModule = sellModule;

    }

    public void loadSellItems() {
        StaticPane pane = new StaticPane(0, 0, 9, 3);
        loadLapis(pane);
        loadLapisOre(pane);
        loadLapisBlock(pane);
        loadDiamante(pane);
        loadDiamanteOre(pane);
        inventory.addPane(pane);
        inventory.show(player);
    }

    public void loadLapis(StaticPane pane) {
        ItemStack item = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x50ff24)) + "Lapis Lazuli");

        List<String> lore = new ArrayList<>();
        int bonus = checkBonus(player);
        if(bonus > 0) {
            lore.add("§7Bônus adicional de §f" + bonus + "%§7.");
        } else {
            lore.add("§7Nenhum bônus encontrado.");
        }
        lore.add("§r");
        lore.add("§r §8❒ §fVenda p/drop: §2$§a" + lapisVenda + "   §r");
        lore.add("§r");
        if(player.getInventory().contains(Material.LAPIS_LAZULI)) {
            lore.add(ChatColor.of(new Color(0xabff8c)) + "Clique para vender seus minérios.");
        } else {
            lore.add("§cNenhum minério encontrado.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            e.setCancelled(true);
            if(player.getInventory().contains(Material.LAPIS_LAZULI)) {
                sellItem(item);
            }
        }), Slot.fromIndex(11));

    }

    public void loadLapisOre(StaticPane pane) {
        ItemStack item = new ItemStack(Material.LAPIS_ORE);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x50ff24)) + "Minério de Lapis Lazuli");

        List<String> lore = new ArrayList<>();
        int bonus = checkBonus(player);
        if(bonus > 0) {
            lore.add("§7Bônus adicional de §f" + bonus + "%§7.");
        } else {
            lore.add("§7Nenhum bônus encontrado.");
        }
        lore.add("§r");
        lore.add("§r §8❒ §fVenda p/drop: §2$§a" + lapisOre + "   §r");
        lore.add("§r");
        if(player.getInventory().contains(Material.LAPIS_ORE)) {
            lore.add(ChatColor.of(new Color(0xabff8c)) + "Clique para vender seus minérios.");
        } else {
            lore.add("§cNenhum minério encontrado.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            e.setCancelled(true);
            if(player.getInventory().contains(Material.LAPIS_ORE)) {
                sellItem(item);
            }
        }), Slot.fromIndex(12));

    }

    private int checkBonus(Player player) {
        if(player.hasPermission("zoneranks.rank.ouro")) {
            return 10;
        } else if (player.hasPermission("zoneranks.rank.ferro")) {
            return 7;
        } else if (player.hasPermission("zoneranks.rank.carvao")) {
            return 5;
        } else if (player.hasPermission("zoneranks.rank.pedra")) {
            return 0;
        }
        return 0;
    }


    private void sellItem(ItemStack item) {

        int total = player.getInventory().all(item.getType()).values().stream()
                .mapToInt(ItemStack::getAmount)
                .sum();

        for(SellModel module : sellModule.getSellList()) {
            if(module.getItem().getType() == item.getType()) {
                float bonus = 1 + ((float) checkBonus(player) / 100);
                float valorTotal = total * module.getValue() * bonus;
                player.getInventory().remove(module.getItem().getType());
                ZoneMines.getEconomy().depositPlayer(player, valorTotal);
                player.sendMessage("§aVocê vendeu §7x" + SellModule.format(total) + " §f" + module.getTranslation() + " §apor §2$§f" + SellModule.format(valorTotal) + "§a. §7[+" + checkBonus(player) + "%]");
                updateMenu();
                return;
            }
        }
        player.sendMessage("§cNenhum minério encontrado.");
    }

    public void loadLapisBlock(StaticPane pane) {
        ItemStack item = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x50ff24)) + "Bloco de Lapis Lazuli");

        List<String> lore = new ArrayList<>();
        int bonus = checkBonus(player);
        if(bonus > 0) {
            lore.add("§7Bônus adicional de §f" + bonus + "%§7.");
        } else {
            lore.add("§7Nenhum bônus encontrado.");
        }
        lore.add("§r");
        lore.add("§r §8❒ §fVenda p/drop: §2$§a" + lapisBlock + "   §r");
        lore.add("§r");
        if(player.getInventory().contains(Material.LAPIS_BLOCK)) {
            lore.add(ChatColor.of(new Color(0xabff8c)) + "Clique para vender seus minérios.");
        } else {
            lore.add("§cNenhum minério encontrado.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            e.setCancelled(true);
            if(player.getInventory().contains(Material.LAPIS_BLOCK)) {
                sellItem(item);
            }
        }), Slot.fromIndex(13));

    }

    public void loadDiamante(StaticPane pane) {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x50ff24)) + "Diamante");

        List<String> lore = new ArrayList<>();
        int bonus = checkBonus(player);
        if(bonus > 0) {
            lore.add("§7Bônus adicional de §f" + bonus + "%§7.");
        } else {
            lore.add("§7Nenhum bônus encontrado.");
        }
        lore.add("§r");
        lore.add("§r §8❒ §fVenda p/drop: §2$§a" + diamanteVenda + "   §r");
        lore.add("§r");
        if(player.getInventory().contains(Material.DIAMOND)) {
            lore.add(ChatColor.of(new Color(0xabff8c)) + "Clique para vender seus minérios.");
        } else {
            lore.add("§cNenhum minério encontrado.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            e.setCancelled(true);
            if(player.getInventory().contains(Material.DIAMOND)) {
                sellItem(item);
            }
        }), Slot.fromIndex(14));

    }

    public void loadDiamanteOre(StaticPane pane) {
        ItemStack item = new ItemStack(Material.DIAMOND_ORE);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x50ff24)) + "Minério de Diamante");

        List<String> lore = new ArrayList<>();
        int bonus = checkBonus(player);
        if(bonus > 0) {
            lore.add("§7Bônus adicional de §f" + bonus + "%§7.");
        } else {
            lore.add("§7Nenhum bônus encontrado.");
        }
        lore.add("§r");
        lore.add("§r §8❒ §fVenda p/drop: §2$§a" + diamanteOre + "   §r");
        lore.add("§r");
        if(player.getInventory().contains(Material.DIAMOND_ORE)) {
            lore.add(ChatColor.of(new Color(0xabff8c)) + "Clique para vender seus minérios.");
        } else {
            lore.add("§cNenhum minério encontrado.");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            e.setCancelled(true);
            if(player.getInventory().contains(Material.DIAMOND_ORE)) {
                sellItem(item);
            }
        }), Slot.fromIndex(15));

    }

    public void updateMenu() {
        inventory.getPanes().clear();
        StaticPane pane = new StaticPane(0, 0, 9, 3);
        loadLapis(pane);
        loadLapisOre(pane);
        loadLapisBlock(pane);
        loadDiamante(pane);
        loadDiamanteOre(pane);
        inventory.getPanes().add(pane);
        inventory.update();

    }


}
