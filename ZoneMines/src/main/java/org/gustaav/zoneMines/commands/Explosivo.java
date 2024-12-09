package org.gustaav.zoneMines.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.persistence.PersistentDataContainer;
import org.gustaav.zoneMines.utils.MessageUtil;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class Explosivo {


    // Bomba Tier I [3x3]
    // Bomba Tier II [5x5]
    // Bomba Tier III [7x7]

    /*
     Tipos: Normal, Fortuna, Toque-Suave e Duplicadora
        - Normal: Apenas dropa os blocos da explosão
        - Fortuna: Aumenta a quantidade de drops causados na explosão
        - Toque-suave: Pega os drops com o encantamento de toque-suave.
        - Duplicadora: Chance de duplicar a bomba ao explodir.
     */

    private static final Integer[] VALID_TIERS = {1, 2, 3};
    private static final String[] VALID_TIPOS = {"Normal", "Fortuna", "Toque-suave", "Duplicadora"};

    @CommandPermission("zonemines.giveexplosive")
    @Command("giveexplosivo <player> <tier> <tipo> <flag>")
    public void giveExplosive(
            CommandSender sender,
            @Named("player") Player target,
            @Suggest({"1", "2", "3"}) @Named("tier") Integer tier,
            @Suggest({"Normal", "Fortuna", "Toque-suave", "Duplicadora"}) @Named("tipo") String especialidade,
            @Optional @Named("flag") String flag) {

        boolean console = false;
        if(flag != null) {
            console = flag.equals("-s");
        }

        if(target == null || !target.isOnline()) {
            if(!console) {
                sender.sendMessage("§cJogador não encontrado.");
            }
            return;
        }

        if(Arrays.stream(VALID_TIERS).noneMatch(Predicate.isEqual(tier)) || Arrays.stream(VALID_TIPOS).noneMatch(Predicate.isEqual(especialidade))) {
            if(!console) {
                sender.sendMessage("§cTier ou especialidade inválido.");
            }
            return;
        }

        ItemStack item = createExplosivo(tier, especialidade);
        if(!console) {
            MessageUtil.sendFormattedMessage((Player) sender, String.format("${Colors.PURPLE_LIGHT}Você deu ${Colors.WHITE}Bomba Tier %s ✦ %s ${Colors.PURPLE_LIGHT}para o jogador ${Colors.WHITE}%s${Colors.PURPLE_LIGHT}.", tier, especialidade, target.getName()));
        }
        target.getInventory().addItem(item);
        return;

    }

    public static ItemStack createExplosivo(int tier, String especialidade) {
        ItemStack explosivo = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) explosivo.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzFkZjcwZGM5ODg1ZTQxOGI1OGQ4ZTc0ODZhODFlZDRlYzJlNzk2M2M0MjU2OWM0NjgwODRkNGY1NjdjNiJ9fX0=";
        if (meta == null) return explosivo;

        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");

        especialidade = especialidade.substring(0, 1).toUpperCase() + especialidade.substring(1).toLowerCase();
        List<Component> lore = new ArrayList<>();
        Component component;
        switch (tier) {
            case 1:
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzFkZjcwZGM5ODg1ZTQxOGI1OGQ4ZTc0ODZhODFlZDRlYzJlNzk2M2M0MjU2OWM0NjgwODRkNGY1NjdjNiJ9fX0=";
                component = MiniMessage.miniMessage().deserialize(
                                String.format("<#FF35F4>Bomba <b>Tier I</b> <dark_gray>✦ <white>%s", especialidade))
                                .decoration(TextDecoration.ITALIC, false);
                meta.displayName(component);
                lore.add(Component.text("§7Utilize essa bomba para"));
                lore.add(Component.text("§7minerar em grande área."));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§f  Área: §73x3"));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§8Aremesse com o botão-direito."));
                meta.lore(lore);
                break;
            case 2:
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGJkZmZmZGNhMDdkNTYyYmY5OTA1MGU5MGM0ZGI1ZmE1OGM1MDk4MmU1NWNmOTZhOGQxMDIyNTZhZmZhZjUifX19";
                component = MiniMessage.miniMessage().deserialize(
                                String.format("<#C800FF>Bomba <b>Tier II</b> <dark_gray>✦ <white>%s", especialidade))
                        .decoration(TextDecoration.ITALIC, false);
                meta.displayName(component);
                lore.add(Component.text("§7Utilize essa bomba para"));
                lore.add(Component.text("§7minerar em grande área."));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§f  Área: §75x5"));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§8Aremesse com o botão-direito."));
                meta.lore(lore);
                break;
            case 3:
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc5YTIzNzUzNjNiZTg5MTM4MWQ0MmM0NWIzNzc1NTNjMmI2ZTBmYmRjMTZkZWY1ZmJjNmIzMmVkNWNhZDdhNyJ9fX0=";
                component = MiniMessage.miniMessage().deserialize(
                                String.format("<#8300AA>Bomba <b>Tier III</b> <dark_gray>✦ <white>%s", especialidade))
                        .decoration(TextDecoration.ITALIC, false);
                meta.displayName(component);
                lore.add(Component.text("§7Utilize essa bomba para"));
                lore.add(Component.text("§7minerar em grande área."));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§f  Área: §77x7"));
                lore.add(Component.text("§r"));
                lore.add(Component.text("§8Aremesse com o botão-direito."));
                meta.lore(lore);
                break;
        }

        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey("minasistema", "tier"), PersistentDataType.INTEGER, tier);
        data.set(new NamespacedKey("minasistema", "tipo"), PersistentDataType.STRING, especialidade);
        explosivo.setItemMeta(meta);
        return explosivo;
    }
}
