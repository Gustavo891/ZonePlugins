package org.gustaav.zoneBoosters.manager;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneBoosters.ZoneBoosters;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class BoosterManager {

    List<PlayerModel> activeBoosters = new ArrayList<>();
    NamespacedKey typeKey, durationKey, multiplierKey;
    ZoneBoosters plugin;
    MongoManager mongoManager;

    public BoosterManager(ZoneBoosters plugin) {
        typeKey = new NamespacedKey(plugin, "type");
        durationKey = new NamespacedKey(plugin, "duration");
        multiplierKey = new NamespacedKey(plugin, "multiplier");
        this.plugin = plugin;
        this.mongoManager = plugin.getMongo();
        activeBoosters = mongoManager.getAllPlayerModels();
        startBoosterRunnable();
    }

    public void startBoosterRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<PlayerModel> toRemove = new ArrayList<>();

                for (PlayerModel playerModel : activeBoosters) {
                    playerModel.setDuration(playerModel.getDuration() - 5000);

                    if (playerModel.getDuration() <= 0) {
                        toRemove.add(playerModel);
                        mongoManager.deleteBooster(playerModel);
                    } else {
                        mongoManager.savePlayerModel(playerModel);
                    }
                }
                activeBoosters.removeAll(toRemove);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L * 5);
    }

    public void addBooster(PlayerModel player) {
        PlayerModel existingBooster = getTypeBooster(player.getPlayerId(), player.getType());
        if (existingBooster != null) {
            existingBooster.setDuration(existingBooster.getDuration() + player.getDuration());
        } else {
            activeBoosters.add(player);

        }
    }

    public void removeBooster(PlayerModel player) {
        activeBoosters.remove(player);
    }
    public PlayerModel getTypeBooster(UUID player, BoosterTypes type) {
        return activeBoosters.stream()
                .filter(playerModel ->
                        playerModel.getType().equals(type) && playerModel.getPlayerId().equals(player))
                .findFirst()
                .orElse(null);
    }
    public boolean hasTypeBooster(UUID player, BoosterTypes type) {
        return activeBoosters.stream().anyMatch(playerModel ->
                playerModel.getType().equals(type) && playerModel.getPlayerId().equals(player));
    }

    public ItemStack giveBooster(BoosterTypes type, int multiplier, int duration) {

        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        ItemStack booster = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) booster.getItemMeta();
        String base64Texture;
        List<Component> lore = new ArrayList<>();
        switch (type) {
            case MINERACAO:
                meta.displayName(MiniMessage.miniMessage().deserialize("<color:#4aff2e>Estimulante de Mineração</color>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("§7Aumente a quantidade de drops"));
                lore.add(Component.text("§7que você ganha ao minerar.§r"));
                lore.add(Component.text(""));
                lore.add(Component.text("§f  Multiplicador: §7" + multiplier + "x"));
                lore.add(Component.text("§f  Duração: §b" + duration + "h"));
                lore.add(Component.text(""));
                lore.add(MiniMessage.miniMessage().deserialize("<color:#b3ffa3>Ative com o botão-direito.</color>").decoration(TextDecoration.ITALIC, false));
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkYzY5ZmVmYWQxODRlZThjOTE4NjkyOGRhOTgyYWRkZmQ3ODNiZWQ5OGQ5NTA4MzQwOGJmNDE5ZTBjY2NkMCJ9fX0=";
                break;
            case PLANTACAO:
                meta.displayName(MiniMessage.miniMessage().deserialize("<color:#4aff2e>Estimulante de Plantação</color>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("§7Aumente a quantidade de drops que"));
                lore.add(Component.text("§7você ganha ao farmar plantações.§r"));
                lore.add(Component.text(""));
                lore.add(Component.text("§f  Multiplicador: §7" + multiplier + "x"));
                lore.add(Component.text("§f  Duração: §b" + duration + "h"));
                lore.add(Component.text(""));
                lore.add(MiniMessage.miniMessage().deserialize("<color:#b3ffa3>Ative com o botão-direito.</color>").decoration(TextDecoration.ITALIC, false));
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3Yjk1ZjEyNDBlNWE0ZDAyNTg1OGIyZjJiZDgyNWE3M2QwNzkyMTM2YTE1N2VhOTI1ZTU2ZjllZTMxODUxYiJ9fX0=";
                break;
            case HABILIDADE:
                meta.displayName(MiniMessage.miniMessage().deserialize("<color:#4aff2e>Estimulante de Habilidade</color>").decoration(TextDecoration.ITALIC, false));
                lore.add(Component.text("§7Aumente a quantidade de experiência"));
                lore.add(Component.text("§7que você ganha nas habilidades.§r"));
                lore.add(Component.text(""));
                lore.add(Component.text("§f  Multiplicador: §7" + multiplier + "x"));
                lore.add(Component.text("§f  Duração: §b" + duration + "h"));
                lore.add(Component.text(""));
                lore.add(MiniMessage.miniMessage().deserialize("<color:#b3ffa3>Ative com o botão-direito.</color>").decoration(TextDecoration.ITALIC, false));
                base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZiYjk1NTAyZTBmZDBkYmE2MGJiZTU4MThlMmZkNTI3OGVkYTI5OTljYzJkNzgxMDljMmNhNTc3NTU3YTBhNiJ9fX0=";
                break;
            case null, default:
                return null;
        }

        meta.lore(lore);
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        persistentDataContainer.set(typeKey, PersistentDataType.STRING, type.toString());
        persistentDataContainer.set(durationKey, PersistentDataType.INTEGER, duration);
        persistentDataContainer.set(multiplierKey, PersistentDataType.INTEGER, multiplier);
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        booster.setItemMeta(meta);

        return booster;
    }



}
