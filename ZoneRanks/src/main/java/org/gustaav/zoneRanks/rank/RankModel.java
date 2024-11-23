package org.gustaav.zoneRanks.rank;

import java.util.List;

public record RankModel(String id, int nivel, String nome, String prefix, int custo, List<String> commands) {


}
