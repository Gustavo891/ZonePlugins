package org.gustaav.zoneBoosters.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneBoosters.BoosterAPI;
import org.gustaav.zoneBoosters.manager.BoosterManager;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class giveBooster {

    BoosterManager boosterManager;

    public giveBooster(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @CommandPermission("zonebooster.admin")
    @Command("giveestimulante <player> <type> <multiplier> <duration>")
    public void getBooster(CommandSender sender, @Named("player") Player target,@Named("type") BoosterTypes type,@Named("multiplier") int multiplier,@Named("duration") int duration) {
        if(!(sender instanceof Player player)) {
            return;
        }
        ItemStack booster = boosterManager.giveBooster(type, multiplier, duration);
        player.sendMessage(String.format("§aO estimulante §f'%s'§a foi entregue para o jogador §7%s§a.", type, target.getName()));
        target.getInventory().addItem(booster);
    }

    @CommandPermission("zonebooster.admin")
    @Command("resetestimulante <player> <type>")
    public void resetBooster(CommandSender sender, @Named("player") Player target, @Named("type") BoosterTypes type) {
        if(!(sender instanceof Player player)) {
            return;
        }
        PlayerModel booster = boosterManager.getTypeBooster(target.getUniqueId(), type);
        if(booster == null) {
            player.sendMessage("§cNenhum estimulante desse tipo ativo.");
        } else {
            boosterManager.removeBooster(booster);
            player.sendMessage("§aEstimulante removido com sucesso.");
        }

    }

    @CommandPermission("zonebooster.admin")
    @Command("checkestimulante <player> <type>")
    public void checkBooster(CommandSender sender, @Named("player") Player target, @Optional @Named("type") BoosterTypes type) {
        if(!(sender instanceof Player player)) {
            return;
        }
        if(type != null) {
            PlayerModel model = boosterManager.getTypeBooster(target.getUniqueId(), type);
            if(model != null) {
                player.sendMessage("§r");
                player.sendMessage("§a  Estimulante ativo de §f" + target.getName());
                player.sendMessage("§r");
                player.sendMessage("§f  Multiplicador: §7" + model.getMultiplier() + "x");
                player.sendMessage("§f  Duração: §b" + convertLongToHoursAndMinutes(model.getDuration()));
                player.sendMessage("§r");
            } else {
                player.sendMessage("§cEsse jogador não tem esse tipo de estimulante ativo.");
            }
        } else {
            player.sendMessage("§r\n  §aTipos de estimulantes ativo: §f" + target.getName() + "\n§r");
            boolean check = false;
            for(BoosterTypes boosterType : BoosterTypes.values()) {
                if(boosterManager.getTypeBooster(target.getUniqueId(), boosterType) != null) {
                    player.sendMessage("§r  §8⚑ §f" + boosterType.toString());
                    check = true;
                }
            }
            if(!check) {
                player.sendMessage("§r  §8⚑ §fNenhum.");
            }
            player.sendMessage("§r\n  §8Utilize /checkbooster <player> <tipo> para mais informações.\n§r");
        }
    }

    public String convertLongToHoursAndMinutes(long timeInMillis) {
        long totalSeconds = timeInMillis / 1000; // Converte milissegundos para segundos
        long totalMinutes = totalSeconds / 60;  // Converte segundos para minutos
        long hours = totalMinutes / 60;         // Converte minutos para horas
        long minutes = totalMinutes % 60;       // Obtém os minutos restantes

        return String.format("%02dh %02dm", hours, minutes); // Formata como HH:mm
    }
}
