package org.gustaav.zoneMines.explosives;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.inventory.meta.ItemMeta;

import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.SellModel;
import org.gustaav.zoneMines.modules.SellModule;
import org.gustaav.zoneMines.managers.LapisManager;
import org.gustaav.zoneMines.utils.MessageUtil;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExplosivoListener implements Listener {

    private final SellModule sellModule;
    private final LapisManager lapisManager;
    private final ZoneMines plugin;
    private final NamespacedKey tierNamespaced = new NamespacedKey("minasistema", "tier");
    private final NamespacedKey tipoNamespaced = new NamespacedKey("minasistema", "tipo");

    public ExplosivoListener(ZoneMines zoneMines, LapisManager lapisManager, SellModule sellModule) {
        this.plugin = zoneMines;
        this.lapisManager = lapisManager;
        this.sellModule = sellModule;
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof TextDisplay) {
                    if (entity.getPersistentDataContainer().has(new NamespacedKey("minasistema", "tier"), PersistentDataType.BOOLEAN)) {
                        entity.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(tierNamespaced, PersistentDataType.INTEGER)) {
            return;
        }

        if (!(player.getWorld() == Bukkit.getWorld("minas"))) {
            event.setCancelled(true);
            player.sendMessage("§cO explosivo somente pode ser utilizado nas minas.");
            return;
        }
        player.getInventory().removeItem(item);
        event.setCancelled(true);
        Location location = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2));
        DroppedItem(player, item, location);
    }


    public void DroppedItem(Player player, ItemStack item, Location location) {
        int tier = item.getItemMeta().getPersistentDataContainer().get(tierNamespaced, PersistentDataType.INTEGER);
        String tipo = item.getItemMeta().getPersistentDataContainer().get(tipoNamespaced, PersistentDataType.STRING);

        Item droppedItem = Objects.requireNonNull(location.getWorld()).dropItem(location, item);
        droppedItem.setVelocity(player.getLocation().getDirection().multiply(0.5)); // Define a direção do arremesso
        droppedItem.setPickupDelay(Integer.MAX_VALUE); // Impede que o item seja coletado
        droppedItem.setInvulnerable(true);

        TextDisplay textDisplay = Objects.requireNonNull(droppedItem.getLocation().getWorld()).spawn(droppedItem.getLocation(), TextDisplay.class);
        textDisplay.getPersistentDataContainer().set(new NamespacedKey("minasistema", "textdisplay"), PersistentDataType.BOOLEAN, true);
        textDisplay.setTransformation(new Transformation(new Vector3f(0.0F, 0.4F, 0.0F), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.0F), new Quaternionf()));
        textDisplay.text(item.getItemMeta().displayName().appendNewline().append(Component.text("§fIrá explodir em §75s§f.")));
        textDisplay.setBillboard(Display.Billboard.VERTICAL);
        droppedItem.addPassenger(textDisplay);

        new BukkitRunnable() {
            int countdown = 6; // Segundos até a explosão

            @Override
            public void run() {
                if (droppedItem.isDead() || !droppedItem.isValid()) {
                    textDisplay.remove();
                    this.cancel();
                    return;
                }
                if (countdown > 0) {
                    countdown--;
                    textDisplay.text(item.getItemMeta().displayName().appendNewline().append(Component.text(String.format("§fIrá explodir em §7%ss§f.", countdown))));
                } else {
                    explode(droppedItem.getLocation(), player, tier, tipo, item);
                    textDisplay.remove();
                    droppedItem.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void explode(Location location, Player player, int tier, String tipo, ItemStack item) {
        int radius = 3;
        switch (tier) {
            case 2:
                radius = 5;
                break;
            case 3:
                radius = 7;
                break;
            default:
                break;
        }

        if(Objects.equals(tipo, "Duplicadora") && ThreadLocalRandom.current().nextBoolean()) {
            ItemStack duplicatedBomb = new ItemStack(item);
            ItemMeta meta = duplicatedBomb.getItemMeta();
            meta.getPersistentDataContainer().set(tipoNamespaced, PersistentDataType.STRING, "Normal");
            duplicatedBomb.setItemMeta(meta);
            DroppedItem(player, duplicatedBomb, location);
            MessageUtil.sendFormattedMessage(player, "${Colors.PURPLE_LIGHT}<b>WOW!</b> Sua bomba foi duplicada, tome cuidado.");
        }


        List<ItemStack> itemsToSell = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance <= radius) {
                        Block block = location.clone().add(x, y, z).getBlock();
                        if (lapisManager.getCuboid().contains(block.getLocation())) {
                            // Coletar o bloco como ItemStack
                            Material material = block.getType();
                            if (material != Material.AIR) {
                                if(tipo.equals("Toque-suave")) {
                                    itemsToSell.add(new ItemStack(material, 1));
                                } else {
                                    itemsToSell.addAll(block.getDrops());
                                }
                            }
                            block.setType(Material.AIR); // Define o bloco como ar
                        }
                    }
                }
            }
        }

        sellBombItems(player, tipo, itemsToSell);
        assert location.getWorld() != null;
        location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
    }

    public void sellBombItems(Player player, String tipo, List<ItemStack> itemsToSell) {
        int totalItems = itemsToSell.size();
        int totalValue = 0;
        if (tipo.equals("Fortuna")) {
            for (ItemStack item : itemsToSell) {
                for (SellModel sell : sellModule.getSellList()) {
                    if (item.getType() == sell.getItem().getType()) {
                        totalValue += item.getAmount() * sell.getValue();
                    }
                }
            }
            int randomInt = ThreadLocalRandom.current().nextInt(1, 40);
            totalValue += totalItems * (1 + (randomInt / 100));
            MessageUtil.sendFormattedMessage(player, String.format(
                    "${Colors.PURPLE_DARK}<b>Bomba </b><dark_gray>✦ <white>Quebrou ${Colors.PURPLE_LIGHT}%s <white>blocos por um total de <dark_green>$<green>%s<white>. <gray>[+%s%%]"
                    , SellModule.format(totalItems), SellModule.format(totalValue), randomInt));
            ZoneMines.getEconomy().depositPlayer(player, totalValue);
            return;
        }
        for (ItemStack item : itemsToSell) {
            for (SellModel sell : sellModule.getSellList()) {
                if (item.getType() == sell.getItem().getType()) {
                    totalValue += item.getAmount() * sell.getValue();
                }
            }
        }
        MessageUtil.sendFormattedMessage(player, String.format(
                "${Colors.PURPLE_DARK}<b>Bomba </b><dark_gray>✦ <white>Quebrou ${Colors.PURPLE_LIGHT}%s <white>blocos por um total de <dark_green>$<green>%s<white>."
                , SellModule.format(totalItems), SellModule.format(totalValue)));
        ZoneMines.getEconomy().depositPlayer(player, totalValue);
        return;

    }

}
