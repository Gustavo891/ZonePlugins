package org.gustaav.zoneEssential.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends Command{

    public GamemodeCommand() {
        super(
                "gm",
                "Trocar o modo de jogo",
                "Use /gamemode (modo de jogo).",
                new String[]{"gamemode", "modo"},
                "zoneessential.gamemode"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {return;}

        if(args.length == 0) {
            player.sendMessage("§eUse /gamemode (modo de jogo).");
        } else if (args.length == 1) {
            if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("sobrevivencia") || args[0].equalsIgnoreCase("0")) {
                if(player.getGameMode() == GameMode.SURVIVAL) {
                    player.sendMessage("§cVocê já está nesse modo de jogo.");
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage("§eModo de jogo alterado para '§fSOBREVIVÊNCIA§e'.");
                }
            } else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("criativo") || args[0].equalsIgnoreCase("1")) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    player.sendMessage("§cVocê já está nesse modo de jogo.");
                } else {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage("§eModo de jogo alterado para '§fCRIATIVO§e'.");
                }
            } else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("aventura") || args[0].equalsIgnoreCase("2")) {
                if(player.getGameMode() == GameMode.ADVENTURE) {
                    player.sendMessage("§cVocê já está nesse modo de jogo.");
                } else {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage("§eModo de jogo alterado para '§fAVENTURA§e'.");
                }
            } else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("espectador") || args[0].equalsIgnoreCase("3")) {
                if(player.getGameMode() == GameMode.SPECTATOR) {
                    player.sendMessage("§cVocê já está nesse modo de jogo.");
                } else {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage("§eModo de jogo alterado para '§fESPECTADOR§e'.");
                }
            } else {
                player.sendMessage("§cModo de jogo inexistente.");
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                player.sendMessage("§cJogador inválido.");
                return;
            }
            if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("sobrevivencia") || args[0].equalsIgnoreCase("0")) {
                if(target.getGameMode() == GameMode.SURVIVAL) {
                    player.sendMessage("§cO jogador já está nesse modo de jogo.");
                } else {
                    target.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage("§eModo de jogo de §f" + target.getName() + " §ealterado para '§fSOBREVIVÊNCIA§e'.");
                }
            } else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("criativo") || args[0].equalsIgnoreCase("1")) {
                if(target.getGameMode() == GameMode.CREATIVE) {
                    player.sendMessage("§cO jogador já está nesse modo de jogo.");
                } else {
                    target.setGameMode(GameMode.CREATIVE);
                    player.sendMessage("§eModo de jogo de §f" + target.getName() + " §ealterado para '§fCRIATIVO§e'.");
                }
            } else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("aventura") || args[0].equalsIgnoreCase("2")) {
                if(target.getGameMode() == GameMode.ADVENTURE) {
                    player.sendMessage("§cO jogador já está nesse modo de jogo.");
                } else {
                    target.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage("§eModo de jogo de§f " + target.getName() + " §ealterado para '§fAVENTURA§e'.");
                }
            } else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("espectador") || args[0].equalsIgnoreCase("3")) {
                if(target.getGameMode() == GameMode.SPECTATOR) {
                    player.sendMessage("§cO jogador já está nesse modo de jogo.");
                } else {
                    target.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage("§eModo de jogo de§f " + target.getName() + "§e alterado para '§fESPECTADOR§e'.");
                }
            } else {
                player.sendMessage("§cModo de jogo inexistente.");
            }

        } else {
            player.sendMessage("§cUse /gamemode [nick] (modo de jogo).");
        }

    }

}
