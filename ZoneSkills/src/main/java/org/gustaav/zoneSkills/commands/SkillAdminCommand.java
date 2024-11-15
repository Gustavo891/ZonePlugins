package org.gustaav.zoneSkills.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneSkills.Database.PlayerData;
import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.skills.SkillType;

public class SkillAdminCommand extends Command{

    PlayerDataManager playerData;

    public SkillAdminCommand(PlayerDataManager playerData) {
        super(
                "skilladmin",
                "Comando para definir as skills (admin).",
                "Use /skilladmin ajuda",
                new String[]{},
                "zoneskills.admin"
        );
        this.playerData = playerData;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof Player player)){
            return;
        }

        if(args.length == 4){ //skilladmin set (player) mining 10
            if(args[0].equalsIgnoreCase("set")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage("§cJogador inválido.");
                    return;
                }
                try {
                    playerData.getSkillData(target.getUniqueId(), SkillType.valueOf(args[2])).setLevel(Integer.parseInt(args[3]));
                    player.sendMessage("§eO nível da habilidade §f'" + args[2] + "' §epara §7" + args[3] + "§e.");
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cSkill ou level inválido.");
                }
            } else {
                player.sendMessage("§cUse: /skilladmin set <player> <mining> <level>");
            }
        } else {
            player.sendMessage("§cUse: /skilladmin set <player> <mining> <level>");
        }

    }
}
