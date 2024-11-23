package org.gustaav.zoneRanks.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneRanks.ZoneRanks;
import org.gustaav.zoneRanks.rank.RankManager;
import org.gustaav.zoneRanks.rank.RankModel;

public class RankCommand extends Command {

    private final RankManager rankManager;

    public RankCommand(ZoneRanks plugin) {
        super(
                "rank",
                "Exibe o seu rank atual.",
                "Use /rank.",
                new String[]{},
                "zoneranks.rank"
        );
        this.rankManager = plugin.getRankManager();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        // Obtém o rank do jogador
        RankModel currentRank = rankManager.getPlayerRank(player.getUniqueId());
        if (currentRank == null) {
            player.sendMessage("§cNão foi possível encontrar seu rank atual.");
            return true;
        }

        // Exibe o rank atual
        player.sendMessage("§aSeu rank atual é: §f" + currentRank.prefix() + currentRank.nome());
        return true;
    }
}
