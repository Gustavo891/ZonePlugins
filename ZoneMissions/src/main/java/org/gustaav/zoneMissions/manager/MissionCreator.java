package org.gustaav.zoneMissions.manager;

import lombok.Getter;
import org.bukkit.Material;
import org.gustaav.zoneMissions.models.MissionModel;
import org.gustaav.zoneMissions.models.RewardModel;
import org.gustaav.zoneMissions.models.TaskModel;
import org.gustaav.zoneMissions.models.TaskTypes;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MissionCreator {

    List<MissionModel> missions = new ArrayList<MissionModel>();

    MissionModel mission01 = new MissionModel(
            "Minerador Iniciante",
            "§7Quebre os minérios necessários para\n§7conseguir suas primeiras moedas.",
            "mission01",
            List.of(
                    new TaskModel(
                            "Lapis01",
                            "Minérios de Lapis",
                            2000,
                            TaskTypes.BREAK_BLOCKS,
                            null,
                            Material.LAPIS_ORE
                    ),
                    new TaskModel(
                            "Diamante01",
                            "Minérios de Diamante",
                            100,
                            TaskTypes.BREAK_BLOCKS,
                            null,
                            Material.DIAMOND_ORE
                    )
            ),
            List.of(
                    new RewardModel("§2$§F30.000", "money add %player% 30000 -c"),
                    new RewardModel("§fEficiência 5", "givebook %player% eficiencia 5 -c")
            )
    );

    MissionModel mission02 = new MissionModel(
            "Minerador Afortunado",
            "§7Já que conseguiu suas primeiras ferramentas\n§7utilize elas para conseguir mais dinheiro.",
            "mission02",
            List.of(
                    new TaskModel(
                            "Lapis02",
                            "Minérios de Lapis",
                            5000,
                            TaskTypes.BREAK_BLOCKS,
                            null,
                            Material.LAPIS_ORE
                    )
            ),
            List.of(
                    new RewardModel("§2$§F100.000", "money add %player% 100000 -c"),
                    new RewardModel("§fEficiência 6", "givebook %player% eficiencia 6 -c"),
                    new RewardModel("§fFortuna 3", "givebook %player% fortuna 3 -c")
            )
    );

    MissionModel mission03 = new MissionModel(
            "Fazendeiro Aprendiz",
            "§7Começe a plantar suas primeiras\n§7sementes em sua farm.",
            "mission03",
            List.of(
                    new TaskModel(
                            "melancia01",
                            "Plante sementes de melancia.",
                            128,
                            TaskTypes.PLACE_BLOCKS,
                            null,
                            Material.MELON_STEM
                    )
            ),
            List.of(
                    new RewardModel("§2$§F100.000", "money add %player% 100000 -c")
            )
    );

    MissionModel mission04 = new MissionModel(
            "Primeira Colheita",
            "§7Venda os recursos que sua\n§7primeira plantação rendeu.",
            "mission04",
            List.of(
                    new TaskModel(
                            "melancia01",
                            "Venda suas melancias.",
                            512,
                            TaskTypes.ARMAZEM_SELL,
                            null,
                            Material.MELON_SLICE
                    )
            ),
            List.of(
                    new RewardModel("§2$§F250.000", "money add %player% 250000 -c")
            )
    );

    public MissionCreator() {
        missions.add(mission01);
        missions.add(mission02);
        missions.add(mission03);
        missions.add(mission04);
    }


    public static String criarBarraProgresso(int progressoAtual, int progressoMaximo) {
        int progressoCheio = (int) ((double) progressoAtual / progressoMaximo * 10);
        StringBuilder barra = new StringBuilder();

        double progresso = ((double) progressoAtual /progressoMaximo) *100;

        for (int i = 0; i < 10; i++) {
            if (i < progressoCheio) {
                barra.append("♦"); // Parte preenchida
            } else {
                barra.append("§8♦"); // Parte vazia
            }
        }

        return barra + " §7" + (int) progresso + "%";
    }

}
