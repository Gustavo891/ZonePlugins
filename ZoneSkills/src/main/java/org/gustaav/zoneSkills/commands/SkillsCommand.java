package org.gustaav.zoneSkills.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.skills.SkillGUI;

public class SkillsCommand extends Command{

    ZoneSkills plugin;

    public SkillsCommand(ZoneSkills plugin) {
        super(
                "skills",
                "Comando para ver suas habilidades.",
                "Use /skills",
                new String[]{},
                "zoneskills.skills"
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {return;}

        SkillGUI skillGUI = new SkillGUI(plugin, player);


    }
}
