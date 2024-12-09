package org.gustaav.zoneLobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.manager.LocationManager;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Events implements Listener {

    List<UUID> players = new ArrayList<UUID>();
    LocationManager locationManager;
    ZoneLobby zoneLobby;

    public Events(LocationManager locationManager, ZoneLobby zoneLobby) {
        this.locationManager = locationManager;
        this.zoneLobby = zoneLobby;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(locationManager.getSpawnLocation());
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        event.getPlayer().setHealthScale(1);
        event.joinMessage(null);
        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().setItem(4, zoneLobby.getServerSelector().getServerSelector());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void dragEvent(InventoryInteractEvent event) {
        if(!players.contains(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        zoneLobby.getQueueManager().getServerQueues().values().forEach(queue -> queue.remove(event.getPlayer()));
        event.quitMessage(null);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(null);
        event.getDrops().clear();
    }
    @EventHandler(priority= EventPriority.LOW)
    public void onCreateSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void brockBleak(BlockBreakEvent event) {
        if(!players.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        if(!players.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void burnEvent(BlockBurnEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void igniteEvent(BlockIgniteEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void explodeEvent(EntityExplodeEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void hungerEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @CommandPermission("zonelobby.build")
    @Command("build")
    public void build(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }
        if(players.contains(player.getUniqueId())) {
            players.remove(player.getUniqueId());
            sender.sendMessage("§cVocê desativou o modo de construção.");
        } else {
            players.add(player.getUniqueId());
            sender.sendMessage("§aVocê ativou o modo de construção.");
        }
    }

}
