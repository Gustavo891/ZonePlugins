package org.gustaav.zoneMines.Listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.gustaav.zoneMines.modules.SellModule;
import org.gustaav.zoneMines.containers.LapisSell;

public class MinesListener implements Listener {

    SellModule sellModule;

    public MinesListener(SellModule sellModule) {
        this.sellModule = sellModule;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onClick(NPCRightClickEvent e) {
        if(e.getNPC().getId() == 0) {
            LapisSell lapis = new LapisSell(e.getClicker().getPlayer(), sellModule);
            lapis.loadSellItems();
        }
    }

}
