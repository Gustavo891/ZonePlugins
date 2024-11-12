package org.gustaav.zoneSkills.Database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

public class MongoManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public MongoManager() {
        mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("server");
        collection = database.getCollection("playerData");
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
