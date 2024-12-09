package org.gustaav.zoneLobby.listeners;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.views.ServerSelector;

public class ServerSelectorEvent implements Listener {

    ZoneLobby plugin;

    public ServerSelectorEvent(ZoneLobby plugin) {
        this.plugin = plugin;
    }


    public ItemStack getServerSelector() {
        ItemStack item = new ItemStack(Material.RECOVERY_COMPASS);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.displayName(Component.text("§aSelecione um servidor"));
        meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
        Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
        if(modifiers == null) {
            modifiers = HashMultimap.create();
            meta.setAttributeModifiers(modifiers);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Verificar se o jogador está clicando no item do ServerSelector
        if (event.getItem() != null && event.getItem().getType() == Material.RECOVERY_COMPASS) {
            // Impede interações com o item se estiver no menu
            new ServerSelector(plugin, player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.equals(player.getInventory())) {
            event.setCancelled(true);
        }
    }
}



