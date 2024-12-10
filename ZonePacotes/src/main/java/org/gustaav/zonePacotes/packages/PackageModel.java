package org.gustaav.zonePacotes.packages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class PackageModel {
    String id;
    String displayName;
    List<String> lore;
    String colorCode;
    String head;
    List<RewardModel> rewards;

}
