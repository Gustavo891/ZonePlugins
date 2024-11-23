package org.gustaav.zoneRanks.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.gustaav.zoneRanks.ZoneRanks;

import java.io.File;
import java.util.UUID;

public class MongoManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private FileConfiguration config;
    ZoneRanks plugin;

    public MongoManager(ZoneRanks plugin) {

        this.plugin = plugin;
        createDatabaseFile();

        String usuario = config.getString("mongodb.user");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String server = config.getString("mongodb.database");
        String collectionId = config.getString("mongodb.collection");

        String uri = "mongodb://" + usuario + ":" + password + "@" + host;
        MongoClientURI clientUri = new MongoClientURI(uri);

        mongoClient = new MongoClient(clientUri);

        assert server != null;
        MongoDatabase database = mongoClient.getDatabase(server);

        assert collectionId != null;
        collection = database.getCollection(collectionId);
    }

    private void createDatabaseFile() {
        File locaisFile = new File(plugin.getDataFolder(), "database.yml");
        if(!locaisFile.exists()) {
            locaisFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(locaisFile);
    }

    public void savePlayerRank(UUID uuid, String rank) {
        Document filter = new Document("uuid", uuid.toString());
        Document update = new Document("$set", new Document("rank", rank));
        collection.updateOne(filter, update, new com.mongodb.client.model.UpdateOptions().upsert(true));
    }

    public String getPlayerRank(UUID uuid) {
        Document filter = new Document("uuid", uuid.toString());
        Document playerData = collection.find(filter).first();
        if (playerData != null) {
            return playerData.getString("rank");
        }
        return null;
    }


    public void close() {
        mongoClient.close();
    }

}
