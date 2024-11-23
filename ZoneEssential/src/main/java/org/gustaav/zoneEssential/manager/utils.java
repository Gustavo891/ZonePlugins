package org.gustaav.zoneEssential.manager;

import java.util.concurrent.TimeUnit;

public class utils {

    public static String formatTime(long millis) {
        long weeks = TimeUnit.MILLISECONDS.toDays(millis) / 7;
        long days = TimeUnit.MILLISECONDS.toDays(millis) % 7;
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        StringBuilder time = new StringBuilder();
        if (weeks > 0) time.append(weeks).append("s ");
        if (days > 0) time.append(days).append("d ");
        if (hours > 0) time.append(hours).append("h ");
        if (minutes > 0) time.append(minutes).append("m ");
        if (seconds > 0) time.append(seconds).append("s");

        return time.toString().trim();
    }


}
