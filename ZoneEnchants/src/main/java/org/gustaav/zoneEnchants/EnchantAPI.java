package org.gustaav.zoneEnchants;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneEnchants.enchantment.CustomEnchants.CustomManager;

public class EnchantAPI {

    private CustomManager customManager;
    private static EnchantAPI instance;

    public EnchantAPI(CustomManager customManager) {
        instance = this;
        this.customManager = customManager;
    }

    public static EnchantAPI getInstance () {
        return instance;
    }

    public boolean hasEnchant(ItemStack item, String id) {
        return false;
    }

    public int getEnchantLevel(ItemStack item, String id) {
        if (item == null || !item.hasItemMeta()) {
            return -1;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (id.equals("explosivo")) {
            if (container.has(customManager.getExplosiveKey(), PersistentDataType.INTEGER)) {
                return container.get(customManager.getExplosiveKey(), PersistentDataType.INTEGER);
            }
            return -1;
        }
        return -1;
    }

}
