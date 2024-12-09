package org.gustaav.zoneLobby.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerModel {
    private String displayName;
    private String proxyName;
    private String ip;
    private int port;
    private boolean maintenance;
    private boolean deployment;
    private boolean betaVip;
    private boolean online;

    public ServerModel(String displayName, String proxyName, String ip, int port,
                       boolean maintenance, boolean deployment, boolean betaVip, boolean online) {
        this.displayName = displayName;
        this.proxyName = proxyName;
        this.ip = ip;
        this.port = port;
        this.maintenance = maintenance;
        this.deployment = deployment;
        this.betaVip = betaVip;
        this.online = online;
    }
}
