package org.gustaav.zoneEssential.kits;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEssential.ZoneEssential;

import java.io.*;
import java.util.*;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class KitManager {

    private FileConfiguration kitsFile, playersConfig;
    private final ZoneEssential plugin;
    private File kitsConfigFile, playersFile;
    private final List<KitModel> kits;  // Lista para armazenar os kits carregados

    public KitManager(ZoneEssential plugin) {
        this.plugin = plugin;
        this.kits = new ArrayList<>();
        createKitsFile();
        createPlayersFile(); // Criar o arquivo players.yml ao inicializar o KitManager
        loadAllKits();  // Carregar os kits na inicialização
    }

    private void createKitsFile() {
        kitsConfigFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitsConfigFile.exists()) {
            kitsConfigFile.getParentFile().mkdirs();
            plugin.saveResource("kits.yml", false);
        }
        kitsFile = YamlConfiguration.loadConfiguration(kitsConfigFile);
    }

    public void loadAllKits() {
        kits.clear(); // Limpar lista existente de kits
        String kitsPath = "kits";

        if (kitsFile.contains(kitsPath)) {
            Set<String> kitNames = Objects.requireNonNull(kitsFile.getConfigurationSection(kitsPath)).getKeys(false);

            for (String kitType : kitNames) {
                String path = kitsPath + "." + kitType;
                String name = kitsFile.getString(path + ".name");
                int delay = kitsFile.getInt(path + ".delay");

                List<ItemStack> items = new ArrayList<>();

                // Obtenha os itens serializados e faça a verificação de tipo
                Object serializedItemsObj = kitsFile.get(path + ".items");
                if (serializedItemsObj instanceof List<?> rawList) {

                    // Verifica se todos os elementos são mapas e faz o cast
                    for (Object obj : rawList) {
                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> serializedItem = (Map<String, Object>) obj;
                            ItemStack item = deserializeItem(serializedItem);
                            if (item != null) {
                                items.add(item);
                            }
                        }
                    }
                }

                // Adicionar o kit carregado à lista de kits
                kits.add(new KitModel(name, kitType, delay, items));
            }
        }
    }


    public void saveAllKits() {
        String kitsPath = "kits";
        kitsFile.set(kitsPath, null);
        for (KitModel kit : kits) {
            String path = kitsPath + "." + kit.getKitType();

            kitsFile.set(path + ".name", kit.getKitName());
            kitsFile.set(path + ".delay", kit.getDelay());

            List<Map<String, Object>> serializedItems = new ArrayList<>();

            for (ItemStack item : kit.getListaItens()) {
                Map<String, Object> serializedItem = serializeItem(item);
                if (serializedItem != null) {
                    serializedItems.add(serializedItem);
                }
            }

            kitsFile.set(path + ".items", serializedItems);
        }

        try {
            kitsFile.save(kitsConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Método para serializar os itens do kit em Base64
    public Map<String, Object> serializeItem(ItemStack item) {
        return item.serialize();
    }

    // Método para desserializar os itens do kit a partir do Base64
    public ItemStack deserializeItem(Map<String, Object> itemData) {
        return ItemStack.deserialize(itemData);
    }


    public void addKit(KitModel kit) {
        kits.add(kit);
        saveAllKits();
    }

    public void removeKit(KitModel kit) {
        kits.remove(kit);
        saveAllKits();
    }

    public void setKitCooldown(UUID playerUUID, String kitName, long timestamp) {
        playersConfig.set(playerUUID + ".kits." + kitName, timestamp);
        savePlayersConfig();
    }

    public long getKitCooldown(UUID playerUUID, String kitName) {
        return playersConfig.getLong(playerUUID + ".kits." + kitName, 0);
    }

    // Salva o arquivo players.yml
    private void savePlayersConfig() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Criação do arquivo players.yml para armazenar os cooldowns dos jogadores
    private void createPlayersFile() {
        playersFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            try {
                playersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playersConfig = YamlConfiguration.loadConfiguration(playersFile);
    }
    // Método para obter todos os kits carregados
    public List<KitModel> getKits() {
        return kits;
    }

    public String formatTime(long millis) {
        long weeks = TimeUnit.MILLISECONDS.toDays(millis) / 7;
        long days = TimeUnit.MILLISECONDS.toDays(millis) % 7;
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        StringBuilder time = new StringBuilder();
        if (weeks > 0) time.append(weeks).append("s ");
        if (days > 0) time.append(days).append("d ");
        if (hours > 0) time.append(hours).append("h ");
        if (minutes > 0) time.append(minutes).append("m ");
        if (seconds > 0) time.append(seconds).append("s");

        return time.toString().trim();
    }

}
