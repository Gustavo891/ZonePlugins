package org.gustaav.zoneEconomy.manager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.gustaav.zoneEconomy.utils.utils;
import org.jetbrains.annotations.NotNull;

public class PlaceholderManager extends PlaceholderExpansion {

    EconomyManager economyManager;

    public PlaceholderManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zoneeconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "gustavo891";
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
            case "money" -> utils.format(economyManager.getBalance(player));
            default -> null;
        };
    }

}
