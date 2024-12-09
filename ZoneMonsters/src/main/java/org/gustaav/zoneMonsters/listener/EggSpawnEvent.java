package org.gustaav.zoneMonsters.listener;

import com.plotsquared.bukkit.util.BukkitUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneMonsters.ZoneMonsters;
import org.gustaav.zoneMonsters.manager.MonsterManager;
import org.gustaav.zoneMonsters.models.MonsterModel;

public class EggSpawnEvent implements Listener {

    private final NamespacedKey BOSS_TYPE_KEY;
    private final NamespacedKey BOSS_STACK_KEY;
    private final NamespacedKey BOSS_HEALTH_KEY;

    private final MonsterManager monsterManager;
    ZoneMonsters zoneMonsters;

    public EggSpawnEvent(MonsterManager monsterManager, ZoneMonsters zoneMonsters) {
        this.monsterManager = monsterManager;
        this.zoneMonsters = zoneMonsters;
        this.BOSS_TYPE_KEY = new NamespacedKey(zoneMonsters, "boss_type");
        this.BOSS_STACK_KEY = new NamespacedKey(zoneMonsters, "boss_stack");
        this.BOSS_HEALTH_KEY = new NamespacedKey(zoneMonsters, "boss_health");
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        if (!data.has(zoneMonsters.getBossType(), PersistentDataType.STRING)) {
            return;
        }

        Location blockLocation = event.getClickedBlock().getLocation();
        com.plotsquared.core.location.Location plotLoc = BukkitUtil.adapt(blockLocation);

        if (plotLoc.isPlotRoad() || !plotLoc.isPlotArea() || plotLoc.isUnownedPlotArea()) {
            player.sendMessage("§cVocê não está em um terreno.");
            event.setCancelled(true);
            return;
        }

        String bossType = data.get(zoneMonsters.getBossType(), PersistentDataType.STRING);

        for(MonsterModel monster : monsterManager.getMonsters()) {
            if(monster.getId().equalsIgnoreCase(bossType)) {
                try {
                    spawnOrStackBoss(player, blockLocation, monster, bossType, item);
                    event.setCancelled(true);
                    return;
                } catch (Exception e) {
                    zoneMonsters.getLogger().severe("Error handling boss spawn: " + e.getMessage());
                    return;
                }
            }
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            event.setCancelled(true);
            return;
        }
    }

    private void spawnOrStackBoss(Player player, Location location, MonsterModel bossSettings, String bossType, ItemStack item) {
        LivingEntity existingBoss = findNearbyBoss(player, location);

        if (existingBoss == null) {
            spawnNewBoss(player, location, bossSettings, bossType, item);
        } else {
            stackExistingBoss(existingBoss, bossSettings, item, player);
        }
    }

    private void stackExistingBoss(LivingEntity existingBoss, MonsterModel bossSettings, ItemStack item, Player player) {
        PersistentDataContainer bossData = existingBoss.getPersistentDataContainer();

        int stackCount = bossData.getOrDefault(BOSS_STACK_KEY, PersistentDataType.INTEGER, 1) + 1;
        String bossName = bossSettings.getName();

        existingBoss.customName(MiniMessage.miniMessage().deserialize(String.format("<gray>[%sx] %s<bold>%s</bold> %s", stackCount, bossSettings.getMonsterType().getColorCode(), bossSettings.getMonsterType().getName(), bossName)));
        bossData.set(BOSS_STACK_KEY, PersistentDataType.INTEGER, stackCount);

       decrementEggQuantity(player, item);
    }

    private LivingEntity findNearbyBoss(Player player, Location location) {
        return player.getWorld().getNearbyEntities(location, 2, 2, 2).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(livingEntity -> livingEntity.getPersistentDataContainer().has(BOSS_TYPE_KEY, PersistentDataType.STRING))
                .findFirst()
                .orElse(null);
    }

    private void spawnNewBoss(Player player, Location location, MonsterModel bossSettings, String bossType, ItemStack item) {
        Location spawnLocation = location.clone().add(0, 1, 0);
        LivingEntity boss = (LivingEntity) player.getWorld().spawnEntity(spawnLocation, bossSettings.getMonsterEntity());

        boss.customName(MiniMessage.miniMessage().deserialize(String.format("<gray>[1x] %s<bold>%s</bold> %s", bossSettings.getMonsterType().getColorCode(), bossSettings.getMonsterType().getName(), bossSettings.getName())));
        boss.setAI(false);
        boss.setGravity(false);

        PersistentDataContainer bossData = boss.getPersistentDataContainer();
        bossData.set(BOSS_TYPE_KEY, PersistentDataType.STRING, bossType);
        bossData.set(BOSS_STACK_KEY, PersistentDataType.INTEGER, 1);
        bossData.set(BOSS_HEALTH_KEY, PersistentDataType.INTEGER, bossSettings.getHealth());

        decrementEggQuantity(player, item);
    }

    private void decrementEggQuantity(Player player, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        int amount = data.get(zoneMonsters.getBossQuantity(), PersistentDataType.INTEGER);
        if(amount > 1) {
            amount--;
            data.set(zoneMonsters.getBossQuantity(), PersistentDataType.INTEGER, amount);
            item.setItemMeta(itemMeta);
            monsterManager.updateLore(item, amount);
        } else {
            player.getInventory().remove(item);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getPersistentDataContainer().has(BOSS_TYPE_KEY, PersistentDataType.STRING)) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(BOSS_TYPE_KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

}
