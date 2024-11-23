package org.gustaav.zoneArmazem.warehouse.dao;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.PlotId;
import org.bukkit.Bukkit;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private Base base;
    private final List<WarehouseModel> armazens = new ArrayList<>();

    public Manager(Base base) {
        this.base = base;
    }

    public List<WarehouseModel> getArmazens() {
        return armazens;
    }

    public void setArmazens(List<WarehouseModel> armazens) {
        this.armazens.clear();
        this.armazens.addAll(armazens);
    }

    public void addWarehouse(WarehouseModel warehouse) {
        armazens.add(warehouse);
    }

    public void removeWarehouse(WarehouseModel warehouse) {
        armazens.remove(warehouse);
    }

    public WarehouseModel getWarehouseByPlotId(String plotId) {
        for (WarehouseModel warehouse : armazens) {
            if (warehouse.getPlotId().getId().toString().equals(plotId)) {
                return warehouse;
            }
        }
        PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea("plots", "1");
        assert plotArea != null;
        WarehouseModel newWarehouse = new WarehouseModel(plotArea.getPlot(PlotId.fromString(plotId)), 1, true);
        addWarehouse(newWarehouse);
        return newWarehouse;
    }
}
