package org.gustaav.zoneEconomy.model;

import java.util.UUID;

public class PlayerModel {

    UUID uuid;
    Double money;

    public PlayerModel(UUID uuid, double money) {
        this.uuid = uuid;
        this.money = money;

    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public Double getMoney() {
        return money;
    }
    public void setMoney(Double money) {
        this.money = money;
    }

}
