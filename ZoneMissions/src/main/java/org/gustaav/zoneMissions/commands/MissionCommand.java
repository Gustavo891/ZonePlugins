package org.gustaav.zoneMissions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneMissions.ZoneMissions;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.TaskModel;
import org.gustaav.zoneMissions.view.MissionGUI;
import revxrsal.commands.annotation.Command;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MissionCommand {

    ZoneMissions zoneMissions;
    MissionManager manager;
    public MissionCommand(ZoneMissions zoneMissions) {
        this.zoneMissions = zoneMissions;
        this.manager = zoneMissions.getManager();
    }

    @Command("missao")
    public void missao(CommandSender sender) {
        if(!(sender instanceof Player player)) return;

        UUID uuid = player.getUniqueId();
        Optional<PlayerModel> optionalPlayerData = manager.getPlayers().stream()
                .filter(playerData -> playerData.getUuid().equals(uuid))
                .findFirst();

        if (optionalPlayerData.isPresent()) {
            PlayerModel playerData = optionalPlayerData.get();
            MissionGUI missionGUI = new MissionGUI(playerData, player, zoneMissions);
        } else {
            player.sendMessage("§cJogador não encontrado.");
        }

    }

}
