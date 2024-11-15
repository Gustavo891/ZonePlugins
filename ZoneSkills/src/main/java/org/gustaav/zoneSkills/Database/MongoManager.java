package org.gustaav.zoneSkills.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.gustaav.zoneSkills.ZoneSkills;

import java.io.File;
import java.util.UUID;

public class MongoManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private FileConfiguration config;
    ZoneSkills plugin;

    public MongoManager(ZoneSkills plugin) {

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

        collection = database.getCollection("playerData");
    }

    private void createDatabaseFile() {
        File locaisFile = new File(plugin.getDataFolder(), "database.yml");
        if(!locaisFile.exists()) {
            locaisFile.getParentFile().mkdirs();
            plugin.saveResource("database.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(locaisFile);
    }

    public Document getPlayerData(UUID uuid) {
        Document query = new Document("uuid", uuid.toString());
        Document playerData = collection.find(query).first();
        if (playerData == null) {
            playerData = new Document("uuid", uuid.toString())
                    .append("skills", new Document());
            collection.insertOne(playerData);
        }
        return playerData;
    }

    public void updatePlayerData(UUID uuid, Document playerData) {
        Document query = new Document("uuid", uuid.toString());
        collection.replaceOne(query, playerData);
    }

    public void close() {
        mongoClient.close();
    }
}
