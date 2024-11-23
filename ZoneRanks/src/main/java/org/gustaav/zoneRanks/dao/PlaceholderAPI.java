package org.gustaav.zoneRanks.dao;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.gustaav.zoneRanks.rank.RankManager;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    RankManager rankManager;

    public PlaceholderAPI(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zoneranks";
    }

    @Override
    public @NotNull String getAuthor() {
        return "gustaav_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        return switch (params) {
            case "prefix" -> rankManager.getPlayerRank(player.getUniqueId()).prefix();
            case "progresso" -> rankManager.getProgresso(player.getUniqueId());
            default -> null;
        };
    }

}

