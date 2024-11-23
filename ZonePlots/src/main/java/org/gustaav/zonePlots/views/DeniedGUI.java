package org.gustaav.zonePlots.views;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.plotsquared.core.plot.Plot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DeniedGUI {

    PlotGUI plotGUI;
    ChestGui deniedGui;
    Plot plot;
    Player player;

    public DeniedGUI(Plot plot, Player player, PlotGUI plotGUI) {
        this.plotGUI = plotGUI;
        this.deniedGui = new ChestGui(4, "Lista de proibidos:");
        this.plot = plot;
        this.player = player;
        PaginatedPane pane = new PaginatedPane(Slot.fromIndex(10), 7, 1);
        StaticPane nav = new StaticPane(Slot.fromIndex(27), 9, 1);

        deniedGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadMembers(pane);
        loadNavBar(nav, pane);


        deniedGui.addPane(pane);
        deniedGui.addPane(nav);

        deniedGui.show(player);
    }

    public void loadMembers(PaginatedPane pane) {

        List<GuiItem> items = new ArrayList<>();

        // caso não tenha ninguem adicionado
        if(plot.getDenied().isEmpty()) {
            ItemStack cobweb = new ItemStack(Material.COBWEB);
            ItemMeta meta = cobweb.getItemMeta();
            meta.displayName(Component.text("§cVázio"));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Proiba um jogador em seu"));
            lore.add(Component.text("§7terreno para aparecer aqui."));
            meta.lore(lore);
            cobweb.setItemMeta(meta);

            pane.setLength(1);
            pane.setSlot(Slot.fromIndex(13));
            pane.populateWithGuiItems(List.of(new GuiItem(cobweb)));
            return;
        }

        // caso tenha membros proibidos
        for(UUID uuid : plot.getDenied()) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

            if(target.getName() == null) {
                continue;
            }
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(target.getName()));
            Component component = MiniMessage.miniMessage().deserialize("<#076800>" + target.getName())
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            if(plot.isOwner(player.getUniqueId())) {
                lore.add(Component.text("§7Clique para libera-lo."));
            } else {
                lore.add(Component.text("§7Sem permissão para liberar."));

            }
            meta.lore(lore);
            playerHead.setItemMeta(meta);

            items.add(new GuiItem(playerHead, e -> {
                if(plot.isOwner(player.getUniqueId())) {
                    plot.removeDenied(target.getUniqueId());
                    player.sendMessage("§aJogador liberado com sucesso.");
                    updateMenu();
                }
            }));

        }
        pane.populateWithGuiItems(items);

    }

    public void loadNavBar(StaticPane nav, PaginatedPane pane) {

        // carregar botão de voltar
        {
            ItemStack arrowBack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) arrowBack.getItemMeta();
            String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVjNDlmNGQ1OTUxZTQzMjZiZjM4MTIyOTZlZWE4ZjIwZmIyNzU0YjBhMGZiYWMxN2FiNWI1YTY0NjZiYSJ9fX0=";
            UUID uuid = UUID.randomUUID();
            PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
            playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
            meta.setPlayerProfile(playerProfile);
            meta.displayName(Component.text("§dVoltar"));
            arrowBack.setItemMeta(meta);

            nav.addItem(new GuiItem(arrowBack, e -> {
                if (pane.getPage() == 0) {
                    player.getInventory().close();
                    plotGUI.loadGui();
                } else {
                    pane.setPage(pane.getPage() - 1);
                    deniedGui.update();
                }
            }), Slot.fromIndex(2));
        }

        // carregar botão de continuar
        {
            if (pane.getPages() > 0) {
                ItemStack arrowForward = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta metaForward = (SkullMeta) arrowForward.getItemMeta();
                String base64Texture2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFmZjlmMmU2NTIzZGI3NWI5MWI4NjFjYTRlMTkwZjUzYWFkNzQ5NmU0MjRjYWM4YWQ5MzY1MzZiMDU0YTdkMyJ9fX0=";
                UUID uuid2 = UUID.randomUUID();
                PlayerProfile playerProfile2 = Bukkit.createProfile(uuid2, "");
                playerProfile2.setProperty(new ProfileProperty("textures", base64Texture2));
                metaForward.setPlayerProfile(playerProfile2);
                metaForward.displayName(Component.text("§dPróximo"));
                arrowForward.setItemMeta(metaForward);

                nav.addItem(new GuiItem(arrowForward, e -> {
                    if (pane.getPage() == 0) {
                        pane.setPage(pane.getPage() + 1);
                        deniedGui.update();
                    }
                }), Slot.fromIndex(6));
            }
        }

        // carregar botão de proibir jogador caso seja membro
        if(plot.isOwner(player.getUniqueId())) {
            ItemStack adicionar = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta adicionarMeta = (SkullMeta) adicionar.getItemMeta();
            String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2ZjZTY2Nzg5Yjc3YjI2MWU2NjliMjM5ZmNkNGE2Mjk2OTY1NTg1YWQwMTkzM2Y4MzBkZTdhOTNkZTQzNzRjZSJ9fX0=";
            UUID uuid = UUID.randomUUID();
            PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
            playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
            adicionarMeta.setPlayerProfile(playerProfile);
            adicionarMeta.displayName(Component.text("§dProibir um jogador"));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Clique para proibir um jogador"));
            lore.add(Component.text("§7de entrar em seu terreno."));
            adicionarMeta.lore(lore);
            adicionar.setItemMeta(adicionarMeta);

            nav.addItem(new GuiItem(adicionar, e -> {
                confirmarPlayer(targetName -> {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
                    if(plot.getDenied().contains(target.getUniqueId())) {
                        player.sendMessage("§cJogador já proibido de entrar em seu terreno.");
                        return;
                    } else if(targetName.equals(player.getName())) {
                        player.sendMessage("§cVocê não pode se pribir de entrar no terreno.");
                        return;
                    }

                    plot.addDenied(target.getUniqueId());
                    player.sendMessage("§aJogador proibido com sucesso!");
                    updateMenu();
                });
            }), Slot.fromIndex(4));
        }

    }

    public void confirmarPlayer(Consumer<String> callback) {
        player.closeInventory(); // Fecha o inventário para abrir a placa.
        player.sendMessage("§aDigite o nome do jogador no chat para proibi-lo:");

        // Registrar evento de chat
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                if (!event.getPlayer().equals(player)) return;

                event.setCancelled(true);
                String targetName = event.getMessage();

                callback.accept(targetName);

                Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(DeniedGUI.class), () -> {
                    updateMenu(); // Atualiza o menu
                    deniedGui.show(player); // Mostra o inventário atualizado
                });
                
                AsyncPlayerChatEvent.getHandlerList().unregister(this);
            }
        }, JavaPlugin.getProvidingPlugin(DeniedGUI.class));
    }

    public void updateMenu() {
        PaginatedPane pane = new PaginatedPane(Slot.fromIndex(10), 7, 1);
        StaticPane nav = new StaticPane(Slot.fromIndex(27), 9, 1);
        deniedGui.getPanes().clear();
        loadMembers(pane);
        loadNavBar(nav, pane);
        deniedGui.addPane(pane);
        deniedGui.addPane(nav);
        deniedGui.update();
    }


}
