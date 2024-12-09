package org.gustaav.zoneLobby.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.queue.QueueManager;
import org.gustaav.zoneLobby.model.ServerModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class QueueCommand {

    private final QueueManager queueManager;
    private final ZoneLobby plugin;

    public QueueCommand(ZoneLobby plugin) {
        this.plugin = plugin;
        this.queueManager = plugin.getQueueManager();
    }

    @Command("fila entrar <server>")
    public void joinQueue(CommandSender sender, @Named("server") @Suggest("rankup") String server) {

        if(!(sender instanceof Player player)) {
            return;
        }

        // Obter os dados do servidor do cache
        ServerModel serverData = plugin.getServerCache().getServer(server);
        if (serverData == null) {
            player.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }

        if (serverData.isDeployment()) {
            player.sendMessage("§cEste servidor está em desenvolvimento no momento!");
            return;
        }

        if (!serverData.isOnline()) {
            player.sendMessage("§cEste servidor está offline no momento!");
            return;
        }
        if(player.hasPermission("zonelobby.queue.jump")) {
            queueManager.sendPlayerToServer(player, "rankup");
        }

        queueManager.addToQueue(server, player);

    }


    @CommandPermission("zonelobby.queue")
    @Command("fila sair <server>")
    public void leaveQueue(CommandSender sender, @Named("server") @Suggest("rankup") String server) {
        if(!(sender instanceof Player player)) {
            return;
        }

        // Obter os dados do servidor do cache
        ServerModel serverData = plugin.getServerCache().getServer(server);
        if (serverData == null) {
            player.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }
        queueManager.removeFromQueue(server, player);
        player.sendMessage("§cVocê saiu da fila do " + serverData.getDisplayName() + ".");
    }

    @CommandPermission("zonelobby.queue.admin")
    @Command("fila status <server>")
    public void statusQueue(CommandSender sender, @Named("server") @Suggest("rankup") String server) {
        ServerModel serverData = plugin.getServerCache().getServer(server);
        if (serverData == null) {
            sender.sendMessage("§cServidor não encontrado ou não configurado!");
            return;
        }
        queueManager.queueStatus(server, (Player) sender, serverData);
    }

}
