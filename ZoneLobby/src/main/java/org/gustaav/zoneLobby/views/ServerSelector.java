package org.gustaav.zoneLobby.views;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.model.ServerModel;
import org.gustaav.zoneLobby.queue.QueueManager;

import java.util.ArrayList;
import java.util.List;

public class ServerSelector {

    ChestGui chestGui;
    ZoneLobby zoneLobby;
    Player player;
    QueueManager queueManager;


    public ServerSelector(ZoneLobby zoneLobby, Player player) {
        this.chestGui = new ChestGui(3, "Selecione um servidor:");
        this.zoneLobby = zoneLobby;
        this.player = player;
        this.queueManager = zoneLobby.getQueueManager();
        StaticPane rankup = new StaticPane(Slot.fromIndex(0), 9, 3);
        loadRankUP(rankup);

        chestGui.setOnGlobalClick(e -> e.setCancelled(true));

        chestGui.addPane(rankup);
        chestGui.show(player);
    }

    public void loadRankUP(StaticPane pane) {
        ServerModel serverModel = zoneLobby.getServerCache().getServer("rankup");
        ItemStack item = new ItemStack(Material.VAULT);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("§6RankUP Origins ⚑"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§8Sobrevivência"));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§7Um novo modo de jogo irá lançar em breve em"));
        lore.add(Component.text("§7nossa rede, chame seus amigos para jogar."));
        lore.add(Component.text("§r"));
        if(!serverModel.isDeployment()) {
            lore.add(Component.text("§cEstamos em desenvolvimento."));
            lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "§7%bungee_rankup% pessoas trabalhando!")));
        } else if (serverModel.isMaintenance()) {
            lore.add(Component.text("§cServidor está em manutenção."));
            lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "§7%bungee_rankup% pessoas trabalhando!")));
        } else if (serverModel.isBetaVip()) {
            lore.add(Component.text("§bServidor está em BETA VIP."));
            lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "§7%bungee_rankup% vips conectados!")));
        } else {
            lore.add(Component.text("§eClique para se conectar."));
            lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "§7%bungee_rankup% pessoas conectadas!")));
        }
        meta.lore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            queueManager.addToQueue("rankup", player);
            player.closeInventory();
            e.setCancelled(true);
        }), Slot.fromIndex(13));

    }



}
