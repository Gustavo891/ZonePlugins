package org.gustaav.zoneEconomy.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneEconomy.ZoneEconomy;
import org.gustaav.zoneEconomy.database.MongoManager;
import org.gustaav.zoneEconomy.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyManager {

    MongoManager mongoManager;
    List<PlayerModel> playersList = new ArrayList<PlayerModel>();
    List<PlayerModel> moneyTop = new ArrayList<>();

    public EconomyManager(MongoManager mongoManager, ZoneEconomy zoneEconomy) {
        this.mongoManager = mongoManager;
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllPlayerEconomy();
            }
        }.runTaskTimer(zoneEconomy, 0L, 100L);

        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllPlayerEconomy();
                setMoneyTop();
            }
        }.runTaskTimer(zoneEconomy, 0L, 60 * 20L); // Carrega o money top a cada 1 minuto

    }

    public List<PlayerModel> getTop10() {
        return moneyTop;
    }

    public void setMoneyTop() {
        moneyTop.clear();
        moneyTop = mongoManager.getTopPlayers(10);
    }

    public boolean loadPlayerEconomy(UUID uuid) {
        PlayerModel playerModel = mongoManager.getPlayerModel(uuid);
        if(hasAccount(Bukkit.getPlayer(uuid))) {
            return false;
        }
        if (playerModel == null) {
            playerModel = new PlayerModel(uuid, 0.0);
        }
        playersList.add(playerModel);
        return true;
    }

    public boolean hasAccount(Player target) {
        return playersList.stream()
                .anyMatch(player -> player.getUuid().equals(target.getUniqueId()));
    }

    public double getBalance(Player target) {
        return playersList.stream()
                .filter(player -> player.getUuid().equals(target.getUniqueId()))
                .findFirst()
                .map(PlayerModel::getMoney)
                .orElseGet(() -> {
                    loadPlayerEconomy(target.getUniqueId());
                    return getBalance(target);
                });
    }

    public void withdraw(Player target, double amount) {
        playersList.stream().filter(player -> player.getUuid().equals(target.getUniqueId())).findFirst().ifPresent(player -> {
           player.setMoney(player.getMoney() - amount);
        });
    }

    public void deposit(Player target, double amount) {
        playersList.stream().filter(player -> player.getUuid().equals(target.getUniqueId())).findFirst().ifPresent(player -> {
            player.setMoney(player.getMoney() + amount);
        });
    }

    public void setBalance(Player target, double amount) {
        playersList.stream().filter(player -> player.getUuid().equals(target.getUniqueId())).findFirst().ifPresent(player -> {
            player.setMoney(amount);
        });
    }

    public void loadAllPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerEconomy(player.getUniqueId());
        }
    }

    public void savePlayerEconomy(UUID uuid) {
        PlayerModel playerModel = playersList.stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);

        if (playerModel != null) {
            mongoManager.savePlayerRank(playerModel);
            playersList.remove(playerModel);
        } else {
            mongoManager.savePlayerRank(new PlayerModel(uuid, 0.0));
        }
    }

    public void saveAllPlayerEconomy() {
        for(PlayerModel playerModel : playersList) {
            mongoManager.savePlayerRank(playerModel);
        }
    }
}
