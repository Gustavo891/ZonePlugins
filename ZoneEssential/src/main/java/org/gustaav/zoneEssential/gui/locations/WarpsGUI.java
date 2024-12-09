package org.gustaav.zoneEssential.gui.locations;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.google.common.collect.HashMultimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WarpsGUI {

    ChestGui warpsGUI;
    Player player;

    public WarpsGUI(Player player) {
        this.player = player;
        this.warpsGUI = new ChestGui(3, "Localizações:");

        StaticPane pane = new StaticPane(Slot.fromIndex(10), 7, 1);

        loadMines(pane);
        loadTerrains(pane);

        warpsGUI.addPane(pane);
        warpsGUI.show(player);
    }

    public void loadMines(StaticPane pane) {
        ItemStack item = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<color:#9cff66>Minas</color>").decoration(TextDecoration.ITALIC, false));
        List<Component> warps = new ArrayList<>();
        warps.add(MiniMessage.miniMessage().deserialize(
                "<color:#edfff4>Visite as área de mineração para")
                .decoration(TextDecoration.ITALIC, false));
        warps.add(MiniMessage.miniMessage().deserialize(
                        "<color:#edfff4>ganhar suas primeiras moedas.")
                .decoration(TextDecoration.ITALIC, false));
        warps.add(Component.text(""));
        warps.add(Component.text("  §7Acesso: §f/minas"));
        warps.add(Component.text(""));
        warps.add(Component.text("§8Clique para acessa-lo."));
        meta.lore(warps);
        meta.setAttributeModifiers(HashMultimap.create());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            player.performCommand("minas");
        }), Slot.fromIndex(0));

    }

    public void loadTerrains(StaticPane pane) {
        ItemStack item = new ItemStack(Material.BRICKS);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<color:#9cff66>Terrenos</color>").decoration(TextDecoration.ITALIC, false));
        List<Component> warps = new ArrayList<>();
        warps.add(MiniMessage.miniMessage().deserialize(
                        "<color:#edfff4>Conquiste seu primeiro terreno")
                .decoration(TextDecoration.ITALIC, false));
        warps.add(MiniMessage.miniMessage().deserialize(
                        "<color:#edfff4>para construir suas farms.")
                .decoration(TextDecoration.ITALIC, false));
        warps.add(Component.text(""));
        warps.add(Component.text("  §7Acesso: §f/terrenos"));
        warps.add(Component.text(""));
        warps.add(Component.text("§8Clique para acessa-lo."));
        meta.lore(warps);
        meta.setAttributeModifiers(HashMultimap.create());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            player.performCommand("warp terrenos");
        }), Slot.fromIndex(1));

    }


}
