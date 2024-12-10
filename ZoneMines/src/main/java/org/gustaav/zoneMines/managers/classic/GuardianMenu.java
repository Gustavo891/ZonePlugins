package org.gustaav.zoneMines.managers.classic;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.SellModule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuardianMenu {

    ChestGui chestGui;
    Player player;
    ZoneMines plugin;

    public GuardianMenu(ZoneMines plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.chestGui = new ChestGui(3, "Guardião da Mina:");
        StaticPane pane = new StaticPane(Slot.fromIndex(10), 7, 1);
        loadSell(pane);

        chestGui.setOnTopClick(e -> {
            e.setCancelled(true);
        });

        chestGui.setOnBottomClick(e -> {
            ItemStack item = e.getCurrentItem();
            if(item == null || item.getType() == Material.AIR) {
                e.setCancelled(true);
                return;
            }
            if(!plugin.getSellModule().sell(player, item)) {
                e.setCancelled(true);
            } else {
                e.setCurrentItem(null);
            }
        });
        chestGui.setOnGlobalDrag(e -> {
            e.setCancelled(true);
        });

        chestGui.addPane(pane);
        chestGui.show(player);
    }


   public void loadSell(StaticPane pane) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTllNzdmYWU1MzEzYmFjMTliZjE0NTc3ZDUwMDkzZTQ3MzhlYmQ3MGZkNTRhNGRlMWEyNzQ3NWQwZWM5NTM4ZiJ9fX0=";
        profile.setProperty(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);

        meta.displayName(MiniMessage.miniMessage().deserialize("<color:#a6ffb2>Vender recursos</color>").decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Venda todos os recursos"));
        lore.add(Component.text("§7de seu inventário."));
        lore.add(Component.text(""));
        lore.add(MiniMessage.miniMessage().deserialize("<color:#f2fff0>Clique no item para vende-lo.</color>").decoration(TextDecoration.ITALIC, false));

        meta.lore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item), Slot.fromIndex(0));
    }




}
