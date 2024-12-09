package org.gustaav.zoneMissions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.gustaav.zoneArmazem.warehouse.listener.ArmazemEvent;
import org.gustaav.zoneMissions.ZoneMissions;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.TaskModel;
import org.gustaav.zoneMissions.models.TaskTypes;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ArmazemSoldEvent implements Listener {

    MissionManager manager;

    public ArmazemSoldEvent(ZoneMissions missions) {
        this.manager = missions.getManager();
    }

    @EventHandler
    public void onArmazemSold(ArmazemEvent e) {

        Player p = e.player();
        UUID uuid = p.getUniqueId();

        Optional<PlayerModel> optionalPlayerData = manager.getPlayers().stream()
                .filter(playerData -> playerData.getUuid().equals(uuid))
                .findFirst();

        if (optionalPlayerData.isEmpty()) {
            p.sendMessage("§cJogador não encontrado.");
            return;
        }

        PlayerModel playerModel = optionalPlayerData.get();
        for (Map.Entry<TaskModel, Integer> task : playerModel.getTaskProgress().entrySet()) {
            TaskModel taskModel = task.getKey();
            int progresso = task.getValue();
            if (taskModel.getType() == TaskTypes.ARMAZEM_SELL) {
                if (progresso >= taskModel.getAmount()) {
                    continue;
                }
                if (taskModel.getMaterial() == null || taskModel.getMaterial() == e.material()) {
                    int novoProgresso = progresso + e.amount();
                    playerModel.getTaskProgress().put(taskModel, novoProgresso);
                    if (manager.checkComplete(playerModel)) {
                        manager.nextMission(playerModel, p);
                        return;
                    }
                }
            }
        }

    }

}
