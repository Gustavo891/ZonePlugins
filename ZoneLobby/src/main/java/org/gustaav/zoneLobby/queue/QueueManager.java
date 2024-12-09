package org.gustaav.zoneLobby.queue;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.model.ServerModel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class QueueManager {

    private final ZoneLobby plugin;

    // Mapa para gerenciar as filas de jogadores por servidor
    @Getter
    private final Map<String, Queue<Player>> serverQueues = new HashMap<>();

    // Constante para o tempo estimado por jogador em segundos
    private static final int TIME_PER_PLAYER_SECONDS = 5;

    public QueueManager(ZoneLobby plugin) {
        this.plugin = plugin;
        initializeQueues();

        // Atualiza status dos servidores e processa as filas a cada segundo
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            plugin.getServerCache().updateServerStatuses();
            processAllQueues();
        }, TIME_PER_PLAYER_SECONDS * 20L, TIME_PER_PLAYER_SECONDS * 20L);
    }

    /**
     * Inicializa as filas para todos os servidores configurados
     */
    private void initializeQueues() {
        plugin.getServerCache().getServerCache().forEach((serverKey, serverData) -> {
            serverQueues.put(serverKey, new LinkedList<>());
        });
    }

    /**
     * Adiciona um jogador à fila de um servidor específico
     */
    public void addToQueue(String serverKey, Player player) {
        Queue<Player> queue = serverQueues.get(serverKey);
        if (queue == null) {
            player.sendMessage("Servidor não encontrado!");
            return;
        }
        if(queue.contains(player)) {
            player.sendMessage("§cVocê já está na fila.");
            return;
        }
        queue.add(player);
        int position = queue.size(); // A posição é o tamanho da fila após adicionar o jogador
        long estimatedTime = calculateEstimatedTime(serverKey, position);

        String message = String.format("\n  §7Entrou na fila do servidor: §f%s\n  §e§l#%d §8- §fTempo estimado: §b%s\n§r",
                plugin.getServerCache().getServer(serverKey).getDisplayName(), position, formatTime(estimatedTime));

        player.sendMessage(message);
    }

    /**
     * Remove um jogador da fila de um servidor específico
     */
    public void removeFromQueue(String serverKey, Player player) {
        Queue<Player> queue = serverQueues.get(serverKey);
        if (queue == null) {
            player.sendMessage("§cServidor não encontrado!");
            return;
        }
        queue.remove(player);
    }

    /**
     * Exibe o status da fila de um servidor para o jogador
     */
    public void queueStatus(String serverKey, Player player, ServerModel serverModel) {
        Queue<Player> queue = serverQueues.get(serverKey);
        if (queue == null) {
            player.sendMessage("§cServidor não encontrado!");
            return;
        }

        player.sendMessage("");
        player.sendMessage("  §fFila do servidor: §7" + serverModel.getDisplayName());
        player.sendMessage("  ");
        player.sendMessage("  §fQuantidade: §b" + queue.size());
        player.sendMessage("  §fStatus: §aAtiva");
        player.sendMessage("  ");

        // Verifica o status do servidor e exibe mensagens apropriadas
        if (!serverModel.isOnline()) {
            player.sendMessage("  §cO servidor está offline, fila parada.");
        } else if (serverModel.isMaintenance()) {
            player.sendMessage("  §cO servidor está em manutenção.");
        } else if (!serverModel.isDeployment()) {
            player.sendMessage("  §cO servidor está em desenvolvimento.");
        }

        player.sendMessage("§r");
    }

    /**
     * Calcula o tempo estimado para um jogador baseado na posição na fila
     */
    private long calculateEstimatedTime(String serverKey, int position) {
        return (long) position * TIME_PER_PLAYER_SECONDS;
    }

    /**
     * Formata o tempo estimado em minutos e segundos
     */
    private String formatTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        return String.format("%d min %d seg", minutes, seconds);
    }

    /**
     * Atualiza as posições de todos os jogadores na fila e envia as informações para cada um
     */
    private void updateQueuePositions(String serverKey) {
        Queue<Player> queue = serverQueues.get(serverKey);
        if (queue == null) return;

        int position = 1;
        for (Player player : queue) {
            long estimatedTime = calculateEstimatedTime(serverKey, position);
            player.sendMessage("Sua nova posição na fila: " + position);
            player.sendMessage("Tempo estimado para entrar: " + formatTime(estimatedTime));
            position++;

            String message = String.format("\n  §7Fila do §f%s\n\n  §fPosição: §7%d\n  §fTempo estimado: §b%s\n§r",
                    plugin.getServerCache().getServer(serverKey).getDisplayName(), position, formatTime(estimatedTime));

            player.sendMessage(message);
        }
    }

    /**
     * Processa a fila de um servidor e envia o jogador para o servidor se possível
     */
    private void processQueue(String serverKey) {
        Queue<Player> queue = serverQueues.get(serverKey);
        if (queue == null || queue.isEmpty()) return;

        ServerModel serverData = plugin.getServerCache().getServer(serverKey);
        if (serverData == null || serverData.isMaintenance() || serverData.isDeployment() || !serverData.isOnline()) {
            return;
        }

        Player player = queue.peek();
        if (player != null) {
            if (sendPlayerToServer(player, serverData.getProxyName())) {
                queue.poll(); // Remove o jogador da fila
            }
            updateQueuePositions(serverKey); // Atualiza as posições na fila
        }
    }

    /**
     * Processa todas as filas de servidores
     */
    public void processAllQueues() {
        serverQueues.keySet().forEach(this::processQueue);
    }

    /**
     * Envia um jogador para o servidor especificado via BungeeCord
     */
    public boolean sendPlayerToServer(Player player, String serverName) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(plugin, "BungeeCord", byteArray.toByteArray());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
