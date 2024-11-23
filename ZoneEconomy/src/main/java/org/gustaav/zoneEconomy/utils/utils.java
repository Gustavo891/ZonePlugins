package org.gustaav.zoneEconomy.utils;

import java.awt.*;

public class utils {

    public static Color mainColor = new Color(0x7AFF6F);

    public static String format(double amount) {
        if (amount < 1000) {
            return String.valueOf(amount);
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

    public static double parseFormattedValue(String value) {
        value = value.toUpperCase();
        if (value.endsWith("K")) {
            return Double.parseDouble(value.replace("K", "")) * 1_000;
        } else if (value.endsWith("M")) {
            return Double.parseDouble(value.replace("M", "")) * 1_000_000;
        } else if (value.endsWith("B")) {
            return Double.parseDouble(value.replace("B", "")) * 1_000_000_000;
        } else if (value.endsWith("T")) {
            return Double.parseDouble(value.replace("T", "")) * 1_000_000_000_000L;
        } else {
            return Double.parseDouble(value);
        }
    }


}
