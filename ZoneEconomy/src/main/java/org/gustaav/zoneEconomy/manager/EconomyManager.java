package org.gustaav.zoneEconomy.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneEconomy.ZoneEconomy;
import org.gustaav.zoneEconomy.database.MongoManager;
import org.gustaav.zoneEconomy.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.gustaav.zoneEconomy.utils.utils.format;

public class EconomyManager {

    MongoManager mongoManager;
    List<PlayerModel> playersList = new ArrayList<PlayerModel>();
    List<PlayerModel> moneyTop = new ArrayList<>();

    NamespacedKey key;

    public EconomyManager(MongoManager mongoManager, ZoneEconomy zoneEconomy) {
        this.mongoManager = mongoManager;
        this.key = new NamespacedKey(zoneEconomy, "display");
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
        for(World w : Bukkit.getWorlds()) {
            for(Entity entity : w.getEntities()) {
                if(entity instanceof TextDisplay display && entity.getPersistentDataContainer().has(key)) {
                    loadTopDisplay(display);
                }
            }
        }
    }
    public void loadTopDisplay(TextDisplay display) {
        StringBuilder text = new StringBuilder("§r\n  §2§l⚑ §r§2Veja os jogadores mais ricos:\n\n");
        for(int i = 1; i < 6; i++) {
            try {
                PlayerModel model = moneyTop.get(i-1);
                OfflinePlayer target = Bukkit.getOfflinePlayer(model.getUuid());
                String prefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");
                text.append("  §7").append(i).append("º §f").append(ChatColor.translateAlternateColorCodes('&', prefix)).append(target.getName()).append("§7: §2$§f").append(format(model.getMoney())).append("\n");
            } catch (IndexOutOfBoundsException ignored) {
                text.append("  §7").append(i).append("º §fNenhum\n");
            }
        }
        text.append("§r\n        §7Atualiza a cada §f5§7 minutos.        \n§r");
        display.setText(text.toString().trim());
        display.setBillboard(Display.Billboard.FIXED); // Fixa o display
        display.setShadowed(true); // Ativa a sombra do texto
        display.setViewRange(20); // Define o alcance de visão
        display.setAlignment(TextDisplay.TextAlignment.LEFT); // Centraliza o texto
        display.setPersistent(true); // Para manter o display após reinício do chunk
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

    public NamespacedKey getKey() {
        return key;
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
