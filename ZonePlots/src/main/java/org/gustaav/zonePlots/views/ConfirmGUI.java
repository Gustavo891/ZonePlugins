package org.gustaav.zonePlots.views;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfirmGUI {

    private ChestGui confirmGui;
    private Plot plot;
    private Player player;
    private PlotGUI plotGUI;

    public ConfirmGUI(Plot plot, Player player, PlotGUI plotGUI) {
        this.confirmGui = new ChestGui(3, "Confirmar ação:");
        this.plot = plot;
        this.player = player;
        this.plotGUI = plotGUI;

        StaticPane pane = new StaticPane(Slot.fromIndex(0), 9, 3);

        confirmGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadConfirm(pane);
        loadCancel(pane);
        confirmGui.addPane(pane);
        confirmGui.show(player);

    }

    public void loadConfirm(StaticPane pane) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTM5MTIwM2QzNzUyYWZjNzFkOGY4Y2IwZGE4ZGJjN2YxM2EzYmFhZmUwYmY1ZjkxMWNiMjFjMzgzNDFmODdkYiJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#009206>Confirmar ação")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Clique para liberar o terreno."));
        meta.lore(lore);
        item.setItemMeta(meta);
        pane.addItem(new GuiItem(item, e -> {
            PlotArea plotArea = plot.getArea();
            if (plotArea != null) {
                plotArea.getBasePlots().remove(plot);
                
                player.sendMessage("§aTerreno liberado com sucesso.");
                player.getInventory().close();
            }
        }), Slot.fromIndex(12));

    }

    public void loadCancel(StaticPane pane) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhkNDA5MzUyNzk3NzFhZGM2MzkzNmVkOWM4NDYzYWJkZjVjNWJhNzhkMmU4NmNiMWVjMTBiNGQxZDIyNWZiIn19fQ==";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#AA0026>Cancelar ação")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Clique para não liberar o terreno."));
        meta.lore(lore);
        item.setItemMeta(meta);
        pane.addItem(new GuiItem(item, e -> {
            plotGUI.loadGui();
        }), Slot.fromIndex(14));

    }

}
