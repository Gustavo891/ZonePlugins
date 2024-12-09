package org.gustaav.zoneEnchants.enchantment;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneEnchants.enchantment.models.EnchantModel;
import org.gustaav.zoneEnchants.enchantment.models.EnchantTypes;
import org.gustaav.zoneEnchants.enchantment.models.RarityType;

import java.util.ArrayList;
import java.util.List;

public class EnchantConfig {

    private final NamespacedKey enchantKey = new NamespacedKey("enchantmentbook", "custom_enchant");
    private final NamespacedKey allowedItemsKey = new NamespacedKey("enchantmentbook", "allowed_items");

    public EnchantModel eficiencia = new EnchantModel(
            "Eficiência",
            "eficiencia",
            Enchantment.EFFICIENCY,
            "Aumenta a velocidade de\nmineração de seu item.",
            List.of("Pacote Comum" ,"Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.AXE, EnchantTypes.SHOVEL, EnchantTypes.PICKAXE),
            6
    );

    public EnchantModel afiada = new EnchantModel(
            "Afiada",
            "afiada",
            Enchantment.SHARPNESS,
            "Aumenta o dano contra\ntodos tipos de entidades.",
            List.of("Pacote Comum" ,"Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.AXE, EnchantTypes.SWORD),
            6
    );
    public EnchantModel durabilidade = new EnchantModel(
            "Durabilidade",
            "durabilidade",
            Enchantment.UNBREAKING,
            "Aumenta a durabilidade\nde seu item.",
            List.of("Pacote Comum" ,"Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.HELMET, EnchantTypes.CHESTPLATE, EnchantTypes.LEGGINGS, EnchantTypes.BOOTS, EnchantTypes.PICKAXE, EnchantTypes.SWORD, EnchantTypes.AXE, EnchantTypes.SHOVEL),
            4
    );
    public EnchantModel fortuna = new EnchantModel(
            "Fortuna",
            "fortuna",
            Enchantment.FORTUNE,
            "Aumenta as chances de conseguir\nitens extras ao minerar.",
            List.of("Pacote Comum" ,"Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.PICKAXE, EnchantTypes.AXE),
            4
    );

    public EnchantModel toqueSuave = new EnchantModel(
            "Toque-suave",
            "toque_suave",
            Enchantment.SILK_TOUCH,
            "Permite que blocos minerados\n sejam coletados em sua forma natural.",
            List.of("Pacote Místico"),
            List.of(EnchantTypes.PICKAXE, EnchantTypes.AXE, EnchantTypes.SHOVEL),
            1
    );
    public EnchantModel protecao = new EnchantModel(
            "Proteção",
            "protecao",
            Enchantment.PROTECTION,
            "Diminui a quantidade de\ndano que você sofre",
            List.of("Pacote Comum" ,"Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.HELMET, EnchantTypes.CHESTPLATE, EnchantTypes.LEGGINGS, EnchantTypes.BOOTS),
            5
    );
    public EnchantModel aspecto_flamejante = new EnchantModel(
            "Aspecto-Flamejante",
            "aspecto_flamejante",
            Enchantment.FIRE_ASPECT,
            "As entidades que você atinge\nirão entrar em combustão.",
            List.of("Pacote Raro", "Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.SWORD, EnchantTypes.AXE),
            2
    );
    public EnchantModel pilhagem = new EnchantModel(
            "Pilhagem",
            "pilhagem",
            Enchantment.LOOTING,
            "Aumente a quantidade de loot\nrecebidos ao matar monstros.",
            List.of("Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.SWORD),
            3
    );
    /*
    Encantamentos Customizados
     */
    public EnchantModel explosivo = new EnchantModel(
            "Explosivo",
            "explosivo",
            null,
            "Cause uma explosão em área.",
            List.of("Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.PICKAXE),
            3
    );
    public EnchantModel forcabrutal = new EnchantModel(
            "Força Brutal",
            "forcabrutal",
            null,
            "Causa mais danos em monstros (bosses).",
            List.of("Pacote Místico", "Caixas Misteriosas"),
            List.of(EnchantTypes.SWORD),
            10

    );

    @Getter
    public final List<EnchantModel> enchantments = List.of(forcabrutal, explosivo, pilhagem, eficiencia, protecao, durabilidade, toqueSuave, fortuna, afiada, aspecto_flamejante);

    public ItemStack giveBook(String tipo, int nivel) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        EnchantModel enchantment = null;

        for(EnchantModel enchant : getEnchantments()) {
            if(enchant.getId().equalsIgnoreCase(tipo)) {
                enchantment = enchant;
            }
        }

        if(enchantment == null) {
            return null;
        }

        if (meta != null) {
            Component component = MiniMessage.miniMessage().deserialize(String.format("<#50FF63>%s %s", enchantment.getName(), nivel))
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            String desc = enchantment.getDesc();  // Exemplo: "Aumenta a velocidade de \nmineração de seu item."
            String[] lines = desc.split("\n");
            for (String line : lines) {
                lore.add(Component.text("§7" + line));  // Adiciona cada linha separada com §7
            }
            lore.add(Component.text("§r"));
            lore.add(MiniMessage.miniMessage().deserialize("  <#50FF63>Aplicável em:").decoration(TextDecoration.ITALIC, false));
            if(enchantment.getName().equalsIgnoreCase("Durabilidade")) {
                lore.add(Component.text("  §8◆ §fTodos itens."));
            } else {
                for(EnchantTypes enchant : enchantment.getTypes()) {
                    lore.add(Component.text("  §8◆ §f" + enchant.getValue()));
                }
            }
            lore.add(Component.text("§r"));
            lore.add(Component.text("§7Clique em um item para aplicar."));
            meta.lore(lore);

            meta.getPersistentDataContainer().set(enchantKey, PersistentDataType.STRING, tipo + ":" + nivel);
            book.setItemMeta(meta);

        }
        return book;
    }


}
