package org.gustaav.zoneMissions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class MissionModel {

    String name;
    String description;
    String id;
    List<TaskModel> tasks;
    List<RewardModel> rewards;

}
