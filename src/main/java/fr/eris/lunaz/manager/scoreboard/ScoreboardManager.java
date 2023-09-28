package fr.eris.lunaz.manager.scoreboard;

import fr.eris.lunaz.manager.scoreboard.listeners.DynamicListeners;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class ScoreboardManager extends Manager {
    private final HashMap<Player, ScoreboardHandler> cachedScoreboard = new HashMap<>();
    private BukkitTask scoreboardTask;


    public void start() {
        scoreboardTask = BukkitTasks.asyncTimer(this::updateScoreboard, 20, 20);
        initScoreboard();
        new DynamicListeners();
    }

    public void stop() {
        scoreboardTask.cancel();
    }

    public void updateScoreboard() {
        if(cachedScoreboard.isEmpty() || scoreboardTask.isCancelled()) return;
        for(Player player : cachedScoreboard.keySet()) {
            cachedScoreboard.get(player).update();
        }
    }

    public void initScoreboard() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            registerPlayer(player);
        }
    }

    public void unregisterPlayer(Player player) {
        if(!cachedScoreboard.containsKey(player)) return;
        cachedScoreboard.get(player).stop();
        cachedScoreboard.remove(player);
    }

    public void registerPlayer(Player player) {
        if(cachedScoreboard.containsKey(player)) return;
        cachedScoreboard.put(player, new ScoreboardHandler(player));
    }
}
