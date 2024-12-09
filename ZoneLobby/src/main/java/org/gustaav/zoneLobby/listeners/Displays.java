package org.gustaav.zoneLobby.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.manager.LocationManager;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class Displays implements Listener {

    private final LocationManager locationManager;
    private final NamespacedKey displayKey;
    ZoneLobby plugin;

    public Displays(LocationManager locationManager, ZoneLobby plugin) {
        this.locationManager = locationManager;
        this.displayKey = new NamespacedKey(plugin, "zonelobby_display");
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        createDisplays();
    }

    private void createDisplays() {
        Location loc = locationManager.getRankupLocation();
        if (loc == null || loc.getWorld() == null) return;

        TextDisplay textDisplay;
        Location loc2 = loc.clone().add(0, 1.5, 0);
        loc2.setYaw(0);
        loc2.setPitch(0);
        textDisplay = loc.getWorld().spawn(loc2, TextDisplay.class);

        textDisplay.getPersistentDataContainer().set(displayKey, PersistentDataType.STRING, "text_display");

        Transformation transformation = new Transformation(
                new Vector3f(0, 0, 0), // Posição (sem mudança)
                new AxisAngle4f(0, 0, 0, 0), // Sem rotação (sem inclinação)
                new Vector3f(1.2F, 1.2F, 1.2F), // Escala (alterar o tamanho)
                new AxisAngle4f(0, 0, 0, 0)  // Sem rotação adicional
        );

        String miniMessageText = "\n<color:#ff6f00><b>RANKUP ORIGINS</b></color>\n" +
                "<color:#d4d4d4>Em desenvolvimento</color>\n\n" +
                "Mais informações em:\n" +
                "<color:#d1d43d>     discord.enderzone.xyz     </color>\n";

        Component component = MiniMessage.miniMessage().deserialize(miniMessageText)
                .decoration(TextDecoration.ITALIC, false);;

        textDisplay.text(component);
        textDisplay.setTransformation(transformation);
        textDisplay.setBackgroundColor(Color.fromARGB(20, 0,0,0));

        ItemDisplay itemDisplay;
        itemDisplay = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(Material.VAULT));
        itemDisplay.clearTitle();
        itemDisplay.customName(null);
        itemDisplay.setBillboard(Display.Billboard.FIXED);

        // Aplicar inclinação ao ItemDisplay
        Transformation transformation2 = new Transformation(
                new Vector3f(0, 0, 0), // Posição
                new AxisAngle4f((float) -Math.PI, 1, 0, 0), // Rotação de 45 graus no eixo X
                new Vector3f(1.5F, 1.5F, 1.5F), // Escala
                new AxisAngle4f(0.7853f, 0, 1, 0)  // Rotação adicional no eixo Y
        );

        itemDisplay.setTransformation(transformation2);
        itemDisplay.getPersistentDataContainer().set(displayKey, PersistentDataType.STRING, "item_display");

        new BukkitRunnable() {
            public void run() {
                loc.getWorld().spawnParticle(Particle.PORTAL, loc.clone().add(0, 0, 0), 20);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L);

    }

    @EventHandler
    public void onServerClose(PluginDisableEvent event) {

        for(Entity entity: locationManager.getRankupLocation().getWorld().getEntities()) {
            if(entity instanceof ItemDisplay) {
                entity.remove();
            } else if (entity instanceof TextDisplay) {
                entity.remove();
            }
        }
    }

    @CommandPermission("zonelobby.admin")
    @Command("setrankuploc")
    public void setRankUPLoc(CommandSender sender) {
        if (!(sender instanceof Player player)) return;

        locationManager.setRankupLocation(player.getLocation());
        player.sendMessage("§dLocalização do display do RankUP salva com sucesso.");
    }
}
