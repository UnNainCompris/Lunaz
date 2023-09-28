package fr.eris.lunaz.manager.stats.task;

import fr.eris.lunaz.manager.stats.data.Stats;
import fr.eris.lunaz.manager.stats.data.TemporaryStats;
import fr.eris.lunaz.utils.BukkitTasks;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemporaryStatsUpdater {

    @Getter private BukkitTask updateTask;
    private HashMap<Player, List<TemporaryStats>> temporaryStatsMap;

    public void start() {
        temporaryStatsMap = new HashMap<>();
        updateTask = BukkitTasks.asyncTimer(this::update, 1L, 1L);
    }

    public void update() {
        for(Player player : new ArrayList<>(temporaryStatsMap.keySet())) {
            temporaryStatsMap.get(player).removeIf(TemporaryStats::update);
            if(temporaryStatsMap.get(player).isEmpty()) temporaryStatsMap.remove(player);
        }
    }

    public boolean haveBoostByReason(String boostReason, Player player) {
        for(TemporaryStats temporaryStats : temporaryStatsMap.get(player)) {
            if(temporaryStats.getReason().equals(boostReason)) return true;
        }
        return false;
    }

    public TemporaryStats getBoostByReason(String boostReason, Player player) {
        for(TemporaryStats temporaryStats : temporaryStatsMap.get(player)) {
            if(temporaryStats.getReason().equals(boostReason)) return temporaryStats;
        }
        return null;
    }

    public void addTemporaryStats(Player player, TemporaryStats newTemporaryStats, Action todoIfAlreadyContains) {
        if(!temporaryStatsMap.containsKey(player)) temporaryStatsMap.put(player, new ArrayList<>());
        if(!haveBoostByReason(newTemporaryStats.getReason(), player)) {
            temporaryStatsMap.get(player).add(newTemporaryStats);
            return;
        }
        if(todoIfAlreadyContains == Action.SET_NEW_TIMER) {
            TemporaryStats temporaryStats = getBoostByReason(newTemporaryStats.getReason(), player);
            temporaryStats.setTimer(newTemporaryStats.getMaxTickTime(), 0);
        }
        else if(todoIfAlreadyContains == Action.ADD_TO_TIMER) {
            TemporaryStats temporaryStats = getBoostByReason(newTemporaryStats.getReason(), player);
            temporaryStats.setMaxTickTime(newTemporaryStats.getMaxTickTime() + temporaryStats.getMaxTickTime());
        }
        else if(todoIfAlreadyContains == Action.ADD_TO_TIMER_AND_RESET) {
            TemporaryStats temporaryStats = getBoostByReason(newTemporaryStats.getReason(), player);
            temporaryStats.setTimer(newTemporaryStats.getMaxTickTime() + temporaryStats.getMaxTickTime(), 0);
        }
        else if(todoIfAlreadyContains == Action.ADD) {
            temporaryStatsMap.get(player).add(newTemporaryStats);
        }
    }

    public Stats getTemporaryStats(Player player) {
        Stats finalStats = Stats.emptyStats();
        if(!temporaryStatsMap.containsKey(player)) return finalStats;
        for(TemporaryStats temporaryStats : temporaryStatsMap.get(player)) {
            finalStats.addStats(temporaryStats.getStats());
        }
        return finalStats;
    }

    public void stop() {
        updateTask.cancel();
    }

    public enum Action {
        SET_NEW_TIMER,
        ADD_TO_TIMER,
        ADD_TO_TIMER_AND_RESET,
        ADD,
        CANCEL;
    }
}
