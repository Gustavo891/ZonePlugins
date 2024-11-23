package org.gustaav.zoneEnchants.enchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantConfig {

    private static final NamespacedKey enchantKey = new NamespacedKey("enchantmentbook", "custom_enchant");
    private final NamespacedKey allowedItemsKey = new NamespacedKey("enchantmentbook", "allowed_items");

    public static EnchantModel eficiencia = new EnchantModel(
            "Eficiência",
            "eficiencia",
            Enchantment.EFFICIENCY,
            "Aumenta a velocidade de\nmineração de seu item.",
            List.of(EnchantTypes.AXE, EnchantTypes.SHOVEL, EnchantTypes.PICKAXE),
            6
    );

    public static EnchantModel afiada = new EnchantModel(
            "Afiada",
            "afiada",
            Enchantment.SHARPNESS,
            "Aumenta o dano contra\ntodos tipos de entidades.",
            List.of(EnchantTypes.AXE, EnchantTypes.SWORD),
            6
    );
    public static EnchantModel durabilidade = new EnchantModel(
            "Durabilidade",
            "durabilidade",
            Enchantment.UNBREAKING,
            "Aumenta a durabilidade\nde seu item.",
            List.of(EnchantTypes.HELMET, EnchantTypes.CHESTPLATE, EnchantTypes.LEGGINGS, EnchantTypes.BOOTS, EnchantTypes.PICKAXE, EnchantTypes.SWORD, EnchantTypes.AXE, EnchantTypes.SHOVEL),
            4
    );
    public static EnchantModel fortuna = new EnchantModel(
            "Fortuna",
            "fortuna",
            Enchantment.FORTUNE,
            "Aumenta as chances de conseguir\nitens extras ao minerar.",
            List.of(EnchantTypes.PICKAXE, EnchantTypes.AXE),
            4
    );

    public static EnchantModel toqueSuave = new EnchantModel(
            "Toque-suave",
            "toque_suave",
            Enchantment.SILK_TOUCH,
            "Permite que blocos minerados\n sejam coletados em sua forma natural.",
            List.of(EnchantTypes.PICKAXE, EnchantTypes.AXE, EnchantTypes.SHOVEL),
            1
    );
    public static EnchantModel protecao = new EnchantModel(
            "Proteção",
            "proteção",
            Enchantment.PROTECTION,
            "Diminui a quantidade de\ndano que você sofre",
            List.of(EnchantTypes.HELMET, EnchantTypes.CHESTPLATE, EnchantTypes.LEGGINGS, EnchantTypes.BOOTS),
            5
    );
    public static EnchantModel aspecto_flamejante = new EnchantModel(
            "Aspecto-Flamejante",
            "aspecto_flamejante",
            Enchantment.FIRE_ASPECT,
            "As entidades que você atinge\nirão entrar em combustão.",
            List.of(EnchantTypes.SWORD, EnchantTypes.AXE),
            2
    );
    public static EnchantModel pilhagem = new EnchantModel(
            "Pilhagem",
            "pilhagem",
            Enchantment.LOOTING,
            "Aumente a quantidade de loot\nrecebidos ao matar monstros.",
            List.of(EnchantTypes.SWORD),
            3
    );
    public static EnchantModel explosivo = new EnchantModel(
            "Explosivo",
            "explosivo",
            null,
            "Cause uma explosão em área.",
            List.of(EnchantTypes.PICKAXE),
            3
    );


    public static final List<EnchantModel> enchantments = List.of(explosivo, pilhagem, eficiencia, protecao, durabilidade, toqueSuave, fortuna, afiada, aspecto_flamejante);

    public static List<EnchantModel> getEnchantments() {
        return enchantments;
    }

    public static ItemStack giveBook(String tipo, int nivel) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        EnchantModel enchantment = null;

        for(EnchantModel enchant : EnchantConfig.getEnchantments()) {
            if(enchant.getId().equalsIgnoreCase(tipo)) {
                enchantment = enchant;
            }
        }

        if(enchantment == null) {
            return null;
        }

        if (meta != null) {
            Component component = MiniMessage.miniMessage().deserialize(String.format("<#FF35F4>%s %s", enchantment.getName(), nivel))
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            String desc = enchantment.getDesc();  // Exemplo: "Aumenta a velocidade de \nmineração de seu item."
            String[] lines = desc.split("\n");
            for (String line : lines) {
                lore.add(Component.text("§7" + line));  // Adiciona cada linha separada com §7
            }
            lore.add(Component.text("§r"));
            lore.add(Component.text("§f  Aplicável em:"));
            if(enchantment.getName().equalsIgnoreCase("Durabilidade")) {
                lore.add(Component.text("  §8◆ §7Todos itens."));
            } else {
                for(EnchantTypes enchant : enchantment.getTypes()) {
                    lore.add(Component.text("  §8◆ §7" + enchant.getValue()));
                }
            }
            lore.add(Component.text("§r"));
            lore.add(Component.text("§dClique em um item para aplicar."));
            meta.lore(lore);

            meta.getPersistentDataContainer().set(enchantKey, PersistentDataType.STRING, tipo + ":" + nivel);
            book.setItemMeta(meta);

        }
        return book;
    }


}
