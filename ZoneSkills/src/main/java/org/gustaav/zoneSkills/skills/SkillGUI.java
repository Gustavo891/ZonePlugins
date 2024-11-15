package org.gustaav.zoneSkills.skills;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.units.qual.C;
import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.utils.RandomUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkillGUI {

    ChestGui gui;
    Player player;
    PlayerDataManager playerDataManager;

    public SkillGUI(ZoneSkills plugin, Player player) {
        this.gui = new ChestGui(4, "Menu de habilidades:");
        this.playerDataManager = plugin.getPlayerDataManager();
        this.player = player;

        gui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        StaticPane pane = new StaticPane(0, 0, 9, 4);

        loadSkills(pane);
        gui.addPane(pane);

        gui.show(player);
    }

    private void loadSkills(StaticPane skills) {
        loadMining(skills);
        loadCombat(skills);
        loadFarming(skills);
        loadAcrobatics(skills);
        loadPlayerInfo(skills);
        loadRanking(skills);
    }

    private void loadMining(StaticPane skills) {

        ItemStack mining = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = mining.getItemMeta();
        assert meta != null;
        int level = playerDataManager.getLevel(player.getUniqueId(), SkillType.mining);
        double experiencia = playerDataManager.getXp(player.getUniqueId(), SkillType.mining);
        double experiencia2 = playerDataManager.getXpRequiredForLevel(level + 1);
        meta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + "Mineração");
        List<String> lore = new ArrayList<>();
        lore.add("§7Ganhe experiência e melhore sua");
        lore.add("§7habilidade quebrando minérios.");
        getDesc(mining, meta, level, experiencia, experiencia2, lore);
        skills.addItem(new GuiItem(mining), Slot.fromIndex(11));

    }
    private void loadFarming(StaticPane skills) {

        ItemStack mining = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = mining.getItemMeta();
        assert meta != null;
        int level = playerDataManager.getLevel(player.getUniqueId(), SkillType.farming);
        double experiencia = playerDataManager.getXp(player.getUniqueId(), SkillType.farming);
        double experiencia2 = playerDataManager.getXpRequiredForLevel(level + 1);
        meta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + "Fazendeiro");
        List<String> lore = new ArrayList<>();
        lore.add("§7Ganhe experiência e melhore sua");
        lore.add("§7habilidade farmando plantações.");
        getDesc(mining, meta, level, experiencia, experiencia2, lore);
        skills.addItem(new GuiItem(mining), Slot.fromIndex(12));

    }
    private void loadCombat(StaticPane skills) {

        ItemStack mining = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = mining.getItemMeta();
        assert meta != null;
        int level = playerDataManager.getLevel(player.getUniqueId(), SkillType.combat);
        double experiencia = playerDataManager.getXp(player.getUniqueId(), SkillType.combat);
        double experiencia2 = playerDataManager.getXpRequiredForLevel(level + 1);
        meta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + "Combate");
        List<String> lore = new ArrayList<>();
        lore.add("§7Ganhe experiência e melhore sua");
        lore.add("§7habilidade matando monstros/jogadores.");
        getDesc(mining, meta, level, experiencia, experiencia2, lore);
        skills.addItem(new GuiItem(mining), Slot.fromIndex(20));

    }
    private void loadAcrobatics(StaticPane skills) {

        ItemStack mining = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta meta = mining.getItemMeta();
        assert meta != null;
        int level = playerDataManager.getLevel(player.getUniqueId(), SkillType.acrobatics);
        double experiencia = playerDataManager.getXp(player.getUniqueId(), SkillType.acrobatics);
        double experiencia2 = playerDataManager.getXpRequiredForLevel(level + 1);
        meta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + "Acrobacia");
        List<String> lore = new ArrayList<>();
        lore.add("§7Ganhe experiência e melhore sua");
        lore.add("§7habilidade tomando dano de queda.");
        getDesc(mining, meta, level, experiencia, experiencia2, lore);
        skills.addItem(new GuiItem(mining), Slot.fromIndex(21));

    }

    private void loadPlayerInfo(StaticPane skills) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        assert skullMeta != null;
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + player.getName());
        List<String> lore = new ArrayList<>();
        lore.add("§7Veja as informações de sua conta.");
        lore.add("");
        lore.add(ChatColor.of(new Color(0x4eff03)) + "  Informações:");
        lore.add("  §fNível total: " + ChatColor.of(new Color(0x96FF92)) + playerDataManager.getTotalLevel(player.getUniqueId()));
        lore.add("  §fPos. Ranking: §7#1");
        lore.add("");
        skullMeta.setLore(lore);
        playerHead.setItemMeta(skullMeta);

        skills.addItem(new GuiItem(playerHead), Slot.fromIndex(15));
    }

    private void loadRanking(StaticPane skills) {
        ItemStack mining = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = mining.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.of(new Color(0x4eff03)) + "Ranking");
        List<String> lore = new ArrayList<>();
        lore.add("§7Veja os jogadores mais habilidades do servidor.");
        meta.setLore(lore);
        mining.setItemMeta(meta);

        skills.addItem(new GuiItem(mining), Slot.fromIndex(24));

    }

    private void getDesc(ItemStack mining, ItemMeta meta, int level, double experiencia, double experiencia2, List<String> lore) {
        lore.add("");
        lore.add(ChatColor.of(new Color(0x4eff03)) + "  Informações:");
        lore.add("  §fNivel: " + ChatColor.of(new Color(0xD7FFC7)) + level);
        lore.add("  §fExperiência: " + ChatColor.of(new Color(0xD7FFC7)) + RandomUtil.format(experiencia) + "/" + RandomUtil.format(experiencia2));
        lore.add("");
        lore.add("§fProgresso: " + barraProgresso(experiencia, experiencia2));
        Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
        if(modifiers == null) {
            modifiers = HashMultimap.create();
            meta.setAttributeModifiers(modifiers);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(lore);
        mining.setItemMeta(meta);
    }
    public String barraProgresso(double experiencia, double necessario) {
        int barrasTotais = 10;

        double percentual = (experiencia / necessario)*100;
        percentual = Math.min(percentual, 100);

        int barrasPreenchidas = (int) (percentual / 100 * barrasTotais);
        int barrasVazias = barrasTotais - barrasPreenchidas;

        return "§2" + "⬛".repeat(Math.max(0, barrasPreenchidas)) +
                "§8" +
                "⬛".repeat(Math.max(0, barrasVazias)) +
                " §7" + (int) percentual + "%";
    }

}
