package org.gustaav.zoneEconomy.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.gustaav.zoneEconomy.ZoneEconomy;
import org.gustaav.zoneEconomy.model.PlayerModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private FileConfiguration config;
    ZoneEconomy plugin;

    public MongoManager(ZoneEconomy plugin) {

        this.plugin = plugin;
        createDatabaseFile();

        String usuario = config.getString("mongodb.user");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String server = config.getString("mongodb.database");

        String uri = "mongodb://" + usuario + ":" + password + "@" + host;
        MongoClientURI clientUri = new MongoClientURI(uri);

        mongoClient = new MongoClient(clientUri);

        assert server != null;
        MongoDatabase database = mongoClient.getDatabase(server);

        collection = database.getCollection("economy");
    }

    private void createDatabaseFile() {
        File locaisFile = new File(plugin.getDataFolder(), "database.yml");
        if(!locaisFile.exists()) {
            locaisFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(locaisFile);
    }

    public PlayerModel getPlayerModel(UUID uuid) {
        Document filter = new Document("uuid", uuid.toString());
        Document playerData = collection.find(filter).first();
        if (playerData != null) {

            return new PlayerModel(uuid, playerData.getDouble("money"));
        }
        return null;
    }

    public void savePlayerRank(PlayerModel player) {
        Document filter = new Document("uuid", player.getUuid().toString());
        Document update = new Document("$set", new Document("money", player.getMoney()));
        collection.updateOne(filter, update, new com.mongodb.client.model.UpdateOptions().upsert(true));
    }

    public List<PlayerModel> getTopPlayers(int limit) {
        return collection.find()
                .sort(Sorts.descending("money")) // Ordena por dinheiro em ordem decrescente
                .limit(limit) // Limita ao número especificado de resultados
                .into(new ArrayList<>())
                .stream()
                .map(doc -> new PlayerModel(
                        UUID.fromString(doc.getString("uuid")),
                        doc.getDouble("money")
                ))
                .collect(Collectors.toList());
    }

    public void close() {
        mongoClient.close();
    }
}
