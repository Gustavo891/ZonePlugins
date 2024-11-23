package org.gustaav.zoneArmazem.warehouse.listener;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.dao.Manager;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

public class PlotDeleteEvent {

    Manager manager;

    public PlotDeleteEvent(Manager manager, Base base) {
        this.manager = manager;
        PlotAPI api = new PlotAPI();
        api.registerListener(this);
    }

    @Subscribe
    public void onPlotDelete(com.plotsquared.core.events.PlotDeleteEvent event) {
        Plot plot = event.getPlot();
        WarehouseModel warehouseModel = manager.getWarehouseByPlotId(String.valueOf(plot.getId()));
        manager.getArmazens().remove(warehouseModel);
    }



}
