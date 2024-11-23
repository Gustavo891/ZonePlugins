package org.gustaav.zonePlots.views;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.plotsquared.core.command.Confirm;
import com.plotsquared.core.plot.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.gustaav.zoneArmazem.ArmazemAPI;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;
import org.gustaav.zonePlots.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Double.NaN;

public class PlotGUI {

    ChestGui plotGui;
    Plot plot;
    Player player;

    public PlotGUI(Plot plot, Player player) {
        this.plot = plot;
        this.player = player;
    }

    public void loadGui() {
        plotGui = new ChestGui(3, "Menu do terreno:");
        StaticPane plotPane = new StaticPane(Slot.fromIndex(0), 9, 3);

        loadInfo(plotPane);
        loadTrusted(plotPane);
        loadDeny(plotPane);
        loadArmazem(plotPane);
        loadDelete(plotPane);
        plotGui.addPane(plotPane);

        plotGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        plotGui.show(player);
    }


    public void loadInfo(StaticPane plotPane) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0OWI5MzE4ZTMzMTU4ZTY0YTQ2YWIwZGUxMjFjM2Q0MDAwMGUzMzMyYzE1NzQ5MzJiM2M4NDlkOGZhMGRjMiJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#076800>Terreno de <#FCFAEE>" + Bukkit.getOfflinePlayer(plot.getOwner()).getName())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Veja todas informações do terreno."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§f  Membros: §7" + plot.getMembers().size()));
        lore.add(Component.text("§f  Confiáveis: §7" + plot.getTrusted().size()));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§f  Avaliação: §e" + getRating(plot.getAverageRating())));
        lore.add(Component.text("§r"));
        meta.lore(lore);
        item.setItemMeta(meta);

        plotPane.addItem(new GuiItem(item), Slot.fromIndex(10));

    }

    public void loadTrusted(StaticPane plotPane) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEwN2E2OGZjMTQ5OTcxOTIzZmZhZWM3NjA2ZDhhMTllNmQyZDkwZWU4NDczZjBmZjZiMTMxNDk3ZjU1ZWNjZSJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#076800>Membros confiáveis")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Veja e administre os jogadores"));
        lore.add(Component.text("§7confiáveis de seu terreno."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§f  Total: §7" + plot.getTrusted().size()));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§aClique para acessar."));
        meta.lore(lore);
        item.setItemMeta(meta);
        plotPane.addItem(new GuiItem(item, e -> {
            MembrosGUI membrosGUI = new MembrosGUI(plot, player, this);
        }), Slot.fromIndex(12));

    }

    public void loadDeny(StaticPane plotPane) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJlMmJmNmMxZWMzMzAyNDc5MjdiYTYzNDc5ZTU4NzJhYzY2YjA2OTAzYzg2YzgyYjUyZGFjOWYxYzk3MTQ1OCJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#076800>Jogadores proibídos")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Veja os jogadores que estão"));
        lore.add(Component.text("§7proibidos de entrar no terreno."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§f  Total: §7" + plot.getDenied().size()));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§aClique para acessar."));
        meta.lore(lore);
        item.setItemMeta(meta);
        plotPane.addItem(new GuiItem(item, e -> {
            DeniedGUI membrosGUI = new DeniedGUI(plot, player, this);
        }), Slot.fromIndex(13));
    }

    public void loadArmazem(StaticPane plotPane) {
        ItemStack armazem = new ItemStack(Material.BARREL);
        ItemMeta armazemMeta = armazem.getItemMeta();

        WarehouseModel warehouseModel = ArmazemAPI.getInstance().getWarehouse(plot);
        int total = ArmazemAPI.getInstance().getBase().getCapacidade(warehouseModel.getNivel());
        int capacidade = ArmazemAPI.getInstance().getBase().getTotal(warehouseModel);

        double percentual = (total > 0) ? ((double) capacidade / total) * 100 : 0;

        Component component = MiniMessage.miniMessage().deserialize("<#076800>Armazém do terreno")
                .decoration(TextDecoration.ITALIC, false);
        armazemMeta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Veja os drops que suas"));
        lore.add(Component.text("§7plantações renderam."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§f  Capacidade: §7" + NumberUtils.format(capacidade)));
        lore.add(Component.text("§f    " + NumberUtils.barraProgresso(percentual)));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§aClique para acessa-lo."));
        armazemMeta.lore(lore);
        armazem.setItemMeta(armazemMeta);

        plotPane.addItem(new GuiItem(armazem, e -> {
            player.performCommand("armazem");
        }), Slot.fromIndex(14));
    }

    public void loadDelete(StaticPane plotPane) {

        ItemStack armazem = new ItemStack(Material.RED_DYE);
        ItemMeta armazemMeta = armazem.getItemMeta();

        Component component = MiniMessage.miniMessage().deserialize("<#AA032C>Liberar o terreno")
                .decoration(TextDecoration.ITALIC, false);
        armazemMeta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Cuidado, ao liberar o terreno, qualquer"));
        lore.add(Component.text("§7jogador poderá reinvindica-lo para si mesmo."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§cClique para iniciar o processo."));
        armazemMeta.lore(lore);
        armazem.setItemMeta(armazemMeta);

        plotPane.addItem(new GuiItem(armazem, e -> {
            ConfirmGUI confirmGUI = new ConfirmGUI(plot, player, this);
        }), Slot.fromIndex(16));

    }

    public String getRating(double rating) {
        if(Double.isNaN(rating)) {
            return "§7Nenhuma.";
        }
        if(rating < 1) {
            return "§6✯§8✯✯✯✯";
        }else if (rating < 2) {
            return "§6✯✯§8✯✯✯";
        } else if (rating < 3) {
            return "§6✯✯✯§8✯✯";
        } else if (rating < 4) {
            return "§6✯✯✯✯§8✯";
        } else {
            return "§6✯✯✯✯✯";
        }

    }



}
