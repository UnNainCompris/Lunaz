package fr.eris.lunaz.manager.stats.task;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.stats.data.Stats;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.nms.NmsUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class StatsUpdater {

    @Getter private BukkitTask updateTask;

    public void start() {
        updateTask = BukkitTasks.asyncTimer(this::update, 2L, 2L);
    }

    public void update() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerStats(player);
        }
    }

    public void updatePlayerStats(Player player) {
        Stats playerStats = LunaZ.getStatsManager().getGlobalStats(player);

        if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playerStats.getHealth());
        }
        NmsUtils.changeEntityMovementSpeed(player, playerStats.getSpeed() / 500d);
    }

    public void stop() {
        updateTask.cancel();
    }
}
