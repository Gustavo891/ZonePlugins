package org.gustaav.zoneMissions.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMissions.ZoneMissions;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.MissionModel;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.TaskModel;

import java.io.File;
import java.util.*;

public class MongoManager {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;
    private final ZoneMissions plugin;
    private FileConfiguration config;
    private MissionManager manager;

    public MongoManager(ZoneMissions plugin) {
        this.plugin = plugin;
        createDatabaseFile();
        manager = plugin.getManager();
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


        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllMissions();
            }
        }.runTaskTimer(plugin, 0L, 100L);
    }

    public void saveAllMissions() {
        for(PlayerModel playerModel : manager.getPlayers()) {
            savePlayerModel(playerModel);
        }
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

    public void savePlayerModel(PlayerModel playerModel) {
        Document document = new Document()
                .append("uuid", playerModel.getUuid().toString())
                .append("mission", playerModel.getMission().getId())
                .append("status", playerModel.getStatus())
                .append("tasks", saveTasks(playerModel.getTaskProgress()));

        collection.replaceOne(
                Filters.eq("uuid", playerModel.getUuid().toString()),
                document,
                new com.mongodb.client.model.ReplaceOptions().upsert(true)
        );
    }

    public PlayerModel loadPlayerModel(Player player) {
        UUID uuid = player.getUniqueId();
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) {
            MissionModel missionModel = plugin.getMissionCreator().getMissions().getFirst();
            Map<TaskModel, Integer> map = new HashMap<>();
            for(TaskModel task : missionModel.getTasks()) {
                map.put(task, 0);
            }
            manager.sendStartMissionMessage(player, missionModel);
            return new PlayerModel(uuid, missionModel, map, "progresso");
        }

        Optional<MissionModel> optionalPlayerData = plugin.getMissionCreator().getMissions().stream()
                .filter(mission -> mission.getId().equals(document.getString("mission")))
                .findFirst();
        MissionModel missionModel = optionalPlayerData.orElse(plugin.getMissionCreator().getMission01());

        return new PlayerModel(uuid, missionModel, loadTasks(document.get("tasks", Document.class), missionModel), document.getString("status"));
    }

    private Document saveTasks(Map<TaskModel, Integer> tasks) {
        Document tasksDocument = new Document();
        for (Map.Entry<TaskModel, Integer> entry : tasks.entrySet()) {
            tasksDocument.append(entry.getKey().getName(), entry.getValue());
        }
        return tasksDocument;
    }
    private Map<TaskModel, Integer> loadTasks(Document tasksDocument, MissionModel missionModel) {
        Map<TaskModel, Integer> tasks = new HashMap<>();
        for (String key : tasksDocument.keySet()) {
            Optional<TaskModel> optionalTask = missionModel.getTasks().stream()
                    .filter(task -> task.getName().equals(key))
                    .findFirst();

            optionalTask.ifPresent(task -> tasks.put(task, tasksDocument.getInteger(key)));
        }
        return tasks;
    }

}
