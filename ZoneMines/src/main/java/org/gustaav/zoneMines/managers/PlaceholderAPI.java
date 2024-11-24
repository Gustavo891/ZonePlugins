package org.gustaav.zoneMines.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    LapisManager lapisManager;

    public PlaceholderAPI(LapisManager lapisManager) {
        this.lapisManager = lapisManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zonemines";
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

        return switch (params) {
            case "normal" -> String.valueOf(lapisManager.getPlayers());
            default -> null;
        };
    }

}
