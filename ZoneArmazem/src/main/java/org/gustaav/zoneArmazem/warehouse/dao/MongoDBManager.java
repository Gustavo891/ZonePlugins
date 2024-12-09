package org.gustaav.zoneArmazem.warehouse.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.PlotId;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneArmazem.ZoneArmazem;
import org.gustaav.zoneArmazem.warehouse.models.WarehouseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDBManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private final ZoneArmazem plugin;
    private FileConfiguration config;
    PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea("plots", "1");

    public MongoDBManager(ZoneArmazem plugin) {
        this.plugin = plugin;
        createDatabaseFile();

        String usuario = config.getString("mongodb.user");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String server = config.getString("mongodb.database");
        String collectionString = config.getString("mongodb.collection");

        String uri = "mongodb+srv://" + usuario + ":" + password + "@" + host;
        MongoClientURI clientUri = new MongoClientURI(uri);

        mongoClient = new MongoClient(clientUri);
        assert server != null;
        MongoDatabase database = mongoClient.getDatabase(server);
        assert collectionString != null;
        collection = database.getCollection(collectionString);

        // Carregar os armazéns no início do servidor
        loadAllWarehouses();

        // Agendar a atualização a cada 5 segundos
        new BukkitRunnable() {
            @Override
            public void run() {
                updateWarehouses();
            }
        }.runTaskTimer(plugin, 0L, 100L); // Executar a cada 5 segundos (100 ticks = 5 segundos)
    }

    private void createDatabaseFile() {
        // Método para criar o arquivo de configuração para o MongoDB
        File locaisFile = new File(plugin.getDataFolder(), "database.yml");
        if (!locaisFile.exists()) {
            locaisFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(locaisFile);
    }

    /**
     * Carrega todos os armazéns do MongoDB para a lista.
     */
    public void loadAllWarehouses() {
        List<WarehouseModel> armazens = new ArrayList<>();
        for (Document document : collection.find()) {
            WarehouseModel warehouse = loadWarehouseModel(document.getString("plotId"));
            if (warehouse != null) {
                armazens.add(warehouse);
            }
        }
        plugin.getManager().setArmazens(armazens); // Atualiza a lista de armazéns no Manager
    }

    /**
     * Carrega um WarehouseModel específico.
     */
    public WarehouseModel loadWarehouseModel(String plotId) {
        Document document = collection.find(Filters.eq("plotId", plotId)).first();
        if (document == null) {
            return null;
        }

        // Agora apenas recupera o plotId para obter o Plot.
        Plot plot = plotArea.getPlot(PlotId.fromString(document.getString("plotId")));

        WarehouseModel warehouse = new WarehouseModel(plot, document.getInteger("nivel"), document.getBoolean("mode"));
        warehouse.setDrops(deserializeDrops(document.get("drops", Document.class)));
        return warehouse;
    }


    /**
     * Atualiza todos os armazéns no MongoDB.
     */
    public void updateWarehouses() {
        for (WarehouseModel warehouse : plugin.getManager().getArmazens()) {
            saveWarehouseModel(warehouse);
        }
    }

    /**
     * Salva um WarehouseModel no MongoDB.
     */
    public void saveWarehouseModel(WarehouseModel warehouse) {
        Document document = new Document()
                .append("plotId", warehouse.getPlotId().getId().toString())
                .append("mode", warehouse.getMode())
                .append("nivel", warehouse.getNivel())
                .append("drops", serializeDrops(warehouse.getDrops()));

        collection.replaceOne(
                Filters.eq("plotId", warehouse.getPlotId().getId().toString()),
                document,
                new com.mongodb.client.model.ReplaceOptions().upsert(true)
        );
    }

    /**
     * Serializa os drops para um formato que pode ser armazenado no MongoDB.
     */
    private Document serializeDrops(Map<Material, Integer> drops) {
        Document dropsDocument = new Document();
        for (Map.Entry<Material, Integer> entry : drops.entrySet()) {
            dropsDocument.append(entry.getKey().name().toLowerCase(), entry.getValue());
        }
        return dropsDocument;
    }

    /**
     * Desserializa os drops de um Document do MongoDB.
     */
    private Map<Material, Integer> deserializeDrops(Document dropsDocument) {
        Map<Material, Integer> drops = new HashMap<>();
        for (String key : dropsDocument.keySet()) {
            Material material = Material.matchMaterial(key.toUpperCase());
            if (material != null) {
                drops.put(material, dropsDocument.getInteger(key));
            }
        }
        return drops;
    }

    public void close() {
        mongoClient.close();
    }
}
