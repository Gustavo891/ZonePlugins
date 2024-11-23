package org.gustaav.zoneArmazem;

import com.plotsquared.core.plot.Plot;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.dao.Manager;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

public class ArmazemAPI {

    private Base base;
    private Manager manager;
    private static ArmazemAPI instance;

    public ArmazemAPI(Base base, Manager manager) {
        instance = this;
        this.base = base;
        this.manager = manager;
    }

    public static ArmazemAPI getInstance () {
        return instance;
    }

    public Base getBase() {
        return base;
    }
    public Manager getManager() {
        return manager;
    }

    public WarehouseModel getWarehouse(Plot plot) {
        return manager.getWarehouseByPlotId(plot.getId().toString());
    }


}
