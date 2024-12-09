package org.gustaav.zoneEssential.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneMissions.MissionAPI;
import org.gustaav.zoneMissions.models.PlayerModel;

import java.util.*;

public class Scoreboard implements Listener {

    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private ZoneEssential plugin;

    public Scoreboard(ZoneEssential essential) {
        essential.getLogger().info("Scoreboard sendo carregada...");
        essential.getServer().getScheduler().runTaskTimer(essential, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0, 20);
        this.plugin = essential;
    }

    public final Map<UUID, FastBoard> getBoards() {
        return boards;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        sendScoreboard(player);

        player.sendPlayerListHeader(Component.text("§r\n"
                + "§6§lRANKUP ORIGINS\n" + "§fUma nova era para o §7§nRankUP§f.\n§r"));
        player.sendPlayerListFooter(Component.text("§r\n" + "§7Loja: §fshop.enderzone.xyz\n" + "      §7Discord: §fdiscord.gg/enderzone      \n§"));
    }

    public void sendScoreboard(Player player) {
        FastBoard board = new FastBoard(player);
        this.boards.put(player.getUniqueId(), board);
        updateBoard(board);
    }

//    @EventHandler
//    public void onWorldChange(PlayerChangedWorldEvent e) {
//        Player player = e.getPlayer();
//
//        if(boards.containsKey(player.getUniqueId())) {
//            updateBoard(boards.get(player.getUniqueId()));
//        } else {
//            FastBoard board = new FastBoard(player);
//            this.boards.put(player.getUniqueId(), board);
//            updateBoard(board);
//        }
//
//    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        removeScoreboard(player);
    }

    public void removeScoreboard(Player player) {
        FastBoard board = this.boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    public void updateBoard(FastBoard board) {
        PlayerModel model = MissionAPI.getInstance().getPlayerModel(board.getPlayer());

        // Define as linhas iniciais comuns
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add(PlaceholderAPI.setPlaceholders(board.getPlayer(), "  §fRank: §f%zoneranks_prefix%   §r"));
        lines.add(PlaceholderAPI.setPlaceholders(board.getPlayer(), "  §7%zoneranks_progresso%"));
        lines.add("");

        lines.add("  §fClan: §7[TST]");

        lines.add("");
        lines.add(PlaceholderAPI.setPlaceholders(board.getPlayer(), "  §fCoins: §2$§f%vault_eco_balance_formatted%   §r"));
        lines.add(PlaceholderAPI.setPlaceholders(board.getPlayer(), "  §fCash: §6✪12.000"));
        lines.add("");
        lines.add("  §7mc.enderzone.xyz");


        // Atualiza o título e as linhas
        board.updateTitle(ChatColor.of("#EE7F00") + "§lORIGINS");
        board.updateLines(lines);
    }

}
