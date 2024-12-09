package org.gustaav.zoneMines.commands;

import org.bukkit.command.CommandSender;
import org.gustaav.zoneMines.managers.classic.LapisManager;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class MineCommand {
    LapisManager lapisManager;

    public MineCommand(LapisManager lapisManager) {
        this.lapisManager = lapisManager;
    }

    @CommandPermission("zonemines.admin")
    @Command("mine reset <mine>")
    public void mineReset(CommandSender sender, @Named("mine") String mine) {
        if(mine.equalsIgnoreCase("classica")) {
            lapisManager.reset();
            sender.sendMessage("§aMina clássica resetada com sucesso.");
        } else {
            sender.sendMessage("§cMina inválida.");
        }
    }

}
