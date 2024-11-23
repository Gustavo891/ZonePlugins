package org.gustaav.zonePlots.utils;

public class NumberUtils {

    public static String barraProgresso(double percentual) {
        int barrasTotais = 10;

        percentual = Math.min(Math.max(percentual, 0), 100);

        int barrasPreenchidas = (int) (percentual / 100 * barrasTotais);
        int barrasVazias = barrasTotais - barrasPreenchidas;

        return "§2" + "⬛".repeat(Math.max(0, barrasPreenchidas)) +
                "§8" + "⬛".repeat(Math.max(0, barrasVazias)) +
                " §7" + (int) percentual + "%";
    }

    public static String format(double amount) {
        if (amount < 1000) {
            return String.format("%.0f", amount);
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
