package org.gustaav.zoneEconomy.utils;


import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.gustaav.zoneEconomy.manager.EconomyManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EconomyProvider implements Economy {

    private final EconomyManager economyManager;

    public EconomyProvider(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean isEnabled() {
        return true; // Verifica se o método de economia está habilitado
    }

    @Override
    public String getName() {
        return "EssentialEconomy"; // Retorna o nome do método de economia
    }

    @Override
    public boolean hasBankSupport() {
        return false; // Retorna se o sistema suporta bancos
    }

    @Override
    public int fractionalDigits() {
        return 0; // Retorna o número de dígitos fracionários
    }

    @Override
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

    @Override
    public String currencyNamePlural() {
        return "Coins"; // Nome plural da moeda
    }

    @Override
    public String currencyNameSingular() {
        return "Coin"; // Nome singular da moeda
    }

    @Override
    public boolean hasAccount(String name) {
        return economyManager.hasAccount(Bukkit.getPlayer(name)); // Verifica se a conta do jogador existe
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return economyManager.hasAccount(player.getPlayer()); // Verifica se a conta do jogador existe
    }

    @Override
    public boolean hasAccount(String name, String worldName) {
        return economyManager.hasAccount(Bukkit.getPlayer(name)); // Verifica se a conta do jogador existe em um mundo específico
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return economyManager.hasAccount(player.getPlayer()); // Verifica se a conta do jogador existe em um mundo específico
    }

    @Override
    public double getBalance(String name) {
        return economyManager.getBalance(Bukkit.getPlayer(name));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economyManager.getBalance(player.getPlayer());
    }

    @Override
    public double getBalance(String name, String worldName) {
        return economyManager.getBalance(Bukkit.getPlayer(name));    }

    @Override
    public double getBalance(OfflinePlayer player, String worldName) {
        return economyManager.getBalance(player.getPlayer());
    }

    @Override
    public boolean has(String name, double amount) {
        return has(Objects.requireNonNull(Bukkit.getPlayer(name)), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return (getBalance(player) >= amount);
    }

    @Override
    public boolean has(String name, String worldName, double amount) {
        return has(Objects.requireNonNull(Bukkit.getPlayer(name)), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return withdrawPlayer(Bukkit.getPlayer(name), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount == 0) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");
        }
        var account = economyManager.hasAccount(player.getPlayer());
        if (account) {
            economyManager.withdraw(player.getPlayer(), amount);
            return new EconomyResponse(amount, economyManager.getBalance(player.getPlayer()), EconomyResponse.ResponseType.SUCCESS, "Transação feita.");
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Conta inválida."
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String worldName, double amount) {
        return withdrawPlayer(Bukkit.getPlayer(name), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        return depositPlayer(Bukkit.getPlayer(name), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount == 0) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");
        }
        var account = economyManager.hasAccount(player.getPlayer());
        if (account) {
            economyManager.deposit(player.getPlayer(), amount);
            return new EconomyResponse(amount, economyManager.getBalance(player.getPlayer()), EconomyResponse.ResponseType.SUCCESS, "Transação feita.");
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Conta inválida."
        );
    }

    @Override
    public EconomyResponse depositPlayer(String name, String worldName, double amount) {
        return depositPlayer(Bukkit.getPlayer(name), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String bankName, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(Objects.requireNonNull(Bukkit.getPlayer(playerName))); // Cria uma conta para o jogador
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return economyManager.loadPlayerEconomy(player.getUniqueId()); // Cria uma conta para o jogador
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(Objects.requireNonNull(Bukkit.getPlayer(playerName))); // Cria uma conta para o jogador em um mundo específico
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player); // Cria uma conta para o jogador em um mundo específico
    }


}

