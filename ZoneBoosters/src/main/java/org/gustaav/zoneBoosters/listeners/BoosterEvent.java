package org.gustaav.zoneBoosters.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneBoosters.manager.BoosterManager;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;

public class BoosterEvent implements Listener {

    BoosterManager boosterManager;

    public BoosterEvent(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @EventHandler
    public void onBoosterEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!(item.getPersistentDataContainer().has(boosterManager.getTypeKey()))) {
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            BoosterTypes type = BoosterTypes.valueOf(item.getPersistentDataContainer().get(boosterManager.getTypeKey(), PersistentDataType.STRING));
            int multiplier = item.getPersistentDataContainer().get(boosterManager.getMultiplierKey(), PersistentDataType.INTEGER);
            int duration = item.getPersistentDataContainer().get(boosterManager.getDurationKey(), PersistentDataType.INTEGER);

            if(boosterManager.hasTypeBooster(player.getUniqueId(), type)) {
                player.sendMessage("§cVocê já possui um booster desse tipo ativo.");
                return;
            }
            player.getInventory().remove(item);
            boosterManager.addBooster(new PlayerModel(player.getUniqueId(), type, duration  * 3600000L, multiplier));
            player.sendMessage(String.format("§aVocê ativou o estimulante §f'%s' §acom sucesso.", type.toString()));
        }

    }


}
