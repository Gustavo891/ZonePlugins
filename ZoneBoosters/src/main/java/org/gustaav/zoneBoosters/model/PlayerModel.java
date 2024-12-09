package org.gustaav.zoneBoosters.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
public class PlayerModel {

    UUID playerId;
    BoosterTypes type;
    long duration;
    int multiplier;

}
