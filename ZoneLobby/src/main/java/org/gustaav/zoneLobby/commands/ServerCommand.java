package org.gustaav.zoneLobby.commands;

import org.bukkit.command.CommandSender;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.manager.ServerCache;
import org.gustaav.zoneLobby.model.ServerModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class ServerCommand {

    ServerCache serverCache;

    public ServerCommand(ZoneLobby zoneLobby) {
        this.serverCache = zoneLobby.getServerCache();
    }

    @CommandPermission("zonelobby.admin")
    @Command("setmaintence <server> <status>")
    public void setMaintence(CommandSender sender, @Named("server") @Suggest("rankup") String server, @Named("status") boolean status) {

        ServerModel serverData = serverCache.getServer(server);
        if (serverData == null) {
            sender.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }

        if(serverData.isMaintenance()) {
            serverData.setMaintenance(false);
            sender.sendMessage("§cManutenção desativada para o servidor: " + serverData.getDisplayName());
        } else {
            serverData.setMaintenance(true);
            sender.sendMessage("§aManutenção ligada para o servidor: " + serverData.getDisplayName());
        }
    }

    @CommandPermission("zonelobby.admin")
    @Command("setdeployment <server> <status>")
    public void setDeployment(CommandSender sender, @Named("server") @Suggest("rankup") String server, @Named("status") boolean status) {

        ServerModel serverData = serverCache.getServer(server);
        if (serverData == null) {
            sender.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }

        if(serverData.isDeployment()) {
            serverData.setDeployment(false);
            sender.sendMessage("§aModo desenvolvimento ativado para o servidor: " + serverData.getDisplayName());
        } else {
            serverData.setDeployment(true);
            sender.sendMessage("§cModo desenvolvimento desativado para o servidor: " + serverData.getDisplayName());
        }
    }

    @CommandPermission("zonelobby.admin")
    @Command("serverinfo <server>")
    public void serverInfo(CommandSender sender, @Named("server") @Suggest("rankup") String server) {
        ServerModel serverData = serverCache.getServer(server);
        if (serverData == null) {
            sender.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }

        String message =
                String.format(
                        """
                 §r  §7Status do servidor: §f%s\n
                 §r  \n
                 §r  §fManutenção: §7%s\n
                 §r  §fDesenvolvimento: §7%s\n"""
                        , serverData.getDisplayName(), serverData.isMaintenance(), !serverData.isDeployment());
                
        sender.sendMessage(message);
    }

}
