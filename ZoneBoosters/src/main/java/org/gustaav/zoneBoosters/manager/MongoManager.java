package org.gustaav.zoneBoosters.manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.gustaav.zoneBoosters.ZoneBoosters;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoManager {


    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private FileConfiguration config;
    ZoneBoosters plugin;

    public MongoManager(ZoneBoosters plugin) {

        this.plugin = plugin;
        createDatabaseFile();

        String usuario = config.getString("mongodb.user");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String server = config.getString("mongodb.database");

        String uri = "mongodb+srv://" + usuario + ":" + password + "@" + host + "/" + server + "?retryWrites=true&w=majority";
        MongoClientURI clientUri = new MongoClientURI(uri);

        mongoClient = new MongoClient(clientUri);

        assert server != null;
        MongoDatabase database = mongoClient.getDatabase(server);

        collection = database.getCollection("boosters");
    }

    private void createDatabaseFile() {
        File locaisFile = new File(plugin.getDataFolder(), "database.yml");
        if(!locaisFile.exists()) {
            locaisFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(locaisFile);
    }

    public List<PlayerModel> getAllPlayerModels() {
        List<PlayerModel> playerModels = new ArrayList<>();

        // Recupera todos os documentos da coleção
        for (Document document : collection.find()) {
            // Extrai os campos do documento
            UUID uuid = UUID.fromString(document.getString("uuid"));
            BoosterTypes type = BoosterTypes.valueOf(document.getString("type"));
            long duration = document.getLong("duration");
            int multiplier = document.getInteger("multiplier");

            // Converte para PlayerModel e adiciona à lista
            playerModels.add(new PlayerModel(uuid, type, duration, multiplier));
        }

        return playerModels;
    }

    public void savePlayerModel(PlayerModel player) {
        Document document = new Document()
                .append("uuid", player.getPlayerId().toString())
                .append("type", player.getType().toString())
                .append("duration", player.getDuration())
                .append("multiplier", player.getMultiplier());

        collection.replaceOne(
                Filters.and(
                        Filters.eq("uuid", player.getPlayerId().toString()),
                        Filters.eq("type", player.getType().toString())
                ),
                document,
                new com.mongodb.client.model.ReplaceOptions().upsert(true)
        );
    }

    public void deleteBooster(PlayerModel player) {
        collection.deleteOne(
                Filters.and(
                        Filters.eq("uuid", player.getPlayerId().toString()),
                        Filters.eq("type", player.getType().toString())
                )
        );
    }


    public void close() {
        mongoClient.close();
    }

}
