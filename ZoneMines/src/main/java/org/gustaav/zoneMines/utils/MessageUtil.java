package org.gustaav.zoneMines.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class MessageUtil {

    // Método para enviar mensagens formatadas com cores
    public static void sendFormattedMessage(Player player, String message) {
        // Substitui os placeholders de cores pelos valores das cores definidas na classe Colors
        for (Field field : Colors.class.getDeclaredFields()) {
            if (field.getType().equals(String.class)) {
                try {
                    String colorName = "${Colors." + field.getName() + "}";
                    String colorValue = (String) field.get(null);

                    message = message.replace(colorName, "<" + colorValue + ">");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        // Envia a mensagem para o jogador com a cor aplicada
        player.sendRichMessage(message);
    }

    public static String getFormattedMessage(String message) {
        for (Field field : Colors.class.getDeclaredFields()) {
            if (field.getType().equals(String.class)) {
                try {
                    String colorName = "${Colors." + field.getName() + "}";
                    String colorValue = (String) field.get(null);

                    message = message.replace(colorName, "<" + colorValue + ">");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return message;
    }
}
