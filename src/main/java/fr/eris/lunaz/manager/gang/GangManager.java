package fr.eris.lunaz.manager.gang;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.commands.GangExecutor;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GangManager extends Manager {

    private final HashMap<UUID, Gang> cache = new HashMap<>();
    private BukkitTask savingTask;

    public void start() {
        new GangExecutor();
        savingTask = BukkitTasks.asyncTimer(this::saveAllGang, 1_200, 1_200);
    }

    public void stop() {
        for(Gang gang : cache.values()) {
            gang.getChestUpgrade().saveItem();
        }
        saveAllGang();
        savingTask.cancel();
    }

    public void saveAllGang() {
        cache.values().forEach(this::saveGang);
    }

    public Gang getGang(UUID gangID) {
        if(cache.containsKey(gangID)) return cache.get(gangID);
        Gang gang = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "gang"),
                gangID.toString()), Gang.class);
        if(gang != null) cache.put(gangID, gang);
        return gang;
    }

    public void saveGang(Gang gang) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "gang"),
                gang.getGangID().toString()), gang);
    }

    public List<Gang> getAllGang(boolean addItToCache) {
        List<Gang> gangList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "gang"), ".json")) {
            Gang newGang = getGang(UUID.fromString(file.getName().replace(".json", "")));
            if(newGang != null) {
                gangList.add(newGang);
                if (addItToCache && !cache.containsKey(newGang.getGangID()))
                    cache.put(UUID.fromString(newGang.getGangID().toString()), newGang);
            }
        }
        return gangList;
    }

    public List<Gang> getAllOnlineGang(boolean sorted) {
        List<Gang> onlineGangList = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            Gang newGang = getPlayerGang(player);
            if(onlineGangList.contains(newGang)) continue;
            onlineGangList.add(newGang);
        }
        if(sorted) {
            return onlineGangList.stream().sorted((gang1, gang2) -> {
                return gang1.getOnlineMemberCount() - gang2.getOnlineMemberCount();
            }).collect(Collectors.toList());
        }
        return onlineGangList;
    }

    public Gang getPlayerGang(Player player) {
        GangPlayer gangPlayer = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        if(gangPlayer.getGangRank() == GangRank.NO_GANG) return null;
        else return getGang(gangPlayer.getGangID());
    }

    public Gang getGangByName(String gangName) {
        for(Gang currentGang : getAllGang(true)) {
            if(currentGang.getGangName().equalsIgnoreCase(gangName)) return currentGang;
        }
        return null;
    }

    public void deleteGang(UUID gangID) {
        if(JsonUtils.isExist(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "gang"), gangID.toString())) {
            JsonUtils.deleteJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "gang"), gangID.toString());
        }
        cache.remove(gangID);
    }
}
