package org.gustaav.zoneShop.gui;


import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.gustaav.zoneShop.Shop.ShopManager;
import org.gustaav.zoneShop.Shop.ShopProduct;
import org.gustaav.zoneShop.ZoneShop;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryInterface {

    private final Player player;
    private final String category;
    private final ShopInterface shopInterface;
    private List<ShopProduct> produtos = new ArrayList<>();
    private final ShopManager shopManager;
    private final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time

    public CategoryInterface(Player player, ZoneShop plugin, ShopInterface shopInterface, String categoryName) {
        this.player = player;
        shopManager = plugin.getShopManager();
        this.shopInterface = shopInterface;
        this.category = categoryName;
        switch (categoryName.toLowerCase()) {
            case "blocos":
                produtos = shopManager.getBlocos();
                break;
            case "ferramentas":
                produtos = shopManager.getFerramentas();
                break;
            case "utilitarios":
                produtos = shopManager.getUtilitarios();
                break;
            case "outros":
                produtos = shopManager.getOutros();
                break;
            case "plantacao":
                produtos = shopManager.getPlantacao();
                break;
        }
    }

    public void openBlocos() {
        ChestGui shop = new ChestGui(6, "Shop: " + category);
        PaginatedPane painel = new PaginatedPane(1,1,7,3);
        loadProduct(painel);
        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(voltarIcon(), event -> {
            if (painel.getPage() > 0) {
                painel.setPage(painel.getPage() - 1);
                shop.update();
            } else {
                shopInterface.openMenu();
            }
            event.setCancelled(true);
        }), 1, 0);
        navigation.addItem(new GuiItem(proximoIcon(), event -> {
            if (painel.getPage() < painel.getPages() - 1) {
                painel.setPage(painel.getPage() + 1);
                shop.update();
            }
            event.setCancelled(true);
        }), 7, 0);
        shop.addPane(painel);
        shop.addPane(navigation);
        shop.show(player);
    }
    public ItemStack voltarIcon() {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        ItemStack voltar = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta voltarMeta = (SkullMeta) voltar.getItemMeta();
        URL urlObject;
        try {
            urlObject = new URL("https://textures.minecraft.net/texture/be8f424e3697ba4aebfe67805719739565d5368665ab21af9ce2ebddd8945c8");
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);

        assert voltarMeta != null;
        voltarMeta.setOwnerProfile(profile);
        voltarMeta.setDisplayName("§cVoltar");
        voltar.setItemMeta(voltarMeta);

        return voltar;

    }
    public ItemStack proximoIcon() {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        ItemStack voltar = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta voltarMeta = (SkullMeta) voltar.getItemMeta();
        URL urlObject;
        try {
            urlObject = new URL("https://textures.minecraft.net/texture/4542d0c7429387948cf99e8cb8c5559e733fc7dbfb184c2c0b7d9efd829ff");
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);

        assert voltarMeta != null;
        voltarMeta.setOwnerProfile(profile);
        voltarMeta.setDisplayName("§aPróximo");
        voltar.setItemMeta(voltarMeta);

        return voltar;

    }
    public void loadProduct(PaginatedPane painel) {
        List<GuiItem> guiItemsList = new ArrayList<>();
        for(ShopProduct product : produtos) {
            ItemStack productItem = getItem(product);
            GuiItem productGui = new GuiItem(productItem, e -> {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                shopManager.buyProduct(player, product);
            });
            guiItemsList.add(productGui);
        }
        painel.populateWithGuiItems(guiItemsList);
    }
    private static ItemStack getItem(ShopProduct product) {
        ItemStack productItem = new ItemStack(product.getMaterial());
        ItemMeta productMeta = productItem.getItemMeta();
        assert productMeta != null;
        productMeta.setDisplayName("§e" + product.getName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("  §fPreço: §2$§f" + ZoneShop.format(product.getPrice()));
        lore.add("");
        lore.add("§aClique para adquirir o produto.");
        Multimap<Attribute, AttributeModifier> modifiers = productMeta.getAttributeModifiers();
        if(modifiers == null) {
            modifiers = HashMultimap.create();
            productMeta.setAttributeModifiers(modifiers);
        }
        productMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        productMeta.setLore(lore);
        productItem.setItemMeta(productMeta);
        return productItem;
    }

}

