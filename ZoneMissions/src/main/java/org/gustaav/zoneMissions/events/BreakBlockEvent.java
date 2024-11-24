package org.gustaav.zoneMissions.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.gustaav.zoneMissions.manager.MissionCreator;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.TaskModel;
import org.gustaav.zoneMissions.models.TaskTypes;

import javax.swing.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BreakBlockEvent implements Listener {

    MissionManager manager;

    public BreakBlockEvent(MissionManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Obtém o PlayerModel do jogador
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
            if (taskModel.getType() == TaskTypes.BREAK_BLOCKS) {
                if (progresso >= taskModel.getAmount()) {
                    continue;
                }
                if (taskModel.getMaterial() == null || taskModel.getMaterial() == e.getBlock().getType()) {
                    int novoProgresso = progresso + 1;
                    playerModel.getTaskProgress().put(taskModel, novoProgresso);
                    String barraProgresso = MissionCreator.criarBarraProgresso(novoProgresso, taskModel.getAmount());

                    if (manager.checkComplete(playerModel)) {
                        manager.nextMission(playerModel, e.getPlayer());
                        return;
                    }
                    p.sendActionBar(
                            Component.text("§e" + taskModel.getDescription() + ": ")
                                    .append(Component.text("§7" + novoProgresso + "/" + taskModel.getAmount()))
                                    .append(Component.text(" §2" + barraProgresso))
                    );
                }

            }
        }


    }

}
