package org.gustaav.zoneEssential.gui.kits;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneEssential.ZoneEssential;

import java.util.List;

public class KitsGUI {

    ChestGui kitsGui;
    ZoneEssential zoneEssential;
    Player player;

    public KitsGUI(ZoneEssential zoneEssential, Player player) {
        this.zoneEssential = zoneEssential;
        this.kitsGui = new ChestGui(3, "Kits:");
        this.player = player;
        StaticPane pane = new StaticPane(0, 0, 9, 3);

        kitsGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadBasics(pane);
        loadVIPs(pane);
        loadCashShop(pane);

        kitsGui.addPane(pane);
        kitsGui.show(player);

    }

    private void loadBasics(StaticPane pane) {
        ItemStack item = new ItemStack(Material.MINECART);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§aKits Básicos");
        meta.setLore(List.of(
                "§7Receba seu primeiro pacote de",
                "§7itens para iniciar sua jornada.",
                "",
                "§aClique para acessá-los."
        ));
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            player.closeInventory();
            new KitsBasicsGUI(zoneEssential, player);
        }), Slot.fromIndex(11));
    }

    private void loadVIPs(StaticPane pane) {
        ItemStack item = new ItemStack(Material.END_CRYSTAL);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§aKits Premium");
        meta.setLore(List.of(
                "§7Possui um plano VIP? Colete um",
                "§7kit com itens exclusivos.",
                "",
                "§aClique para acessá-los."
        ));
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item), Slot.fromIndex(13));
    }

    private void loadCashShop(StaticPane pane) {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§6Loja de Cash");
        meta.setLore(List.of(
                "§7Adquira itens exclusivos para",
                "§7se tornar mais forte.",
                "",
                "§6Clique para acessá-la."
        ));
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            player.performCommand("shop");
        }), Slot.fromIndex(15));
    }



}
