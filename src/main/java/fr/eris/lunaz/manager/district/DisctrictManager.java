package fr.eris.lunaz.manager.district;

import fr.eris.lunaz.manager.district.data.District;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;

public class DisctrictManager extends Manager {
    private HashMap<String, District> caches = new HashMap<>();
    private BukkitTask savingTask;

    public void start() {
        savingTask = BukkitTasks.asyncTimer(this::saveAllData, 60_000, 60_000);
        saveDistrict(District.defaultDistrict());
    }

    public void stop() {
        savingTask.cancel();
    }

    public District getDistrict(String name) {
        if(caches.containsKey(name))
            return caches.get(name);
        District district = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), name), District.class);
        if(district == null) return null;
        caches.put(name, district);
        return district;
    }

    public void saveAllData() {
        if(savingTask.isCancelled()) return;
        caches.values().forEach(this::saveDistrict);
    }

    public void saveDistrict(District district) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), district.getDistrictName()), district);
    }

    public void saveDistrict(String name) {
        if(!caches.containsKey(name)) return;
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), name), caches.get(name));
    }

    public void deleteDistrict(String name) {
        FileUtils.deleteFile(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), name));
    }

    public boolean exist(String name) {
        return FileUtils.isExist(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), name + ".json");
    }

    public void emptyCache() {
        saveAllData();
        caches = new HashMap<>();
    }

    public District getDistrictFromLocation(Location location) {
        loadAllDistrict();
        for(District district : caches.values()) {
            if(district.isIn(location)) return district;
        }
        return null;
    }

    private void loadAllDistrict() {
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "district"), ".json")) {
            getDistrict(file.getName().replace(".json", ""));
        }
    }
}
