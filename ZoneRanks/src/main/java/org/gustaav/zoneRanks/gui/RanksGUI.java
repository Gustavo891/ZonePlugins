package org.gustaav.zoneRanks.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.gustaav.zoneRanks.rank.RankManager;
import org.gustaav.zoneRanks.rank.RankModel;

import java.util.*;

public class RanksGUI {

    ChestGui ranksGui;
    Player player;
    RankManager rankManager;

    public RanksGUI(Player player, RankManager rankManager) {
        this.rankManager = rankManager;
        this.player = player;
        ranksGui = new ChestGui(4, "Lista de ranks:");

        PaginatedPane pane = new PaginatedPane(Slot.fromIndex(10), 7, 2);
        loadRanks(pane);

        ranksGui.addPane(pane);
        ranksGui.show(player);

    }

    public void loadRanks(PaginatedPane pane) {

        RankModel rankAtual = rankManager.getPlayerRank(player.getUniqueId());
        List<GuiItem> items = new ArrayList<>();

        List<RankModel> sortedRanks = new ArrayList<>(rankManager.getRanks().values());
        sortedRanks.sort(Comparator.comparingInt(RankModel::nivel));

        for (RankModel rank : sortedRanks) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            UUID uuid = UUID.randomUUID();
            PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
            playerProfile.setProperty(new ProfileProperty("textures", rank.head()));
            meta.setPlayerProfile(playerProfile);
            meta.displayName(Component.text(rank.prefix().replace("[", "").replace("]", "")));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§r"));
            if (rank.nivel() == 1) {
                lore.add(Component.text("  §fCusto: §7Grátis"));
            } else {
                lore.add(Component.text("  §fCusto: §2$§f" + format(rank.custo())));
            }
            RankModel nextRank = rankManager.getNextRank(rank);
            if (nextRank != null) {
                lore.add(Component.text("  §fPróximo: §7" + rankManager.getNextRank(rank).prefix().replace("[", "").replace("]", "")));
            } else {
                lore.add(Component.text("  §fPróximo: §7Nenhum"));
            }
            lore.add(Component.text(""));
            if (rankAtual.nivel() == rank.nivel()) {
                lore.add(Component.text("§7Você está nesse rank."));
            } else if (rankAtual.nivel() < rank.nivel()) {
                lore.add(Component.text("§cVocê ainda não chegou nesse rank."));
            } else {
                lore.add(Component.text("§aVocê já completou esse rank."));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
            items.add(new GuiItem(item));
        }

        pane.populateWithGuiItems(items);

    }

    public String format(double amount) {
        if (amount < 1000) {
            return String.format("%.2f", amount);
        } else if (amount < 1_000_000) {
            return String.format("%.2fk", amount / 1000);
        } else if (amount < 1_000_000_000) {
            return String.format("%.2fM", amount / 1_000_000);
        } else if (amount < 1_000_000_000_000L) {
            return String.format("%.2fB", amount / 1_000_000_000);
        } else {
            return String.format("%.2fT", amount / 1_000_000_000_000L);
        }
    }


}
