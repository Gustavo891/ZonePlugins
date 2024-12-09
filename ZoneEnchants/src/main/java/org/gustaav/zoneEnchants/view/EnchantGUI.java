package org.gustaav.zoneEnchants.view;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.gustaav.zoneEnchants.commands.EnchantCommand;
import org.gustaav.zoneEnchants.commands.GiveBookCommand;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import org.gustaav.zoneEnchants.enchantment.Experience;
import org.gustaav.zoneEnchants.utils.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EnchantGUI {

    int custoComum = 300;
    int custoRaro = 800;
    int custoMistico = 2000;

    EnchantConfig enchantConfig;
    ChestGui enchantGui;
    Player player;
    String categoria = null;

    public EnchantGUI(Player player, EnchantConfig enchantConfig) {
        this.player = player;
        this.enchantConfig = enchantConfig;

    }

    public void loadGui() {
        enchantGui = new ChestGui(5, "Mesa de encantamento:");

        enchantGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });
        StaticPane pane = new StaticPane(Slot.fromIndex(11), 1, 3);
        StaticPane encantar = new StaticPane(Slot.fromIndex(13), 3, 3);
        loadFerramentas(pane);
        loadArmaduras(pane);
        loadArmamentos(pane);

        loadHead(encantar);
        pacoteComum(encantar);
        pacoteRaro(encantar);
        pacoteMistico(encantar);

        enchantGui.addPane(pane);
        enchantGui.addPane(encantar);
        enchantGui.show(player);
    }

    public void loadFerramentas(StaticPane pane) {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        Component component = MiniMessage.miniMessage().deserialize("<#52FF0F>Ferramentas")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Consiga livros para deixar suas"));
        lore.add(Component.text("§7ferramentas mais rápidas e lucrativas."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("  §fAplicável em:"));
        lore.add(Component.text("  §8◆ §7Picaretas"));
        lore.add(Component.text("  §8◆ §7Machados"));
        lore.add(Component.text("  §8◆ §7Pás..."));
        lore.add(Component.text("§r"));
        if(categoria != null && categoria.equalsIgnoreCase("ferramentas")) {
            meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
            lore.add(Component.text("§8Categoria já selecionada."));
        } else {
            lore.add(Component.text("§aClique para seleciona-la."));
        }
        meta.lore(lore);

        Multimap<Attribute, AttributeModifier> modifierMultimap = meta.getAttributeModifiers();
        if(modifierMultimap == null) {
            modifierMultimap = HashMultimap.create();
            meta.setAttributeModifiers(modifierMultimap);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null && categoria.equalsIgnoreCase("ferramentas")) {
                e.setCancelled(true);
            } else {
                categoria = "ferramentas";
                updateMenu();
            }
        }), Slot.fromIndex(0));

    }
    public void loadArmaduras(StaticPane pane) {
        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        Component component = MiniMessage.miniMessage().deserialize("<#52FF0F>Armaduras")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Torne sua armadura extremamente"));
        lore.add(Component.text("§7poderosa contra seus rivais."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("  §fAplicável em:"));
        lore.add(Component.text("  §8◆ §7Capacetes"));
        lore.add(Component.text("  §8◆ §7Peitorais"));
        lore.add(Component.text("  §8◆ §7Calças"));
        lore.add(Component.text("  §8◆ §7Botas"));
        lore.add(Component.text("§r"));
        if(categoria != null && categoria.equalsIgnoreCase("armaduras")) {
            meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
            lore.add(Component.text("§8Categoria já selecionada."));
        } else {
            lore.add(Component.text("§aClique para seleciona-la."));
        }
        meta.lore(lore);



        Multimap<Attribute, AttributeModifier> modifierMultimap = meta.getAttributeModifiers();
        if(modifierMultimap == null) {
            modifierMultimap = HashMultimap.create();
            meta.setAttributeModifiers(modifierMultimap);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null && categoria.equalsIgnoreCase("armaduras")) {
                e.setCancelled(true);
            } else {
                categoria = "armaduras";
                updateMenu();
            }
        }), Slot.fromIndex(1));

    }
    public void loadArmamentos(StaticPane pane) {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        Component component = MiniMessage.miniMessage().deserialize("<#52FF0F>Armamentos")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Forje a melhor arma do servidor"));
        lore.add(Component.text("§7para combater todos inimigos."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("  §fAplicável em:"));
        lore.add(Component.text("  §8◆ §7Espadas"));
        lore.add(Component.text("  §8◆ §7Machados"));
        lore.add(Component.text("§r"));        if(categoria != null && categoria.equalsIgnoreCase("armamentos")) {
            lore.add(Component.text("§8Categoria já selecionada."));
            meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
        } else {
            lore.add(Component.text("§aClique para seleciona-la."));
        }
        meta.lore(lore);

        Multimap<Attribute, AttributeModifier> modifierMultimap = meta.getAttributeModifiers();
        if(modifierMultimap == null) {
            modifierMultimap = HashMultimap.create();
            meta.setAttributeModifiers(modifierMultimap);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null && categoria.equalsIgnoreCase("armamentos")) {
                e.setCancelled(true);
            } else {
                categoria = "armamentos";
                updateMenu();
            }
        }), Slot.fromIndex(2));

    }
    public void loadHead(StaticPane pane) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg5NTEzOTlkMGViZDBkZmU4N2U1MGYwZDZkZWUyNTI3NGQ5M2YxZmJiMzg1MDVlYzk3MWI2MDFkMWMyY2I5In19fQ==";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);

        Component component = MiniMessage.miniMessage().deserialize("<#4EFF03>Encantamentos")
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Veja todos encantamentos"));
        lore.add(Component.text("§7disponiveis no rankup."));
        meta.lore(lore);
        item.setItemMeta(meta);

        pane.addItem(new GuiItem(item, e -> {
            EnchantmentList enchantmentList = new EnchantmentList(enchantConfig, player, this);
            enchantmentList.loadGui();
        }), Slot.fromIndex(1));
    }

    public void pacoteComum(StaticPane pane) {
        ItemStack item;
        if(categoria == null) {
            item = new ItemStack(Material.RED_DYE);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#AA354A>Pacote não encontrado!</color>")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Selecione uma categoria para"));
            lore.add(Component.text("§7adquirir os encantamentos."));
            meta.lore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#9CFF90>Pacote Comum</color>    ")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Encantamentos de baixo nível."));
            lore.add(Component.text("§r"));
            lore.add(Component.text(String.format("§f  Custo: §7%s XP", custoComum)));
            lore.add(Component.text("§r"));
            if(Experience.getExp(player) >= custoComum) {
                lore.add(Component.text("§aClique para adquirir esse pacote."));
            } else {
                lore.add(Component.text("§cVocê não tem experiência suficiente"));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
        }

        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null) {
                if(Experience.getExp(player) >= custoComum) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    Experience.changeExp(player, -custoComum);
                    buyPacote("comum");
                    e.setCancelled(true);
                    updateMenu();
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }), Slot.fromIndex(6));

    }
    public void pacoteRaro(StaticPane pane) {
        ItemStack item;
        if(categoria == null) {
            item = new ItemStack(Material.RED_DYE);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#AA354A>Pacote não encontrado!</color>")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Selecione uma categoria para"));
            lore.add(Component.text("§7adquirir os encantamentos."));
            meta.lore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#9CFF90>Pacote Raro</color>    ")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Encantamentos que deixam seu item forte."));
            lore.add(Component.text("§r"));
            lore.add(Component.text(String.format("§f  Custo: §7%s XP", custoRaro)));
            lore.add(Component.text("§r"));
            if(Experience.getExp(player) >= custoRaro) {
                lore.add(Component.text("§aClique para adquirir esse pacote."));
            } else {
                lore.add(Component.text("§cVocê não tem experiência suficiente"));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
        }

        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null) {
                if(Experience.getExp(player) >= custoRaro) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    Experience.changeExp(player, -custoRaro);
                    buyPacote("raro");
                    e.setCancelled(true);
                    updateMenu();
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }), Slot.fromIndex(7));

    }
    public void pacoteMistico(StaticPane pane) {
        ItemStack item;
        if(categoria == null) {
            item = new ItemStack(Material.RED_DYE);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#AA354A>Pacote não encontrado!</color>")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Selecione uma categoria para"));
            lore.add(Component.text("§7adquirir os encantamentos."));
            meta.lore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.BOOKSHELF);
            ItemMeta meta = item.getItemMeta();
            Component component = MiniMessage.miniMessage().deserialize("<color:#9CFF90>Pacote Místico</color>    ")
                    .decoration(TextDecoration.ITALIC, false);
            meta.displayName(component);
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Encantamentos mais fortes do servidor."));
            lore.add(Component.text("§r"));
            lore.add(Component.text(String.format("§f  Custo: §7%s XP", custoMistico)));
            lore.add(Component.text("§r"));
            if(Experience.getExp(player) >= custoMistico) {
                lore.add(Component.text("§aClique para adquirir esse pacote."));
            } else {
                lore.add(Component.text("§cVocê não tem experiência suficiente"));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
        }

        pane.addItem(new GuiItem(item, e -> {
            if(categoria != null) {
                if(Experience.getExp(player) >= custoMistico) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    Experience.changeExp(player, -custoMistico);
                    buyPacote("mistico");
                    e.setCancelled(true);
                    updateMenu();
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
        }), Slot.fromIndex(8));

    }

    public void updateMenu() {
        StaticPane pane = new StaticPane(Slot.fromIndex(11), 1, 3);
        StaticPane encantar = new StaticPane(Slot.fromIndex(13), 3, 3);

        loadFerramentas(pane);
        loadArmaduras(pane);
        loadArmamentos(pane);

        loadHead(encantar);
        pacoteComum(encantar);
        pacoteRaro(encantar);
        pacoteMistico(encantar);

        enchantGui.getPanes().clear();
        enchantGui.addPane(pane);
        enchantGui.addPane(encantar);
        enchantGui.update();
    }
    public void buyPacote(String pacote) {
        if (categoria == null) {
            return;
        }

        String[][] encantamentos = getEnchant(categoria, pacote);
        String[] escolhido = encantamentos[new Random().nextInt(encantamentos.length)];
        String tipo = escolhido[0];
        String nivel = escolhido[1];

        ItemStack livro = enchantConfig.giveBook(tipo, Integer.parseInt(nivel));
        if (livro != null) {
            player.getInventory().addItem(livro);
            MessageUtil.sendFormattedMessage(player, String.format(
                    "${Colors.PURPLE}<b>ENCANTAR!</b> ${Colors.PURPLE_LIGHT}Você adquiriu um livro do tipo '<white>%s${Colors.PURPLE_LIGHT}'.", PlainTextComponentSerializer.plainText().serialize(livro.getItemMeta().displayName())));
        }
    }
    private String[][] getEnchant(String categoria, String pacote) {
        return switch (categoria) {
            case "ferramentas" -> switch (pacote) {
                case "comum" -> new String[][]{
                        {"eficiencia", "1"}, {"eficiencia", "2"}, {"Durabilidade", "1"}, {"Fortuna", "1"}
                };
                case "raro" -> new String[][]{
                        {"eficiencia", "2"}, {"eficiencia", "3"}, {"Durabilidade", "2"}, {"Fortuna", "1"}, {"Fortuna", "2"}
                };
                case "mistico" -> new String[][]{
                        {"eficiencia", "4"}, {"eficiencia", "5"}, {"Durabilidade", "3"}, {"Fortuna", "2"}, {"Fortuna", "3"}, {"Durabilidade", "2"}, {"toque_suave", "1"}
                };
                default -> new String[][]{{"Durabilidade", "1"}};
            };
            case "armaduras" -> switch (pacote) {
                case "comum" -> new String[][]{
                        {"protecao", "1"}, {"protecao", "2"}, {"Durabilidade", "1"}
                };
                case "raro" -> new String[][]{
                        {"protecao", "2"}, {"protecao", "3"}, {"Durabilidade", "2"}
                };
                case "mistico" -> new String[][]{
                        {"protecao", "3"}, {"protecao", "4"}, {"Durabilidade", "3"}, {"Durabilidade", "2"}
                };
                default -> new String[][]{{"Durabilidade", "2"}};
            };
            case "armamentos" -> switch (pacote) {
                case "comum" -> new String[][]{
                        {"Afiada", "1"}, {"Afiada", "2"}, {"Durabilidade", "1"}
                };
                case "raro" -> new String[][]{
                        {"Afiada", "2"}, {"Afiada", "3"}, {"Durabilidade", "2"}, {"aspecto_flamejante", "1"}
                };
                case "mistico" -> new String[][]{
                        {"Afiada", "4"}, {"Afiada", "5"}, {"Durabilidade", "3"}, {"Durabilidade", "2"},
                        {"aspecto_flamejante", "2"}, {"Pilhagem", "1"}, {"Pilhagem", "2"}
                };
                default -> new String[][]{{"Durabilidade", "3"}};
            };
            default -> new String[][]{{"Durabilidade", "3"}};
        };
    }


}
