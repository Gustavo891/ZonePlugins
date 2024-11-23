package org.gustaav.zoneShop.dao;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.gustaav.zoneShop.ZoneShop;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final ZoneShop plugin;

    public FileManager(ZoneShop plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration loadFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName + ".yaml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Cria o arquivo se não existir
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yaml", false);
        }

        return config;
    }

    public void saveFile(String fileName, FileConfiguration config) {
        File file = new File(plugin.getDataFolder(), fileName + ".yaml");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
