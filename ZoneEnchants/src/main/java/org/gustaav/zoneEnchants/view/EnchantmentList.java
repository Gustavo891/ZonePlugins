package org.gustaav.zoneEnchants.view;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import org.gustaav.zoneEnchants.enchantment.models.EnchantModel;
import org.gustaav.zoneEnchants.enchantment.models.EnchantTypes;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentList {

    EnchantConfig enchantConfig;
    EnchantGUI beforeGui;
    Player player;
    ChestGui enchantGui;

    public EnchantmentList(EnchantConfig enchantConfig, Player player, EnchantGUI beforeGui) {
        this.enchantConfig = enchantConfig;
        this.player = player;
        this.beforeGui = beforeGui;

        loadGui();
    }

    public void loadGui() {
        enchantGui = new ChestGui(5, "Lista de encantamentos:");
        PaginatedPane enchantments = new PaginatedPane(Slot.fromIndex(10), 7, 3);
        StaticPane navBar = new StaticPane(Slot.fromIndex(36), 9, 1);
        enchantGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadEnchantments(enchantments);
        loadNavBar(enchantments, navBar);

        enchantGui.addPane(enchantments);
        enchantGui.addPane(navBar);

        enchantGui.show(player);

    }

    public void loadEnchantments(PaginatedPane enchantments) {

        List<GuiItem> items = new ArrayList<>();
        for (EnchantModel enchantment : enchantConfig.getEnchantments()) {
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize(String.format("<#50FF63>%s <dark_gray>[Máx. %s]", enchantment.getName(), enchantment.getMaxLevel()))
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            String desc = enchantment.getDesc();  // Exemplo: "Aumenta a velocidade de \nmineração de seu item."
            String[] lines = desc.split("\n");
            for (String line : lines) {
                lore.add(Component.text("§7" + line));  // Adiciona cada linha separada com §7
            }
            lore.add(Component.text("§r"));
            lore.add(MiniMessage.miniMessage().deserialize("  <#50FF63>Como conseguir?").decoration(TextDecoration.ITALIC, false));
            for(String forma : enchantment.getConseguir()) {
                lore.add(Component.text("§8  ❒ §7" + forma));
            }
            lore.add(Component.text("§r"));
            lore.add(MiniMessage.miniMessage().deserialize("  <#50FF63>Aplicável em:").decoration(TextDecoration.ITALIC, false));
            if (enchantment.getName().equalsIgnoreCase("Durabilidade")) {
                lore.add(Component.text("  §8◆ §fTodos itens."));
            } else {
                for (EnchantTypes enchant : enchantment.getTypes()) {
                    lore.add(Component.text("  §8◆ §f" + enchant.getValue()));
                }
            }
            lore.add(Component.text(""));
            meta.lore(lore);
            item.setItemMeta(meta);

            items.add(new GuiItem(item));
        }
        enchantments.populateWithGuiItems(items);

    }

    public void loadNavBar(PaginatedPane enchantments, StaticPane navBar) {

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta meta = arrow.getItemMeta();
        meta.displayName(Component.text("§eVoltar"));
        arrow.setItemMeta(meta);

        navBar.addItem(new GuiItem(arrow, e -> {
            if (enchantments.getPage() > 0) {
                enchantments.setPage(enchantments.getPage() - 1);
                enchantGui.update();
            } else {
                beforeGui.loadGui();
            }
        }), Slot.fromIndex(2));


        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextmeta = next.getItemMeta();
        nextmeta.displayName(Component.text("§ePróximo"));
        next.setItemMeta(nextmeta);

        navBar.addItem(new GuiItem(next, e -> {
            if (enchantments.getPage() < enchantments.getPages() - 1) {
                enchantments.setPage(enchantments.getPage() + 1);
                enchantGui.update();
            }
        }), Slot.fromIndex(6));

    }

}
