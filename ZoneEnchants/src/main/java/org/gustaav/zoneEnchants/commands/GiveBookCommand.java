package org.gustaav.zoneEnchants.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.gustaav.zoneEnchants.enchantment.CustomEnchants.CustomManager;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import org.gustaav.zoneEnchants.enchantment.EnchantModel;
import org.gustaav.zoneEnchants.enchantment.EnchantTypes;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;

import java.util.ArrayList;
import java.util.List;

public class GiveBookCommand {

    CustomManager customManager;
    public GiveBookCommand(CustomManager customManager) {
        this.customManager = customManager;
    }

    @Command("givebook <player> <tipo> <nivel> <flag>")
    public void giveBook(CommandSender sender,
                         @Named("player") Player player,
                         @Suggest({"explosivo", "eficiencia", "afiada", "protecao", "fortuna", "toque_suave", "Durabilidade", "aspecto_flamejante"}) @Named("tipo") String tipo,
                         @Named("nivel") int nivel,
                         @Optional @Named("flag") String flag) {

        ItemStack livro = EnchantConfig.giveBook(tipo, nivel);
        boolean console = false;
        if(flag != null && flag.equalsIgnoreCase("-c")) {
            console = true;
        }

        if(livro == null) {
            sender.sendMessage("§cEncantamento não encontrado.");
            return;
        }

        player.getInventory().addItem(livro);
        if(console) {
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Livro entregue para " + player.getName() + "!");
    }


}
