package org.gustaav.zoneEnchants.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.gustaav.zoneEnchants.view.EnchantGUI;

public class EnchantmentEvent implements Listener {

    @EventHandler
    public void enchantingTable(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || block.getType() != Material.ENCHANTING_TABLE) {
            return;
        }

        event.setCancelled(true);
        EnchantGUI enchantGUI = new EnchantGUI(player);
    }










}
