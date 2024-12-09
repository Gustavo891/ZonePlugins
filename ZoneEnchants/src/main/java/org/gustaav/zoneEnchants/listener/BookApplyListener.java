package org.gustaav.zoneEnchants.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneEnchants.enchantment.CustomEnchants.CustomManager;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import org.gustaav.zoneEnchants.enchantment.models.EnchantModel;
import org.gustaav.zoneEnchants.enchantment.models.EnchantTypes;

public class BookApplyListener implements Listener {

    CustomManager customManager;

    private final NamespacedKey enchantKey = new NamespacedKey("enchantmentbook", "custom_enchant");
    private final NamespacedKey allowedItemsKey = new NamespacedKey("enchantmentbook", "allowed_items");

    EnchantConfig enchantConfig;

    public BookApplyListener(CustomManager customManager, EnchantConfig enchantConfig) {
        this.customManager = customManager;
        this.enchantConfig = enchantConfig;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        ItemStack cursor = event.getCursor();
        ItemStack target = event.getCurrentItem();

        if (target == null || cursor.getType() != Material.ENCHANTED_BOOK || !cursor.hasItemMeta()) {
            return;
        }

        ItemMeta bookMeta = cursor.getItemMeta();
        PersistentDataContainer container = bookMeta.getPersistentDataContainer();

        if (container.has(enchantKey, PersistentDataType.STRING)) {
            String enchantData = container.get(enchantKey, PersistentDataType.STRING);

            String[] data = enchantData.split(":");
            String enchantment = data[0];
            int level = Integer.parseInt(data[1]);

            EnchantModel model = null;
            for(EnchantModel enchant : enchantConfig.getEnchantments()) {
                if(enchant.getId().equalsIgnoreCase(enchantment)) {
                    model = enchant;
                }
            }
            if(model == null) {
                player.sendMessage("§cEsse livro foi corrompido, contate um staff para ajuda-lo.");
                return;
            }

            // Parte de juntar livros
            if(target.getType() == Material.ENCHANTED_BOOK) {
                if(target.getItemMeta().getPersistentDataContainer().has(enchantKey, PersistentDataType.STRING)) {
                    String stringTarget = target.getItemMeta().getPersistentDataContainer().get(enchantKey, PersistentDataType.STRING);
                    assert stringTarget != null;
                    String[] dataTarget = stringTarget.split(":");
                    String enchantmentTarget = dataTarget[0];
                    int levelTarget = Integer.parseInt(dataTarget[1]);
                    if(enchantmentTarget.equalsIgnoreCase(enchantment) && levelTarget == level) {
                        if(level + 1 > model.getMaxLevel()) {
                            player.sendMessage("§cLivro já está no nível máximo.");
                            return;
                        }
                        ItemStack newBook = enchantConfig.giveBook(enchantment, level+1);
                        if(newBook != null) {
                            event.getWhoClicked().setItemOnCursor(null);
                            target.setAmount(target.getAmount() - 1);
                            player.getInventory().addItem(newBook);
                            return;
                        } else {
                            player.sendMessage("§cFalha ao juntar os livros. Contate um staff.");
                            return;
                        }
                    } else {
                        player.sendMessage("§cEsses livros não são compatíveis.");
                        return;
                    }
                } else {
                    player.sendMessage("§cVocê não pode juntar esses livros.");
                    return;
                }
            }

            boolean canApply = false;
            for(EnchantTypes type : model.getTypes()) {
                if(target.getType().name().toLowerCase().contains(type.name().toLowerCase())) {
                    canApply = true;
                }
            }
            if(!canApply) {
                return;
            }

            ItemMeta targetMeta = target.getItemMeta();
            if (targetMeta != null) {

                if(model.getEnchantment() == null) {
                    if(!(customManager.setCustomEnchantment(player, target, model, level))) {
                        return;
                    }
                } else {
                    if(targetMeta.getEnchantLevel(model.getEnchantment()) < level) {
                        targetMeta.addEnchant(model.getEnchantment(), level, true);
                    } else if(targetMeta.getEnchantLevel(model.getEnchantment()) > level) {
                        player.sendMessage("§cO item possui o mesmo encantamento de nivel maior.");
                        return;
                    } else {
                        if(level + 1 > model.getMaxLevel()) {
                            player.sendMessage("§cO encantamento está no nível máximo");
                            return;
                        }
                        targetMeta.addEnchant(model.getEnchantment(), level+1, true);
                    }
                    target.setItemMeta(targetMeta);
                }
            }

            event.getWhoClicked().setItemOnCursor(null);
            event.setCancelled(true);

            event.getWhoClicked().sendMessage(ChatColor.GREEN + "Encantamento aplicado com sucesso!");
        }
    }
}
