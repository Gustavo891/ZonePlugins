package org.gustaav.zoneMines.managers.classic;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.SellModule;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ClassicGuardian implements Listener {

    NamespacedKey guardianKey;
    ZoneMines plugin;
    LapisManager lapisManager;

    Location guardianLocation = new Location(Bukkit.getWorld("minas"), 96, 85, 78);
    LivingEntity guardian;

    public ClassicGuardian(ZoneMines plugin) {
        this.plugin = plugin;
        this.lapisManager = plugin.getLapisManager();
        guardianKey = new NamespacedKey(plugin, "guardianKeyIdentifier");
        loadGuardian();

    }

    public void loadGuardian() {

        clearDisplays(guardianLocation.getWorld());
        guardianLocation.setPitch(1.6F);
        guardianLocation.setYaw((float) -142.4);

        TextDisplay textDisplay = guardianLocation.getWorld().spawn(guardianLocation, TextDisplay.class);
        textDisplay.setTransformation(new Transformation(new Vector3f(0.0F, 0.4F, 0.0F), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.0F), new Quaternionf()));
        textDisplay.text(
                MiniMessage.miniMessage().deserialize(
                        """
                                <color:#ffbb00><b>GUARDIÃO DA MINA</b></color>
                                <white>Venda seus recursos coletados
                                <white>em troca de moedas.

                                <gray>Clique no Guardião para acessar.</gray>""").decoration(TextDecoration.ITALIC, false));
        textDisplay.setBillboard(Display.Billboard.VERTICAL);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setPersistent(true);
        textDisplay.setShadowed(true);
        textDisplay.getPersistentDataContainer().set(guardianKey, PersistentDataType.BOOLEAN, true);
        guardian = (LivingEntity) guardianLocation.getWorld().spawnEntity(guardianLocation, EntityType.IRON_GOLEM);
        guardian.setInvulnerable(true);
        guardian.addPassenger(textDisplay);
        guardian.setAI(false);
        guardian.setSilent(true);
        guardian.setVelocity(new Vector(0, 0, 0));
        guardian.setPersistent(true);
        guardian.getPersistentDataContainer().set(guardianKey, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void openGuardian(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() == guardian) {
            new GuardianMenu(plugin, event.getPlayer());
        }

    }

    public void clearDisplays(World world) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof LivingEntity) {
                    if (entity.getPersistentDataContainer().has(guardianKey, PersistentDataType.BOOLEAN)) {
                        entity.remove();
                    }
                } else if (entity instanceof TextDisplay) {
                    if (entity.getPersistentDataContainer().has(guardianKey, PersistentDataType.BOOLEAN)) {
                        entity.remove();
                    }
                }
            }

    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if(event.getPlugin() == plugin.getZoneMines()) {
            try {
                clearDisplays(Bukkit.getWorld("minas"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if(event.getPlugin() == plugin.getZoneMines()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        clearDisplays(Bukkit.getWorld("minas"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLater(plugin, 200L);
        }
    }


}
