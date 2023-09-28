package fr.eris.lunaz.utils;

import java.util.concurrent.TimeUnit;

public class TimerUtils {

    private static long startTime;

    public static void start() {
        startTime = System.nanoTime();
    }

    public static String getElapseTime() {
        long nanoSecondElapse = System.nanoTime() - startTime;
        return String.format("%02d:%02d:%02d:%03d", TimeUnit.NANOSECONDS.toHours(nanoSecondElapse),
                TimeUnit.NANOSECONDS.toMinutes(nanoSecondElapse) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.NANOSECONDS.toSeconds(nanoSecondElapse) % TimeUnit.MINUTES.toSeconds(1),
                TimeUnit.NANOSECONDS.toMillis(nanoSecondElapse) % TimeUnit.SECONDS.toMillis(1));
    }

}
