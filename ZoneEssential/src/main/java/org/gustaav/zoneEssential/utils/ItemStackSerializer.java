package org.gustaav.zoneEssential.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
public class ItemStackSerializer {

    public static String itemStackToString(ItemStack item) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {

            // Write the ItemStack to the output stream
            bukkitObjectOutputStream.writeObject(item);
            bukkitObjectOutputStream.flush();

            // Convert the byte array to a Base64 encoded string
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the error appropriately in your code
        }
    }

    public static ItemStack stringToItemStack(String itemString) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(itemString));
             BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {

            // Lê o objeto da stream (ItemStack)
            return (ItemStack) bukkitObjectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Lide com o erro apropriadamente no seu código
        }
    }

}
