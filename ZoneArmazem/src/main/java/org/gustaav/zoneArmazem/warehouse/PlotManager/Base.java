package org.gustaav.zoneArmazem.warehouse.PlotManager;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.dao.Manager;
import org.gustaav.zoneArmazem.warehouse.models.DropModel;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.util.List;
import java.util.Map;

public class Base {

    Manager manager;
    @Getter
    int nivelLimite = 6;
    double baseValue = 500000;

    @Getter
    List<DropModel> drops = List.of(
            new DropModel(Material.MELON_SLICE, "Melancia", 9),
            new DropModel(Material.CARROT, "Cenoura", 25),
            new DropModel(Material.POTATO, "Batata", 25),
            new DropModel(Material.CACTUS, "Cactu", 5),
            new DropModel(Material.WHEAT, "Trigo", 20),
            new DropModel(Material.NETHER_WART, "Fungo do Nether", 15),
            new DropModel(Material.PUMPKIN, "Abôbora", 15)
    );

    public Base(ZoneArmazem zoneArmazem) {
        manager = zoneArmazem.getManager();
    }

    public Plot getPlot(Location loc) {
        com.plotsquared.core.location.Location plotLoc = BukkitUtil.adapt(loc);
        PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotLoc);

        if (plotArea == null) {
            return null;
        }
        Plot plot = plotArea.getPlot(plotLoc);
        if (plot == null || plot.getOwner() == null) {
            return null;
        }
        return plot;
    }

    public int getCapacidade(int nivel) {
        return switch (nivel) {
            case 1 -> 3000;
            case 2 -> 6000;
            case 3 -> 10000;
            case 4 -> 15000;
            case 5 -> 25000;
            default -> 40000; // maior que 5 retorna 40k
        };
    }

    public double getValue(int nivel) {
        return baseValue * Math.pow(2.5, nivel);
    }

    public int getTotal(WarehouseModel warehouseModel) {
        int total = 0;
        if(warehouseModel.getDrops().isEmpty()) {
            return 0;
        }
        for(Map.Entry<Material, Integer> entry : warehouseModel.getDrops().entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public static String formatNumber(double number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000);
        }
        else if (number >= 1_000) {
            return String.format("%.1fk", number / 1_000);
        }
        else {
            return String.valueOf((int) number);
        }
    }


}
