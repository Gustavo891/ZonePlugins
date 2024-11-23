package org.gustaav.zoneEssential.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

@Command("gamemode")
@CommandPermission("zoneessential.gamemode")
public class GamemodeCommand {

    @Command({"gamemode <modo>", "gm <modo>"})
    public void gamemode(CommandSender sender, @Suggest({"sobrevivencia", "criativo", "aventura", "espectador", "0", "1", "2", "3", "survival", "creative", "adventure", "spectator"}) String modo) {
        if(!(sender instanceof Player player)) {return;}

        if(modo.equalsIgnoreCase("survival") || modo.equalsIgnoreCase("sobrevivencia") || modo.equalsIgnoreCase("0")) {
            if(player.getGameMode() == GameMode.SURVIVAL) {
                sendFormattedMessage(player, "${Colors.RED}Você já está nesse modo de jogo.");
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo alterado para '${Colors.WHITE}SOBREVIVÊNCIA${Colors.YELLOW}'.");
            }
        } else if(modo.equalsIgnoreCase("creative") || modo.equalsIgnoreCase("criativo") || modo.equalsIgnoreCase("1")) {
            if(player.getGameMode() == GameMode.CREATIVE) {
                sendFormattedMessage(player, "${Colors.RED}Você já está nesse modo de jogo.");
            } else {
                player.setGameMode(GameMode.CREATIVE);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo alterado para '${Colors.WHITE}CRIATIVO${Colors.YELLOW}'.");
            }
        } else if(modo.equalsIgnoreCase("adventure") || modo.equalsIgnoreCase("aventura") || modo.equalsIgnoreCase("2")) {
            if(player.getGameMode() == GameMode.ADVENTURE) {
                sendFormattedMessage(player, "${Colors.RED}Você já está nesse modo de jogo.");
            } else {
                player.setGameMode(GameMode.ADVENTURE);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo alterado para '${Colors.WHITE}AVENTURA${Colors.YELLOW}'.");
            }
        } else if(modo.equalsIgnoreCase("spectator") || modo.equalsIgnoreCase("espectador") || modo.equalsIgnoreCase("3")) {
            if(player.getGameMode() == GameMode.SPECTATOR) {
                sendFormattedMessage(player, "${Colors.RED}Você já está nesse modo de jogo.");
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo alterado para '${Colors.WHITE}ESPECTADOR${Colors.YELLOW}'.");
            }
        } else {
            sendFormattedMessage(player, "${Colors.RED}Modo de jogo inexistente.");
        }
        
    }

    @Command({"gamemode <modo> <jogador>", "gm <modo> <jogador>"})
    public void gamemodePlayer(CommandSender sender, @Suggest({"sobrevivencia", "criativo", "aventura", "espectador", "0", "1", "2", "3", "survival", "creative", "adventure", "spectator"}) String modo, @Named("jogador") Player target) {
        if(!(sender instanceof Player player)) {return;}
        if(target == null || !target.isOnline()) {
            sendFormattedMessage(player, "${Colors.RED}Jogador não encontrado.");
            return;
        }
        if(modo.equalsIgnoreCase("survival") || modo.equalsIgnoreCase("sobrevivencia") || modo.equalsIgnoreCase("0")) {
            if(target.getGameMode() == GameMode.SURVIVAL) {
                sendFormattedMessage(player, "${Colors.RED}O jogador já está nesse modo de jogo.");
            } else {
                target.setGameMode(GameMode.SURVIVAL);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo de ${Colors.WHITE}" + target.getName() + " ${Colors.YELLOW}alterado para '${Colors.WHITE}SOBREVIVÊNCIA${Colors.YELLOW}'");
            }
        } else if(modo.equalsIgnoreCase("creative") || modo.equalsIgnoreCase("criativo") || modo.equalsIgnoreCase("1")) {
            if(target.getGameMode() == GameMode.CREATIVE) {
                sendFormattedMessage(player, "${Colors.RED}O jogador já está nesse modo de jogo.");
            } else {
                target.setGameMode(GameMode.CREATIVE);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo de ${Colors.WHITE}" + target.getName() + " ${Colors.YELLOW}alterado para '${Colors.WHITE}CRIATIVO${Colors.YELLOW}'");
            }
        } else if(modo.equalsIgnoreCase("adventure") || modo.equalsIgnoreCase("aventura") || modo.equalsIgnoreCase("2")) {
            if(target.getGameMode() == GameMode.ADVENTURE) {
                sendFormattedMessage(player, "${Colors.RED}O jogador já está nesse modo de jogo.");
            } else {
                target.setGameMode(GameMode.ADVENTURE);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo de ${Colors.WHITE}" + target.getName() + " ${Colors.YELLOW}alterado para '${Colors.WHITE}AVENTURA${Colors.YELLOW}'");
            }
        } else if(modo.equalsIgnoreCase("spectator") || modo.equalsIgnoreCase("espectador") || modo.equalsIgnoreCase("3")) {
            if(target.getGameMode() == GameMode.SPECTATOR) {
                sendFormattedMessage(player, "${Colors.RED}O jogador já está nesse modo de jogo.");
            } else {
                target.setGameMode(GameMode.SPECTATOR);
                sendFormattedMessage(player, "${Colors.YELLOW}Modo de jogo de ${Colors.WHITE}" + target.getName() + " ${Colors.YELLOW}alterado para '${Colors.WHITE}ESPECTADOR${Colors.YELLOW}'");
            }
        } else {
            sendFormattedMessage(player, "${Colors.RED}Modo de jogo inexistente.");
        }
    }


}
