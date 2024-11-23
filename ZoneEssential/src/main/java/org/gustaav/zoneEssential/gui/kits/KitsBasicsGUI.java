package org.gustaav.zoneEssential.gui.kits;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.kits.KitModel;

import java.util.ArrayList;
import java.util.List;

public class KitsBasicsGUI {

    ChestGui kitsBasics;
    ZoneEssential zoneEssential;
    Player player;

    public KitsBasicsGUI(ZoneEssential zoneEssential, Player player) {
        this.zoneEssential = zoneEssential;
        this.kitsBasics = new ChestGui(4, "Kits: Básicos");
        this.player = player;
        StaticPane pane = new StaticPane(0, 0, 9, 4);

        kitsBasics.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadMembroDiario(pane);
        loadMinerador(pane);
        voltar(pane);

        kitsBasics.addPane(pane);
        kitsBasics.show(player);

    }

    private void loadMembroDiario(StaticPane pane) {
        List<KitModel> kits = zoneEssential.getKitManager().getKits();
        KitModel selectedKit = kits.stream()
                .filter(kit -> kit.getKitType().equalsIgnoreCase("membrodiario"))
                .findFirst()
                .orElse(null);

        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§aMembro Diário");

        assert selectedKit != null;
        List<String> lore = new ArrayList<>(List.of(
                "§7Colete seu primeiro armamento",
                "§7para se aventurar no servidor.",
                "",
                "  §fTempo: §71 dia",
                ""
        ));
        getSelected(selectedKit, item, meta, lore);

        pane.addItem(new GuiItem(item, e -> {
            player.performCommand("kit membrodiario");
            player.closeInventory();
        }), Slot.fromIndex(12));
    }

    private void loadMinerador(StaticPane pane) {
        List<KitModel> kits = zoneEssential.getKitManager().getKits();
        KitModel selectedKit = kits.stream()
                .filter(kit -> kit.getKitType().equalsIgnoreCase("minerador"))
                .findFirst()
                .orElse(null);

        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§aMinerador");

        assert selectedKit != null;
        List<String> lore = new ArrayList<>(List.of(
                "§7Pegue sua primeira picareta",
                "§7e comece sua mineração.",
                "",
                "  §fTempo: §71 dia",
                ""
        ));
        getSelected(selectedKit, item, meta, lore);

        pane.addItem(new GuiItem(item, e -> {
            player.performCommand("kit minerador");
            player.closeInventory();
        }), Slot.fromIndex(14));
    }

    private void getSelected(KitModel selectedKit, ItemStack item, ItemMeta meta, List<String> lore) {
        if (!zoneEssential.getKitManager().getKitCooldown(player.getUniqueId(), selectedKit, false)) {
            lore.add("§cAguarde para coletar esse kit novamente.");
        } else {
            lore.add("§aClique para coletar o kit.");
        }
        meta.setLore(lore);

        Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
        if (modifiers == null) {
            modifiers = HashMultimap.create();
            meta.setAttributeModifiers(modifiers);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }


    private void voltar(StaticPane pane) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§cVoltar");
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            player.closeInventory();
            player.performCommand("kits");
        }), Slot.fromIndex(31));

    }
}
