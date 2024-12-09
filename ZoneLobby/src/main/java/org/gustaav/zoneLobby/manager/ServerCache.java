package org.gustaav.zoneLobby.manager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.gustaav.zoneLobby.ZoneLobby;
import org.gustaav.zoneLobby.model.ServerModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ServerCache {

    private Map<String, ServerModel> serverCache = new HashMap<>();

    public ServerCache(ZoneLobby plugin) {
        serverCache = plugin.getConfigManager().loadServersFromConfig();

    }

    public ServerModel getServer(String serverKey) {
        return serverCache.get(serverKey);
    }

    public void updateServerStatuses() {
        for (Map.Entry<String, ServerModel> entry : serverCache.entrySet()) {
            String serverKey = entry.getKey();
            ServerModel serverData = entry.getValue();

            boolean online = isServerOnline(serverData.getIp(), serverData.getPort());
            serverData.setOnline(online);

            if (!online) {
                Bukkit.getLogger().warning("Servidor " + serverKey + " (" + serverData.getDisplayName() + ") está offline!");
            }
        }
    }

    private boolean isServerOnline(String ip, int port) {
        if(ip == null ) {
            Bukkit.getLogger().warning("IP INVALIDO");
            return false;
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), 2000); // Timeout de 2 segundos
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
