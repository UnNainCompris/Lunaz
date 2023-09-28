package fr.eris.lunaz.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AsyncTask {
    private static final ScheduledExecutorService userSaverScheduler = Executors.newScheduledThreadPool(4);

    public static void async(Runnable task) {
        userSaverScheduler.execute(task);
    }

    public static void asyncWait(Runnable task, long delay) {
        userSaverScheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> asyncRepeat(Runnable task, int initDelay, int delay) {
        return userSaverScheduler.scheduleAtFixedRate(task, initDelay, delay, TimeUnit.MILLISECONDS);
    }
}
