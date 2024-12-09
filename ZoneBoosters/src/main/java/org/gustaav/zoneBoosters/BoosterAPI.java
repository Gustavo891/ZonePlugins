package org.gustaav.zoneBoosters;

import lombok.Getter;
import org.gustaav.zoneBoosters.manager.BoosterManager;


public class BoosterAPI {

    BoosterManager boosterManager;
    private static BoosterAPI instance;

    public BoosterAPI(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
        instance = this;
    }
    public static BoosterAPI getInstance() {
        return instance;
    }

    public BoosterManager getBoosterManager() {
        return boosterManager;
    }
}
