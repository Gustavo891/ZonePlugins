package org.gustaav.zoneMissions.manager;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMissions.ZoneMissions;
import org.gustaav.zoneMissions.models.MissionModel;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.RewardModel;
import org.gustaav.zoneMissions.models.TaskModel;
import org.gustaav.zoneMissions.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class MissionManager {

    private final MissionCreator missionCreator;
    @Getter
    List<PlayerModel> players = new ArrayList<PlayerModel>();

    private final ZoneMissions zoneMissions;
    public MissionManager(ZoneMissions zoneMissions) {
        this.zoneMissions = zoneMissions;
        this.missionCreator = zoneMissions.getMissionCreator();
    }
    public boolean checkComplete(PlayerModel playerModel) {
        MissionModel currentMission = playerModel.getMission();
        for(TaskModel taskModel : currentMission.getTasks()) {
            int value = playerModel.getTaskProgress().get(taskModel);
            if(value < taskModel.getAmount()) {
                // caso o valor seja menor que o necessário, ele retorna falso
                return false;
            }
        }
        // caso todos valores sejam maiores, ou iguais, ele retorna verdadeiro
        return true;

    }
    public void nextMission(PlayerModel playerModel, Player player) {
        MissionModel currentMission = playerModel.getMission();
        int index = missionCreator.getMissions().indexOf(currentMission);
        MissionModel nextMission = missionCreator.getMissions().get(index + 1);
        setMission(playerModel, nextMission, player);
        MessageUtil.sendFormattedMessage(player,
                String.format("<b><green>WOW!</b> <green>Você completou a missão '<white>%s</white>', suas recompensas foram entregues!", currentMission.getName()));

        for(RewardModel rewardModel : currentMission.getRewards()) {
            String toExecute = rewardModel.getCommandToExecute().replaceAll("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), toExecute);
        }

    }
    public void resetAccount(PlayerModel playerModel, Player player) {
        setMission(playerModel, missionCreator.getMissions().getFirst(), player);
    }
    public void setMission(PlayerModel playerModel, MissionModel mission, Player player) {
        playerModel.setMission(mission);
        playerModel.getTaskProgress().clear();
        for(TaskModel task : mission.getTasks()) {
            playerModel.getTaskProgress().put(task, 0);
        }
        sendStartMissionMessage(player, mission);
    }

    public void sendStartMissionMessage(Player player, MissionModel mission) {
        new BukkitRunnable() {
            @Override
            public void run() {

                String desc = mission.getDescription();
                String[] lines = desc.split("\n");

                player.sendMessage("§r\n" +
                        "  §e§lNOVA MISSAO! §r§e" + mission.getName());
                for (String line : lines) {
                    player.sendMessage("§7  " + line);
                }
                player.sendMessage("§r");
                for(TaskModel task : mission.getTasks()) {
                    player.sendMessage(String.format("  §8⚑ §7x%s §f%s", task.getAmount(), task.getDescription()));
                }
                player.sendMessage("§r\n" +
                        "  §eBoa sorte em sua aventura, jogador!\n§r");
            }
        }.runTaskLater(zoneMissions, 200L);

    }

}
