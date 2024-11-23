package org.gustaav.zoneMonsters.manager;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneMonsters.ZoneMonsters;
import org.gustaav.zoneMonsters.models.MonsterModel;
import org.gustaav.zoneMonsters.models.RewardModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MonsterManager {

    @Getter
    private final NamespacedKey BOSS_TYPE_KEY;
    @Getter
    private final NamespacedKey BOSS_STACK_KEY;
    @Getter
    private final NamespacedKey BOSS_HEALTH_KEY;

    ZoneMonsters plugin;
    @Getter
    List<MonsterModel> monsters = new ArrayList<>();

    public MonsterManager(ZoneMonsters plugin) {
        this.plugin = plugin;
        this.BOSS_TYPE_KEY = new NamespacedKey(plugin, "boss_type");
        this.BOSS_STACK_KEY = new NamespacedKey(plugin, "boss_stack");
        this.BOSS_HEALTH_KEY = new NamespacedKey(plugin, "boss_health");
    }

    public void addMonster(MonsterModel monster) {
        monsters.add(monster);
    }

    public void deliveryRewards(Player player, MonsterModel monsterModel) {
        for(RewardModel rewardModel : monsterModel.getRewards()) {
            int random = ThreadLocalRandom.current().nextInt(0, 100);
            if(rewardModel.getChance() > random) {
                String name = rewardModel.getName();
                String command = rewardModel.getCommandToExecute().replaceAll("%player%", player.getName());
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
    }

    public ItemStack giveMonster(MonsterModel monster, int amount) {
        ItemStack egg = monster.getMonsterItem();
        ItemMeta meta = egg.getItemMeta();

        var container = meta.getPersistentDataContainer();
        container.set(plugin.getBossType(), PersistentDataType.STRING, monster.getId());
        container.set(plugin.getBossQuantity(), PersistentDataType.INTEGER, amount);
        egg.setItemMeta(meta);

        updateLore(egg, amount);

        return egg;
    }

    public void updateLore(ItemStack egg, int amount) {

        ItemMeta eggMeta = egg.getItemMeta();
        PersistentDataContainer container = eggMeta.getPersistentDataContainer();

        DecimalFormat formatter = new DecimalFormat("#,###");
        String type = container.get(plugin.getBossType(), PersistentDataType.STRING);

        for(MonsterModel monster : monsters) {
            assert type != null;
            if(type.equals(monster.getId())) {
                String health = formatter.format(monster.getHealth());
                String colorCode = monster.getMonsterType().getColorCode();
                String typeName = monster.getMonsterType().getName();
                Component component = MiniMessage.miniMessage().deserialize(
                                String.format("%s<b>%s</b> %s%s <gray>[x%s]", colorCode, typeName, colorCode, monster.getName(), amount))
                        .decoration(TextDecoration.ITALIC, false);
                eggMeta.displayName(component);
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§7Invoque monstros em seu"));
                lore.add(Component.text("§7terreno para conseguir"));
                lore.add(Component.text("§7recompensas incríveis."));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§f  Vida: §c❤" + health));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§8Clique no chão para invoca-lo."));
                eggMeta.lore(lore);

                egg.setItemMeta(eggMeta);
                return;
            }
        }
    }






}
