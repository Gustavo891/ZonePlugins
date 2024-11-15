package org.gustaav.zoneSkills.utils;

public class RandomUtil {

    public static String format(double amount) {
        if (amount < 1000) {
            return String.format("%.2f", amount);
        } else if (amount < 1_000_000) {
            return String.format("%.2fk", amount / 1000);
        } else if (amount < 1_000_000_000) {
            return String.format("%.2fM", amount / 1_000_000);
        } else if (amount < 1_000_000_000_000L) {
            return String.format("%.2fB", amount / 1_000_000_000);
        } else {
            return String.format("%.2fT", amount / 1_000_000_000_000L);
        }
    }

}
