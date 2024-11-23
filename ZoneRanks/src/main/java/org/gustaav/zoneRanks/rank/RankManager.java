package org.gustaav.zoneRanks.rank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gustaav.zoneRanks.ZoneRanks;
import org.gustaav.zoneRanks.dao.MongoManager;

import java.util.*;

public class RankManager {

    private final Map<String, RankModel> ranks = new HashMap<>();
    private final Map<UUID, RankModel> playerRanks = new HashMap<>();
    private final MongoManager mongoManager;
    private final String rankInicial;

    public RankManager(MongoManager mongoManager, String rankInicial) {
        this.mongoManager = mongoManager;
        this.rankInicial = rankInicial;
    }

    /**
     * Função de evoluir o rank do jogador
     * @param player - Jogador que vai evoluir de rank.
     * @param currentRank - Rank atual do jogador.
     **/
    public void rankUP(Player player, RankModel currentRank) {

        if (currentRank == null) {
            player.sendMessage("§cRank atual inválido!");
            return;
        }
        RankModel nextRank = getNextRank(currentRank);
        if (nextRank == null) {
            player.sendMessage("§aVocê já alcançou o rank máximo!");
            return;
        }

        Bukkit.getLogger().info("Tentando promover o jogador " + player.getName() + " de rank " + currentRank.nome());


        double rankUpCost = nextRank.custo();
        if (!ZoneRanks.getEconomy().has(player, rankUpCost)) {
            player.sendMessage("§cVocê precisa de §e$" + ZoneRanks.format(rankUpCost) + " §cpara subir de rank.");
            return;
        }

        ZoneRanks.getEconomy().withdrawPlayer(player, rankUpCost);
        setPlayerRank(player.getUniqueId(), nextRank);

        if (nextRank.commands() != null && !nextRank.commands().isEmpty()) {
            nextRank.commands().forEach(command -> {
                if (command != null && !command.trim().isEmpty()) {
                    String parsedCommand = command.replace("{player}", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
                }
            });
        }


        player.sendMessage("§aVocê subiu para o rank §f" + nextRank.nome() + "!");
        Bukkit.broadcastMessage("§e" + player.getName() + " §asubiu para o rank §f" + nextRank.nome() + "!");
    }

    /**
     * Função para definir um rank em um jogador.
     * @param uuid - UUID do jogador.
     * @param rank - Rank que vai ser definido.
     **/
    public void setPlayerRank(UUID uuid, RankModel rank) {
        playerRanks.put(uuid, rank);

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            String rankPermission = "zoneranks.rank." + rank.id();
            rankPermission = rankPermission.substring(0, rankPermission.length() - 1);
            ZoneRanks.getPermission().playerAdd(player, rankPermission);

            ranks.values().stream()
                    .filter(r -> r.nivel() > rank.nivel())
                    .forEach(r -> {
                        String superiorPermission = "zoneranks.rank." + r.id();
                        superiorPermission = superiorPermission.substring(0, superiorPermission.length() - 1);
                        if (player.hasPermission(superiorPermission)) {
                            ZoneRanks.getPermission().playerRemove(player, superiorPermission);
                        }
                    });
        }
    }

    /**
     * Função para pegar o rank atual do jogador.
     * @param uuid - UUID do jogador.
     **/
    public RankModel getPlayerRank(UUID uuid) {
        return playerRanks.get(uuid);
    }

    /**
     * Função de remover o player da lista de carregados.
     * @param uuid - UUID do jogador.
     **/
    public void removePlayer(UUID uuid) {
        playerRanks.remove(uuid);
    }

    /**
     * Função para carregar o rank do jogador
     * @param uuid - UUID do jogador.
     **/
    public void loadPlayerRank(UUID uuid) {
        String rankId = mongoManager.getPlayerRank(uuid);
        RankModel rank = null;
        if (rankId != null) {
            rank = ranks.get(rankId);
        }
        if (rank == null) {
            rank = ranks.get(rankInicial);
            setPlayerRank(uuid, rank);
        }
        if (rank == null) {
            System.out.println("Rank inicial não encontrado para " + uuid);
            return;
        }
        playerRanks.put(uuid, rank);
    }

    /**
     * Função para pegar o rank seguinte
     * @param currentRank - Rank que quer pegar o próximo.
     **/
    public RankModel getNextRank(RankModel currentRank) {
        if (currentRank == null) return null;

        return ranks.values().stream()
                .filter(rank -> rank.nivel() == currentRank.nivel() + 1)
                .findFirst()
                .orElse(null);
    }

    /**
     * Função para pegar a barra de progresso do rank.
     * @param uuid - UUID do jogador.
     **/
    public String getProgresso(UUID uuid) {
        RankModel currentRank = getPlayerRank(uuid);
        RankModel nextRank = getNextRank(currentRank);

        if (nextRank == null) {
            return "§aúltimo rank! ;)  ";
        }

        double saldoAtual = ZoneRanks.getEconomy().getBalance(Bukkit.getPlayer(uuid));
        double custoProximoRank = nextRank.custo();

        double porcentagem = Math.min(saldoAtual / custoProximoRank, 1.0);

        if((porcentagem * 100) >= 100) {
            return "§dEvolua! /rankup";
        }

        int barrasCheias = (int) (porcentagem * 10);
        int barrasVazias = 10 - barrasCheias;

        String barra = "§2" +
                "◆".repeat(barrasCheias) +
                "§8" +
                "◆".repeat(barrasVazias);

        return barra + " §7" + (int) (porcentagem * 100) + "% ";
    }

    /**
     * Função para salvar todos os ranks do jogador na database.
     **/
    public void saveAllPlayerRanks() {
        playerRanks.forEach((uuid, rank) -> {
            mongoManager.savePlayerRank(uuid, rank.id());
        });
    }

    /**
     * Função para carregar todos os ranks da config.
     * @param loadedRanks - Ranks carregados.
     **/
    public void loadRanks(Map<String, RankModel> loadedRanks) {
        ranks.putAll(loadedRanks);
    }

    /**
     * Função para retornar a lista dos ranks de todos jogadores online.
     **/
    public Map<UUID, RankModel> getPlayerRanks() {
        return Collections.unmodifiableMap(playerRanks);
    }
}
