package org.gustaav.zoneMissions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@AllArgsConstructor @Getter @Setter
@ToString
public class PlayerModel {

    UUID uuid;
    MissionModel mission;
    Map<TaskModel, Integer> taskProgress = new HashMap<TaskModel, Integer>();
}
