package org.gustaav.zonePacotes.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zonePacotes.ZonePacotes;
import org.gustaav.zonePacotes.packages.PackageManager;
import org.gustaav.zonePacotes.packages.PackageModel;
import org.gustaav.zonePacotes.packages.RewardDistributor;
import org.gustaav.zonePacotes.packages.RewardModel;

public class PackageOpener implements Listener {

    PackageManager pm;
    RewardDistributor distributor;
    public PackageOpener(ZonePacotes plugin) {
        this.pm = plugin.getPm();
        this.distributor = plugin.getDistributor();
    }


    @EventHandler
    public void openPackage(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack item = event.getItem();

        if(item == null || !item.hasItemMeta()) {
            return;
        }
        String type = pm.checkPackage(item);
        if(type == null) return;

        PackageModel model = pm.getPackage(type);
        RewardModel reward = distributor.selectReward(model.getRewards());
        distributor.giveReward(event.getPlayer(), reward);
        if(item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            event.getPlayer().getInventory().remove(item);
        }

    }

}
