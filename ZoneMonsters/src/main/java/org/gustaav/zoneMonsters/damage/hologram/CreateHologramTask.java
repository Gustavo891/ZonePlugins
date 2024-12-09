package org.gustaav.zoneMonsters.damage.hologram;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.gustaav.zoneMonsters.ZoneMonsters;
import org.gustaav.zoneMonsters.damage.vector.VectorGenerator;

public class CreateHologramTask implements Runnable {

    private final ZoneMonsters plugin;
    private final EntityDamageEvent e;
    private final VectorGenerator vectorGenerator;
    private final HologramManager hologramManager;

    private Player playerDamager = null;

    private final ChatColor RED  = ChatColor.DARK_RED;

    public CreateHologramTask(ZoneMonsters plugin, VectorGenerator vectorGenerator, EntityDamageEvent e, HologramManager hologramManager)
    {
        this.plugin = plugin;
        this.e = e;
        this.vectorGenerator = vectorGenerator;
        this.hologramManager = hologramManager;
    }
    @Override
    public void run()
    {
        double dmgFinal = e.getDamage();
        Entity victim = e.getEntity();

        double radius = 1.0;
        double offsetX = (Math.random() - 0.5) * 2 * radius; // Gera entre -radius e +radius
        double offsetZ = (Math.random() - 0.5) * 2 * radius; // Gera entre -radius e +radius

        // Calcula a nova posição da ArmorStand
        Location hologramLocation = victim.getLocation().clone().add(offsetX, 0, offsetZ);


        ArmorStand hologram = victim.getWorld().spawn(hologramLocation, ArmorStand.class, stand -> {
                    stand.setVisible(false);
                    stand.setBasePlate(false);
                    stand.setCollidable(false);
                    stand.setArms(false);
                    stand.setSmall(true);
                    stand.setSilent(true);
                    stand.setCanPickupItems(false);
                    stand.setGliding(true);
                    stand.getPersistentDataContainer().set(new NamespacedKey(plugin, "hologram"), PersistentDataType.STRING, "true");
                    stand.setLeftLegPose(EulerAngle.ZERO.add(180, 0, 0));
                    stand.setRightLegPose(EulerAngle.ZERO.add(180, 0, 0));
                    stand.setInvulnerable(true);
                    Vector velocity = new Vector(0, 0.4, 0); // Sobe a 0.5 blocos por tick
                    stand.setVelocity(velocity);
        });
        hologramManager.addHologram(hologram);

        String customName = String.format(RED + "-%s ❤", (int) dmgFinal);

        //checking if a critical hit
        if(playerDamager != null)
        {
            if(playerDamager.getFallDistance() > 0
                    && !(((Entity) playerDamager).isOnGround())
                    && !(playerDamager.getLocation().getBlock().getType().equals(Material.VINE))
                    && !(playerDamager.getLocation().getBlock().getType().equals(Material.LADDER))
                    && !(playerDamager.getLocation().getBlock().getType().equals(Material.WATER))
                    && !(playerDamager.getLocation().getBlock().getType().equals(Material.LAVA))
                    && !(playerDamager.hasPotionEffect(PotionEffectType.BLINDNESS))
                    && playerDamager.getVehicle() == null)
            {
                customName = customName + ChatColor.DARK_RED + "" + ChatColor.ITALIC + ChatColor.BOLD + " Crit!";
            }
        }

        hologram.setCustomNameVisible(true);
        hologram.setCustomName(customName);

        //victim.getWorld().spawnParticle(Particle.HEART, victim.getLocation().add(0, 2.5,0), 1);

        CleanupHologramTask cleanupTask = new CleanupHologramTask(hologram, hologramManager);
        cleanupTask.runTaskLater(plugin, 10);
    }
}
