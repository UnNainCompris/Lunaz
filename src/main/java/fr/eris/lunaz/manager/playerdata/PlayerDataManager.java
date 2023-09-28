package fr.eris.lunaz.manager.playerdata;

import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager extends Manager {
    private HashMap<UUID, PlayerData> caches = new HashMap<>();
    private BukkitTask savingTask;

    public void start() {
        savingTask = BukkitTasks.asyncTimer(this::saveAllData, 1_200, 1_200);
    }

    public void stop() {
        saveAllData();
        savingTask.cancel();
    }

    public PlayerData getPlayerdata(UUID UUID) {
        if(caches.containsKey(UUID))
            return caches.get(UUID);
        PlayerData data = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"), UUID.toString()), PlayerData.class);
        if(data == null) data = new PlayerData(UUID);
        caches.put(UUID, data);
        return data;
    }

    public void saveAllData() {
        caches.values().forEach(this::savePlayerData);
    }

    public void savePlayerData(PlayerData playerData) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"), playerData.getUuid().toString()), playerData);
    }

    public void deletePlayerData(UUID UUID) {
        FileUtils.deleteFile(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"), UUID.toString()));
    }

    public void savePlayerData(UUID UUID) {
        if(!caches.containsKey(UUID)) JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"),
                UUID.toString()), new PlayerData(UUID));
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"), UUID.toString()), caches.get(UUID));
    }

    public boolean exist(UUID UUID) {
        return FileUtils.isExist(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "playerdata"), UUID.toString() + ".json");
    }

    public void emptyCache(UUID uniqueId) {
        savePlayerData(uniqueId);
        caches.remove(uniqueId);
    }

    public void emptyCache() {
        saveAllData();
        caches = new HashMap<>();
    }
}
