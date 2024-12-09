package org.gustaav.zoneEnchants.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEnchants.enchantment.CustomEnchants.CustomManager;
import org.gustaav.zoneEnchants.enchantment.EnchantConfig;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;

public class GiveBookCommand {

    EnchantConfig enchantConfig;
    CustomManager customManager;
    public GiveBookCommand(CustomManager customManager, EnchantConfig enchantConfig) {
        this.customManager = customManager;
        this.enchantConfig = enchantConfig;
    }

    @Command("givebook <player> <tipo> <nivel> <flag>")
    public void giveBook(CommandSender sender,
                         @Named("player") Player player,
                         @Suggest({"explosivo", "eficiencia", "afiada", "protecao", "fortuna", "toque_suave", "Durabilidade", "aspecto_flamejante"}) @Named("tipo") String tipo,
                         @Named("nivel") int nivel,
                         @Optional @Named("flag") String flag) {

        ItemStack livro = enchantConfig.giveBook(tipo, nivel);
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
