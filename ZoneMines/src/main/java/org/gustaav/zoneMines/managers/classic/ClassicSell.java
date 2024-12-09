package org.gustaav.zoneMines.managers.classic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneMines.ZoneMines;
import revxrsal.commands.annotation.Command;

import java.util.Objects;

public class ClassicSell {

    NamespacedKey guardianKey;
    LivingEntity guardian;

    Location guardianLocation = new Location(Bukkit.getWorld("minas"), 96, 88, 78);

    public ClassicSell(ZoneMines plugin) {
        guardianKey = new NamespacedKey(plugin, "guardian");
        loadGuardian();
    }

    public void loadGuardian() {
            for (Entity entity : Objects.requireNonNull(Bukkit.getWorld("minas")).getEntities()) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (entity.getPersistentDataContainer().has(guardianKey, PersistentDataType.BOOLEAN)) {
                        guardian = livingEntity;
                        return;
                    }
                }
            }

        if (guardian == null) {
            System.out.println("Guardian not found, creating new one.");
            guardian = (LivingEntity) guardianLocation.getWorld().spawnEntity(guardianLocation, EntityType.ELDER_GUARDIAN);
            guardian.setInvulnerable(true);
            guardian.setAI(false);
            guardian.setGravity(false);
            guardian.setSilent(true);
            guardian.setPersistent(true);
            guardian.getPersistentDataContainer().set(guardianKey, PersistentDataType.BOOLEAN, true);
        }
    }



}
