package fr.eris.lunaz.utils;

import fr.eris.lunaz.LunaZ;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTasks {
    public static void sync(Callable callable) {
        Bukkit.getScheduler().runTask(LunaZ.getInstance(), callable::call);
    }

    public static BukkitTask syncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLater(LunaZ.getInstance(), callable::call, delay);
    }

    public static BukkitTask syncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimer(LunaZ.getInstance(), callable::call, delay, value);
    }

    public static void async(Callable callable) {
        Bukkit.getScheduler().runTaskAsynchronously(LunaZ.getInstance(), callable::call);
    }

    public static BukkitTask asyncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(LunaZ.getInstance(), callable::call, delay);
    }

    public static BukkitTask asyncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(LunaZ.getInstance(), callable::call, delay, value);
    }


    public interface Callable {
        void call();
    }
}
