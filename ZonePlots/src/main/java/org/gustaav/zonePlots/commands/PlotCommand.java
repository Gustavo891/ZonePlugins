package org.gustaav.zonePlots.commands;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zonePlots.views.PlotGUI;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;

public class PlotCommand {

    @Command({"plot <flag>", "plots <flag>", "terreno <flag>", "terrenos <flag>"})
    public void plot(CommandSender sender,@Optional @Named("flag") String[] flag) {
        if(!(sender instanceof Player player)) {
            return;
        }

        Plot plot = getPlot(player.getLocation());

        if(plot != null && flag == null) {
            PlotGUI plotGUI = new PlotGUI(plot, player);
            plotGUI.loadGui();
        } else {
            String command = "plotsquared:plot";
            if (flag != null && flag.length > 0) {
                command += " " + String.join(" ", flag);
            }
            player.performCommand(command);
        }

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

}
