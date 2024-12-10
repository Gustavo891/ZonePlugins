package org.gustaav.zonePacotes.packages;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zonePacotes.ZonePacotes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PackageManager {

    private File packageFile;
    private final FileConfiguration packageConfig;
    private final List<PackageModel> packages = new ArrayList<>();
    private final ZonePacotes plugin;
    private final NamespacedKey packageKey;

    public PackageManager(ZonePacotes plugin) {
        this.plugin = plugin;
        // Inicializa a configuração após garantir que o arquivo foi criado ou copiado
        this.packageFile = new File(plugin.getDataFolder(), "pacotes.yml");
        this.packageKey = new NamespacedKey(plugin, "package");
        this.packageConfig = YamlConfiguration.loadConfiguration(packageFile);
        loadPackages();
    }

    public void loadPackages() {
        File pluginFolder = plugin.getDataFolder();

        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        packageFile = new File(plugin.getDataFolder(), "pacotes.yml");

        // Verifica se o arquivo pacotes.yml existe, se não, copia o arquivo de recursos
        if (!packageFile.exists()) {
            try {
                copyDefaultConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        packages.clear();

        if (packageConfig.contains("pacotes")) {
            for (String packageId : packageConfig.getConfigurationSection("pacotes").getKeys(false)) {
                String basePath = "pacotes." + packageId;

                String displayName = packageConfig.getString(basePath + ".displayName", "");
                List<String> lore = packageConfig.getStringList(basePath + ".lore");
                String head = packageConfig.getString(basePath + ".head", "");
                String colorCode = packageConfig.getString(basePath + ".colorCode", "");

                List<RewardModel> rewards = new ArrayList<>();
                if (packageConfig.contains(basePath + ".rewards")) {
                    for (String rewardId : packageConfig.getConfigurationSection(basePath + ".rewards").getKeys(false)) {
                        String rewardPath = basePath + ".rewards." + rewardId;

                        String rewardDisplayName = packageConfig.getString(rewardPath + ".displayName", "");
                        List<String> rewardLore = packageConfig.getStringList(rewardPath + ".lore");
                        String material = packageConfig.getString(rewardPath + ".material", "STONE");
                        String rewardHead = packageConfig.getString(rewardPath + ".head", "");
                        int weight = packageConfig.getInt(rewardPath + ".weight", 1);
                        String rarity = packageConfig.getString(rewardPath + ".rarity", "NORMAL");
                        String command = packageConfig.getString(rewardPath + ".command", "");

                        Material itemMaterial = Material.STONE;
                        try {
                            itemMaterial = Material.valueOf(material);
                        } catch (Exception ignored) {
                        }

                        rewards.add(new RewardModel(
                                rewardId,
                                rewardDisplayName,
                                rarity,
                                command,
                                rewardLore,
                                itemMaterial,
                                rewardHead,
                                weight
                        ));
                    }
                }

                PackageModel model = new PackageModel(packageId, displayName, lore, colorCode, head, rewards);
                plugin.getServer().getLogger().info("model: " + model.toString());
                packages.add(model);
            }
        }
    }
    public String checkPackage(ItemStack item) {
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        if(data.has(packageKey)) {
            return data.get(packageKey, PersistentDataType.STRING);
        } else {
            return null;
        }
    }
    public ItemStack givePackage(PackageModel packageModel) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        profile.setProperty(new ProfileProperty("textures", packageModel.head));
        meta.setPlayerProfile(profile);

        meta.displayName(MiniMessage.miniMessage().deserialize(packageModel.displayName).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        for(String line : packageModel.lore) {
            lore.add(MiniMessage.miniMessage().deserialize(line).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(packageKey, PersistentDataType.STRING, packageModel.id);

        item.setItemMeta(meta);

        return item;
    }
    public PackageModel getPackage(String id) {
        return packages.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
    public void editPackage(PackageModel updatedPackage) {
        packages.removeIf(p -> p.getId().equalsIgnoreCase(updatedPackage.getId()));
        packages.add(updatedPackage);
        savePackages();
    }
    public void savePackages() {
        packageConfig.set("pacotes", null);

        for (PackageModel packageModel : packages) {
            String basePath = "pacotes." + packageModel.getId();
            packageConfig.set(basePath + ".displayName", packageModel.getDisplayName());
            packageConfig.set(basePath + ".lore", packageModel.getLore());
            packageConfig.set(basePath + ".head", packageModel.getHead());
            packageConfig.set(basePath + ".colorCode", packageModel.getColorCode());

            for (RewardModel reward : packageModel.getRewards()) {
                String rewardPath = basePath + ".rewards." + reward.getId();
                packageConfig.set(rewardPath + ".displayName", reward.getDisplayName());
                packageConfig.set(rewardPath + ".lore", reward.getLore());
                packageConfig.set(rewardPath + ".material", reward.getMaterial().name());
                packageConfig.set(rewardPath + ".head", reward.getHead());
                packageConfig.set(rewardPath + ".weight", reward.getWeight());
                packageConfig.set(rewardPath + ".rarity", reward.getRarity());
                packageConfig.set(rewardPath + ".command", reward.getCommand());
            }
        }

        try {
            packageConfig.save(packageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void copyDefaultConfig() throws IOException {
        // Cria o arquivo e copia o conteúdo do arquivo de recursos
        InputStream in = plugin.getResource("pacotes.yml");
        if (in == null) {
            throw new IllegalStateException("Não foi possível encontrar pacotes.yml no diretório resources");
        }

        // Cria o arquivo e copia o conteúdo
        Files.copy(in, packageFile.toPath());
        in.close();
    }

}
