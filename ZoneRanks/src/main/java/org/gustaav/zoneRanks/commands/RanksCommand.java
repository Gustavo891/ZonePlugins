package org.gustaav.zoneRanks.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneRanks.gui.RanksGUI;
import org.gustaav.zoneRanks.rank.RankManager;
import org.gustaav.zoneRanks.rank.RankModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;

public class RanksCommand {

    RankManager rankManager;

    public RanksCommand(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Command("rank <player>")
    public void rank(CommandSender sender,@Optional @Named("player") Player target) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if(target == null) {
            RankModel currentRank = rankManager.getPlayerRank(player.getUniqueId());
            if (currentRank == null) {
                player.sendMessage("§cNão foi possível encontrar seu rank atual.");
                return;
            }
            player.sendMessage("§eSeu rank atual é: §f" + currentRank.nome());
            return;
        }
        RankModel currentRank = rankManager.getPlayerRank(target.getUniqueId());
        if (currentRank == null) {
            player.sendMessage("§cNão foi possível encontrar o rank desse jogador.");
            return;
        }
        player.sendMessage(String.format("§eO rank de §f%s§e é: §7%s", target.getName(), currentRank.nome()));

    }


    @Command("rankup")
    public void RankUP(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        RankModel currentRank = rankManager.getPlayerRank(player.getUniqueId());
        if (currentRank == null) {
            player.sendMessage("§cNão foi possível encontrar seu rank atual.");
            return;
        }
        rankManager.rankUP(player, currentRank);
    }


    @Command("ranks")
    public void listRanks(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }

        RanksGUI ranksGUI = new RanksGUI(player, rankManager);
    }

}
