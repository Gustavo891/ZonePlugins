package org.gustaav.zoneRanks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneRanks.ZoneRanks;
import net.milkbowl.vault.economy.Economy;
import org.gustaav.zoneRanks.rank.RankManager;
import org.gustaav.zoneRanks.rank.RankModel;

public class RankUpCommand extends Command {

    private final ZoneRanks plugin;
    private final RankManager rankManager;
    private final Economy economy;

    public RankUpCommand(ZoneRanks plugin) {
        super(
                "rankup",
                "Upe de rank",
                "Use /rankup.",
                new String[]{"up"},
                "zoneranks.rankup"
        );
        this.plugin = plugin;
        this.rankManager = plugin.getRankManager();
        this.economy = ZoneRanks.getEconomy();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        RankModel currentRank = rankManager.getPlayerRank(player.getUniqueId());

        if (currentRank == null) {
            player.sendMessage("§cNão foi possível encontrar seu rank atual.");
            return true;
        }

        rankManager.rankUP(player, currentRank);
        return true;
    }

}
