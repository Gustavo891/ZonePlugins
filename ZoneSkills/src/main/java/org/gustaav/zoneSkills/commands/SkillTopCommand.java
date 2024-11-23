package org.gustaav.zoneSkills.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.skills.SkillType;

import java.util.Objects;
import java.util.UUID;

public class SkillTopCommand extends Command{

    ZoneSkills plugin;

    public SkillTopCommand(ZoneSkills plugin) {
        super(
                "skillstop",
                "Comando para ver os top habilidades.",
                "Use /skillstop",
                new String[]{},
                "zoneskills.skillstop"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {return;}

        if(args.length == 1) {
            try {
              SkillType type = SkillType.valueOf(args[0].toLowerCase());
              switch(type) {
                  case SkillType.mining -> {
                      player.sendMessage("§r");
                      player.sendMessage("  §aTOP Mineração:");
                      int indice = 1;
                      for(UUID uuid :  plugin.getSkillRepository().getMiningSkill().getMiningTop()) {
                          player.sendMessage("  §7" + indice + ". §f" + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() + ": §7" + plugin.getPlayerDataManager().getLevel(uuid, SkillType.mining));
                      }
                      player.sendMessage("§r");
                      player.sendMessage("  §7A lista é atualizada a cada §f1 §7minuto(s).");
                      player.sendMessage("§r");
                  }
              }
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cSkill ou level inválido.");
                return;
            }
        } else {
            player.sendMessage("§cUse /skillstop <tipo>.");
        }


    }
}
