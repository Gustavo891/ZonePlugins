package org.gustaav.zoneArmazem.warehouse.PlotManager;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.models.DropModel;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.util.Map;

public class DropListener implements Listener {

    private final Base base;
    private final ZoneArmazem plugin;

    public DropListener(Base base, ZoneArmazem zoneArmazem) {
        this.base = base;
        this.plugin = zoneArmazem;
    }

    @EventHandler
    public void onDropItem(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();
        Location loc = item.getLocation();
        com.plotsquared.core.location.Location plotLoc = BukkitUtil.adapt(loc);
        PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotLoc);

        if (plotArea == null) {
            return;
        }

        Plot plot = plotArea.getPlot(plotLoc);
        if (plot == null || plot.getOwner() == null) {
            return;
        }


        WarehouseModel warehouse = plugin.getManager().getWarehouseByPlotId(plot.getId().toString());
        int capacidade = base.getCapacidade(warehouse.getNivel());
        int total = base.getTotal(warehouse);
        int slots = capacidade - total;

        for(DropModel drop : base.getDrops()) {
            if(drop.getDrop() == itemStack.getType()) {
                warehouse.addDrop(itemStack.getType(), Math.min(slots, item.getItemStack().getAmount()));
                event.setCancelled(true);
            }
        }

    }

}
