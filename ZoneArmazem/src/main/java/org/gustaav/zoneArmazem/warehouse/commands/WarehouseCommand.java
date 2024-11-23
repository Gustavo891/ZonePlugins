package org.gustaav.zoneArmazem.warehouse.commands;

import com.plotsquared.core.plot.Plot;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.PlotManager.Base;
import org.gustaav.zoneArmazem.warehouse.gui.WarehouseGUI;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.util.Objects;

public class WarehouseCommand extends Command{

    Base base;
    WarehouseGUI warehouseGUI;
    ZoneArmazem zoneArmazem;


    public WarehouseCommand(Base base, ZoneArmazem zoneArmazem) {
        super(
                "warehouse",
                "Comando para abrir seu armazem.",
                "§cUse /armazem.",
                new String[]{"armazem"},
                ""
        );
        this.base = base;
        this.zoneArmazem = zoneArmazem;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        Plot plot = base.getPlot(player.getLocation());
        if(plot == null) {
            player.sendMessage("§cVocê precisa executar esse comando em uma plot claimada.");
            return false;
        }
        String id = plot.getId().toString();
        if(args.length == 0) {
            WarehouseModel warehouse = zoneArmazem.getManager().getWarehouseByPlotId(id);
            if(plot.getTrusted().contains(player.getUniqueId()) || Objects.equals(plot.getOwner(), player.getUniqueId())) {
                warehouseGUI = new WarehouseGUI(base, player, warehouse);
                warehouseGUI.openMenu();
            } else {
                player.sendMessage("§cVocê não tem permissão para acessar esse armazém.");
            }
        }


        return false;
    }
}
