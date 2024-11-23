package org.gustaav.zoneArmazem.warehouse.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.listener.ArmazemSoldEvent;
import org.gustaav.zoneArmazem.warehouse.models.DropModel;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class WarehouseGUI {

    Base base;
    Player player;
    private final ChestGui armazem;
    private final WarehouseModel plot;

    public WarehouseGUI(Base base, Player player, WarehouseModel plot) {
        armazem = new ChestGui(5, String.format("Armazém [%s/%s]", base.getTotal(plot), base.getCapacidade(plot.getNivel())));
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

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        ItemStack level1 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta level1Meta = (SkullMeta) level1.getItemMeta();
        URL urlObject;
        try {
            urlObject = new URL("https://textures.minecraft.net/texture/cbb632ceb83e2c39cb53e801a29af9109cf341df89d01ed8dfeb49460a5264c8");
        } catch (MalformedURLException exception) {
            level1.setType(Material.NETHER_STAR);
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);

        assert level1Meta != null;
        level1Meta.setDisplayName("§6Modo Privado");
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

        nav.addItem(modoPrivate, 4, 0);
        armazem.addPane(nav);

    }

    public void loadProducts(StaticPane painel) {
        painel.clear(); // Limpar o painel antes de recarregar os itens
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
            Bukkit.broadcastMessage("ownerId: " + plot.getPlotId().getOwner());
            Bukkit.broadcastMessage("playerId: " + player.getUniqueId());
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
                    ArmazemSoldEvent event = new ArmazemSoldEvent(player, material, quantidadeAtual, price);
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
