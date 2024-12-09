package org.gustaav.zoneArmazem.warehouse.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.listener.ArmazemEvent;
import org.gustaav.zoneArmazem.warehouse.models.DropModel;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class WarehouseGUI {

    Base base;
    Player player;
    private final ChestGui armazem;
    private final WarehouseModel plot;

    public WarehouseGUI(Base base, Player player, WarehouseModel plot) {
        armazem = new ChestGui(5, String.format("Armazém [%s/%s]", base.getTotal(plot), base.getCapacidade(plot.getNivel())));

        armazem.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        this.base = base;
        this.player = player;
        this.plot = plot;
    }

    public void openMenu() {
        StaticPane painel = new StaticPane(1, 1, 7, 2);
        StaticPane nav = new StaticPane(0, 4, 9, 1);
        loadProducts(painel);
        configNav(nav);
        armazem.addPane(painel);
        armazem.show(player);
    }

    public void configNav(StaticPane nav) {
        loadExit(nav);
        loadPrivate(nav);
        loadUpgrade(nav);

    }
    public void loadExit(StaticPane nav) {
        ItemStack sair = new ItemStack(Material.ARROW);
        ItemMeta sairMeta = sair.getItemMeta();
        assert sairMeta != null;
        sairMeta.setDisplayName("§cFechar");
        sair.setItemMeta(sairMeta);

        GuiItem sairGui = new GuiItem(sair, event -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        });

        nav.addItem(sairGui, 0, 0);
    }
    public void loadPrivate(StaticPane nav) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        profile.setProperty(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGY5NWVlYWRiMGUyZGU0NWVmOTJjYThmOTQ1MmMwNWQyMGY3ZmQ3MDUxMWMwMjczNDQ0ZTg2ZDRkMTU0Y2VjOSJ9fX0="));
        ItemStack level1 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta level1Meta = (SkullMeta) level1.getItemMeta();
        level1Meta.setPlayerProfile(profile);

        level1Meta.displayName(MiniMessage.miniMessage().deserialize("<gold>Modo Privado").decoration(TextDecoration.ITALIC, false));
        List<String> lores = new ArrayList<>();
        lores.add("§7Configure para apenas o dono do terreno");
        lores.add("§7conseguir vender os produtos do armazém.");
        lores.add("");
        if(plot.getMode()) {
            lores.add("  §fStatus: §aAtivado");
        } else {
            lores.add("  §fStatus: §cDesativado");
        }
        lores.add("§r");
        if(plot.getPlotId().isOwner(player.getUniqueId())) {
            lores.add("§eClique para alterar o modo.");
        } else {
            lores.add("§cApenas o dono do terreno pode alterar.");
        }

        level1Meta.setLore(lores);
        level1.setItemMeta(level1Meta);

        GuiItem modoPrivate = new GuiItem(level1, event -> {
            if(plot.getPlotId().isOwner(player.getUniqueId())) {
                plot.setMode(!plot.getMode());
                updateMenu();
            }
            event.setCancelled(true);
        });

        nav.addItem(modoPrivate, 3, 0);
        armazem.addPane(nav);
    }
    public void loadUpgrade(StaticPane nav) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        profile.setProperty(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUxNTAyY2U5YzdlYWE1ZTA3M2I0ZTQ5YjcwYmU2NDg0MmFkZWZlMTJmYzE3ZDk2ZDM4ZTU2ZjYzZGIyOTNhZCJ9fX0="));
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setPlayerProfile(profile);
        meta.displayName(MiniMessage.miniMessage().deserialize("<color:#acffab>Evoluir Armazenamento</color>").decoration(TextDecoration.ITALIC, false));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Aumente o limite que seu armazém"));
        lore.add(Component.text("§7pode guardar suas plantações."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("  §fNível: §7" + plot.getNivel()));
        if(plot.getNivel() == base.getNivelLimite()) {
            lore.add(Component.text("  §FUpgrade: §7Máximo."));
            lore.add(Component.text("§r"));
            lore.add(Component.text("§cVocê não possui mais níveis para evoluir."));
        } else {
            lore.add(Component.text(String.format("  §fUpgrade: §a§m%s§a ➟ %s", formatter.format(base.getCapacidade(plot.getNivel())), formatter.format(base.getCapacidade(plot.getNivel() + 1)))));
            lore.add(Component.text("  §FCusto: §2$§f" + Base.formatNumber(base.getValue(plot.getNivel()))));
            lore.add(Component.text("§r"));
            if(ZoneArmazem.getEconomy().has(player, base.getValue(plot.getNivel()))) {
                lore.add(Component.text("§7Clique para evoluir o seu armazém"));
            } else {
                lore.add(Component.text("§cVocê não possui dinheiro suficiente."));
            }
        }
        meta.lore(lore);
        item.setItemMeta(meta);

        nav.addItem(new GuiItem(item, e -> {
            if(plot.getNivel() < base.getNivelLimite()) {
                double value = base.getValue(plot.getNivel());
                if(ZoneArmazem.getEconomy().has(player, value)) {
                    ZoneArmazem.getEconomy().withdrawPlayer(player, value);
                    plot.setNivel(plot.getNivel() + 1);
                    player.sendMessage(String.format("§aArmazém evoluído com sucesso. §7[%s ➟ %s]", base.getCapacidade(plot.getNivel() - 1), base.getCapacidade(plot.getNivel())));
                    player.closeInventory();
                } else {
                    player.closeInventory();
                    player.sendMessage("§cSaldo insuficiente.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                }
            }
        }), 5, 0);

    }

    public void loadProducts(StaticPane painel) {
        painel.clear();
        if (plot.getDrops().isEmpty()) {
            GuiItem item = loadEmpty();
            painel.addItem(item, 3, 0);
        } else {
            int counter = 0;
            for (Map.Entry<Material, Integer> entry : plot.getDrops().entrySet()) {
                GuiItem product = loadItem(entry, painel);
                painel.addItem(product, counter, 0);
                counter++;
            }
        }
    }

    private GuiItem loadItem(Map.Entry<Material, Integer> entry, StaticPane pane) {
        Material material = entry.getKey();
        int quantidade = entry.getValue();
        double total = 0;
        DropModel drop = base.getDrops().stream()
                .filter(drops -> drops.getDrop().equals(material))
                .findFirst().orElse(null);
        assert drop != null;
        total = quantidade * drop.getValue();
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§a" + drop.getName());
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("  §fQuantidade: §7" + Base.formatNumber(quantidade));
        lores.add("  §fValor total: §2$§f" + Base.formatNumber(total));
        lores.add("");
        if(plot.getMode()) {
            if(Objects.equals(plot.getPlotId().getOwner(), player.getUniqueId())) {
                lores.add("§eClique para vender.");
            } else {
                lores.add("§cVocê não tem permissão para vender.");
            }
        } else {
            lores.add("§eClique para vender.");
        }
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);

        return new GuiItem(item, e -> {
            e.setCancelled(true);
            if(!plot.getMode() || Objects.equals(plot.getPlotId().getOwner(), player.getUniqueId())) {
                int quantidadeAtual = entry.getValue();
                if (quantidadeAtual > 0) {
                    double price = quantidadeAtual * drop.getValue();
                    ZoneArmazem.getEconomy().depositPlayer(player, price);
                    player.sendMessage("§aVocê vendeu §7x" + Base.formatNumber(quantidadeAtual) + " §apor §2$§f" + Base.formatNumber(quantidadeAtual * drop.getValue()) + "§a.");
                    plot.removeDrop(entry.getKey());
                    ArmazemEvent event = new ArmazemEvent(player, material, quantidadeAtual, price);
                    Bukkit.getPluginManager().callEvent(event);
                    updateMenu();
                } else {
                    player.sendMessage("§cVocê não possui nenhuma plantação para vender. Alguém já deve ter vendido!");
                }
            }
        });
    }

    private GuiItem loadEmpty() {
        ItemStack empty = new ItemStack(Material.COBWEB);
        ItemMeta emptyItemMeta = empty.getItemMeta();
        assert emptyItemMeta != null;
        emptyItemMeta.setDisplayName("§cVázio");
        List<String> lores = new ArrayList<>();
        lores.add("§7Nenhuma plantação por aqui...");
        emptyItemMeta.setLore(lores);
        empty.setItemMeta(emptyItemMeta);

        return new GuiItem(empty, e -> e.setCancelled(true));
    }

    private void updateMenu() {
        StaticPane newPane = new StaticPane(1, 1, 7, 2);
        StaticPane nav = new StaticPane(0, 4, 9, 1);
        armazem.getPanes().clear();
        loadProducts(newPane);
        configNav(nav);
        armazem.setTitle(String.format("Armazém [%s/%s]", base.getTotal(plot), base.getCapacidade(plot.getNivel())));
        armazem.addPane(newPane);
        armazem.addPane(nav);
        armazem.update();
    }

}
