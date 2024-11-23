package org.gustaav.zoneShop.gui;


import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneShop.Shop.ShopManager;
import org.gustaav.zoneShop.ZoneShop;

import java.util.ArrayList;
import java.util.List;

public class ShopInterface {

    private final Player player;
    private final ZoneShop plugin;

    public ShopInterface(Player player, ZoneShop plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public void openMenu() {
        ChestGui shop = new ChestGui(3, "Loja:");

        StaticPane painel = new StaticPane(1,1,7,2);
        loadCategories(painel);
        shop.addPane(painel);
        shop.show(player);

    }

    public void loadCategories(StaticPane pane) {

        pane.addItem(loadBlocos(), 1, 0);
        pane.addItem(loadFerramentas(), 2, 0);
        pane.addItem(loadPlantacao(), 3, 0);
        pane.addItem(loadUtilitarios(), 4, 0);
        pane.addItem(loadOutros(), 5, 0);
    }

    public GuiItem loadBlocos() {
        ItemStack blocos = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta blocosMeta = blocos.getItemMeta();
        assert blocosMeta != null;
        blocosMeta.setDisplayName("§aBlocos");
        List<String> lores = new ArrayList<>();
        lores.add("§7Clique para acessar.");
        blocosMeta.setLore(lores);
        blocos.setItemMeta(blocosMeta);

        return new GuiItem(blocos, e -> {
            CategoryInterface blocosInterface = new CategoryInterface(player, plugin, this, "Blocos");
            blocosInterface.openBlocos();
            e.setCancelled(true);
        });
    }
    public GuiItem loadFerramentas() {
        ItemStack blocos = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta blocosMeta = blocos.getItemMeta();
        assert blocosMeta != null;
        blocosMeta.setDisplayName("§aFerramentas & Armamentos");
        List<String> lores = new ArrayList<>();
        lores.add("§7Clique para acessar.");
        Multimap<Attribute, AttributeModifier> modifiers = blocosMeta.getAttributeModifiers();
        if(modifiers == null) {
            modifiers = HashMultimap.create();
            blocosMeta.setAttributeModifiers(modifiers);
        }
        blocosMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        blocosMeta.setLore(lores);
        blocos.setItemMeta(blocosMeta);

        return new GuiItem(blocos, e -> {
            CategoryInterface blocosInterface = new CategoryInterface(player, plugin, this, "Ferramentas");
            blocosInterface.openBlocos();
            e.setCancelled(true);
        });
    }
    public GuiItem loadPlantacao() {
        ItemStack blocos = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta blocosMeta = blocos.getItemMeta();
        assert blocosMeta != null;
        blocosMeta.setDisplayName("§aPlantações");
        List<String> lores = new ArrayList<>();
        lores.add("§7Clique para acessar.");
        blocosMeta.setLore(lores);
        blocos.setItemMeta(blocosMeta);

        return new GuiItem(blocos, e -> {
            CategoryInterface blocosInterface = new CategoryInterface(player, plugin, this, "Plantacao");
            blocosInterface.openBlocos();
            e.setCancelled(true);
        });
    }
    public GuiItem loadUtilitarios() {
        ItemStack blocos = new ItemStack(Material.CHEST);
        ItemMeta blocosMeta = blocos.getItemMeta();
        assert blocosMeta != null;
        blocosMeta.setDisplayName("§aUtilitários");
        List<String> lores = new ArrayList<>();
        lores.add("§7Clique para acessar.");
        blocosMeta.setLore(lores);
        blocos.setItemMeta(blocosMeta);

        return new GuiItem(blocos, e -> {
            CategoryInterface blocosInterface = new CategoryInterface(player, plugin, this, "Utilitarios");
            blocosInterface.openBlocos();
            e.setCancelled(true);
        });
    }
    public GuiItem loadOutros() {
        ItemStack blocos = new ItemStack(Material.BONE_MEAL);
        ItemMeta blocosMeta = blocos.getItemMeta();
        assert blocosMeta != null;
        blocosMeta.setDisplayName("§aOutros");
        List<String> lores = new ArrayList<>();
        lores.add("§7Clique para acessar.");
        blocosMeta.setLore(lores);
        blocos.setItemMeta(blocosMeta);

        return new GuiItem(blocos, e -> {
            CategoryInterface blocosInterface = new CategoryInterface(player, plugin, this, "Outros");
            blocosInterface.openBlocos();
            e.setCancelled(true);
        });
    }

}
