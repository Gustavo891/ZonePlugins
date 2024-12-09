package org.gustaav.zoneMonsters.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneEnchants.EnchantAPI;
import org.gustaav.zoneMonsters.ZoneMonsters;
import org.gustaav.zoneMonsters.damage.hologram.CreateHologramTask;
import org.gustaav.zoneMonsters.damage.hologram.HologramManager;
import org.gustaav.zoneMonsters.damage.vector.VectorGenerator;
import org.gustaav.zoneMonsters.manager.MonsterManager;
import org.gustaav.zoneMonsters.models.MonsterModel;

import java.text.DecimalFormat;

public class MonsterDamageEvent implements Listener {

    MonsterManager monsterManager;
    HologramManager hologramManager;
    VectorGenerator vectorGenerator;
    ZoneMonsters plugin;

    public MonsterDamageEvent(MonsterManager monsterManager, HologramManager hologramManager, VectorGenerator vectorGenerator, ZoneMonsters plugin) {
        this.monsterManager = monsterManager;
        this.hologramManager = hologramManager;
        this.vectorGenerator = vectorGenerator;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || !(event instanceof EntityDamageByEntityEvent damageEvent)) {
            return; // Early return para entidades não-vivas
        }

        PersistentDataContainer entityData = entity.getPersistentDataContainer();
        if (!entityData.has(monsterManager.getBOSS_TYPE_KEY(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        Entity damager = damageEvent.getDamager();
        if (!(damager instanceof Player player)) {
            event.setCancelled(true);
            return;
        }

        handleBossDamage(event, entityData, entity, player);

    }

    public void handleBossDamage(EntityDamageEvent event, PersistentDataContainer entityData, LivingEntity entity, Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        MonsterModel monster = null;
        for(MonsterModel model : monsterManager.getMonsters()) {
            if(model.getId().equalsIgnoreCase(entityData.get(monsterManager.getBOSS_TYPE_KEY(), PersistentDataType.STRING))) {
                monster = model;
            }
        }
        if(monster == null) {
            return;
        }

        if(EnchantAPI.getInstance().hasEnchant(item, "forcabrutal")) {
            int level = EnchantAPI.getInstance().getEnchantLevel(item, "forcabrutal");
            double finaldamage = event.getDamage() * Math.pow(level, 1.5);
            event.setDamage(finaldamage);
        }

        int currentHealth = entityData.get(monsterManager.getBOSS_HEALTH_KEY(), PersistentDataType.INTEGER);
        int newHealth = (int) (currentHealth - event.getDamage());
        lifeMessage(newHealth, barraProgresso(newHealth, monster.getHealth()), (int) event.getDamage(), player);

        CreateHologramTask createHologramTask = new CreateHologramTask(plugin, vectorGenerator, event, hologramManager);
        createHologramTask.run();

        if(newHealth <= 0) {
            handleBossDeath(entity, entityData, player, monster);
        } else {
            entityData.set(monsterManager.getBOSS_HEALTH_KEY(), PersistentDataType.INTEGER, newHealth);
        }
        event.setDamage(0);

    }

    public void handleBossDeath(Entity entity, PersistentDataContainer entityData, Player player, MonsterModel monster) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        killMessage(monster.getName(), player);
        entity.getWorld().spawnParticle(Particle.EXPLOSION, entity.getLocation(), 10, 0.1, 0.1, 0.1, 0.02);

        int stackCount = entityData.get(monsterManager.getBOSS_STACK_KEY(), PersistentDataType.INTEGER);
        if(stackCount > 1) {
            entityData.set(monsterManager.getBOSS_STACK_KEY(), PersistentDataType.INTEGER, stackCount - 1);
            entityData.set(monsterManager.getBOSS_HEALTH_KEY(), PersistentDataType.INTEGER, monster.getHealth());
            entity.setCustomName("§e§lx" + (stackCount - 1) + "§r§e " + monster.getName());
        } else {
            entity.remove();
            monsterManager.deliveryRewards(player, monster);
        }
    }
    public void killMessage(String type, Player player) {
        TextComponent message = new TextComponent();
        message.setText("§fMatou! §eBoss§l " + type);
        message.setColor(ChatColor.of("#D45D67"));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
    }
    public void lifeMessage(int currentHealth, String progresso, int damage, Player player) {
        TextComponent message = new TextComponent();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        message.setText(formatter.format(currentHealth) + "❤ " + progresso + " §7-" + (int) damage + "HP");
        message.setColor(ChatColor.of("#D45D67"));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
    }
    public String barraProgresso(int xp, int necessario) {

        int barrasPreenchidas;
        int barrasVazias;
        int barrasTotais = 10;
        double percentual = ((double) xp /necessario)*100;
        percentual = Math.min(percentual, 100);

        if(percentual <= 10) {
            barrasPreenchidas = 1;
            barrasVazias = 9;
        } else {
            barrasPreenchidas = (int) (percentual / 100 * barrasTotais);
            barrasVazias = barrasTotais - barrasPreenchidas;
        }

        return "§2" + "⬛".repeat(Math.max(0, barrasPreenchidas)) +
                "§8" +
                "⬛".repeat(Math.max(0, barrasVazias));
    }

}
