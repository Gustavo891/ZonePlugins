package org.gustaav.zoneBoosters.listeners;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.gustaav.zoneBoosters.manager.BoosterManager;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;

import java.util.List;

public class PlantacaoBooster implements Listener {

    BoosterManager boosterManager;

    public PlantacaoBooster(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @EventHandler
    public void onDrop(BlockBreakEvent e) {
        Player player = e.getPlayer();

        Location loc = e.getBlock().getLocation();
        if(!(loc.getWorld() == Bukkit.getWorld("plots"))) {
            return;
        }

        PlayerModel booster = boosterManager.getTypeBooster(player.getUniqueId(), BoosterTypes.PLANTACAO);

        if(booster == null) {
            return;
        }

        int multiplier = booster.getMultiplier();
            Material brokenBlock = e.getBlock().getType();
            List<Material> list = List.of(Material.WHEAT, Material.CARROTS, Material.CACTUS, Material.POTATOES, Material.MELON, Material.NETHER_WART);
            if (list.contains(brokenBlock)) {
                e.getBlock().getDrops(player.getInventory().getItemInMainHand()).forEach(drop -> {
                    for (int i = 0; i < (multiplier - 1); i++) {
                        e.getBlock().getWorld().dropItemNaturally(loc, drop);
                    }
                });
            }


    }

}
