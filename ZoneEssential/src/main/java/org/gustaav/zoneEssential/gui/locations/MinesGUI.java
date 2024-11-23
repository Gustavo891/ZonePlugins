package org.gustaav.zoneEssential.gui.locations;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinesGUI {

    ChestGui minesGUI;
    Player player;

    public MinesGUI(Player player) {
        minesGUI = new ChestGui(3, "Locais de mineração:");
        this.player = player;
        StaticPane pane = new StaticPane(0, 0, 9, 3);
        loadLapisMine(pane);
        minesGUI.addPane(pane);

        minesGUI.show(player);
    }

    public void loadLapisMine(StaticPane pane) {

        ItemStack lapis = new ItemStack(Material.LAPIS_ORE);
        ItemMeta meta = lapis.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eNormal");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§fAcesso rápido: §7/warp mina");
        lore.add("§fJogadores: §7" + PlaceholderAPI.setPlaceholders(player, "zonemines_normal_players"));
        lore.add("");
        lore.add("§eClique para teleportar.");
        meta.setLore(lore);

        pane.addItem(new GuiItem(lapis, e -> {
            player.performCommand("warp mina");
        }), Slot.fromIndex(13));

    }

}
