package org.gustaav.zoneEnchants.enchantment.CustomEnchants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.gustaav.zoneEnchants.enchantment.EnchantModel;
import org.gustaav.zoneEnchants.enchantment.EnchantTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomManager {

    Map<Integer, String> roman = Map.of(
            1, "I",
            2, "II",
            3, "III",
            4, "IV",
            5, "V"
    );
    NamespacedKey explosivo = new NamespacedKey("zoneenchants", "explosivo");

    public boolean setCustomEnchantment(Player player, ItemStack item, EnchantModel customEnchant, int level) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String enchantType = customEnchant.getId();

        switch (enchantType) {
            case "explosivo":
                if(container.has(explosivo)) {
                    int nivel = container.get(explosivo, PersistentDataType.INTEGER);
                    if(nivel > level) {
                        player.sendMessage("§cO item possui o mesmo encantamento de nivel maior.");
                        return false;
                    } else if(nivel == level) {
                        if(level + 1 > customEnchant.getMaxLevel()) {
                            player.sendMessage("§cO encantamento está no nível máximo");
                            return false;
                        }
                        container.set(explosivo, PersistentDataType.INTEGER, level + 1);
                        break;
                    } else {
                        container.set(explosivo, PersistentDataType.INTEGER, level);
                        break;
                    }
                } else {
                    container.set(explosivo, PersistentDataType.INTEGER, level);
                    break;
                }
            default:
                player.sendMessage("§cEncantamento não encontrado");
                return false;
        }
        updateLore(meta);
        item.setItemMeta(meta);
        return true;
    }

    public void updateLore(ItemMeta meta) {
        int explosive = meta.getPersistentDataContainer().getOrDefault(explosivo, PersistentDataType.INTEGER, 0);
        List<Component> lore = meta.lore() != null ? new ArrayList<>(meta.lore()) : new ArrayList<>();

        lore.removeIf(component ->
                PlainTextComponentSerializer.plainText().serialize(component).contains("Explosivo")
        );

        if (explosive > 0) {
            lore.add(0, Component.text("§7Explosivo " + roman.get(explosive)));
        }

        meta.lore(lore);
    }

    public NamespacedKey getExplosiveKey() {
        return explosivo;
    }







}
