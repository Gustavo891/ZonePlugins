package org.gustaav.zonePacotes.packages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class RewardDistributor {

    private final Random random = new Random();

    /**
     * Seleciona uma recompensa aleatória baseada no peso.
     *
     * @param rewards Lista de recompensas disponíveis.
     * @return A recompensa selecionada.
     */
    public RewardModel selectReward(List<RewardModel> rewards) {
        int totalWeight = rewards.stream().mapToInt(RewardModel::getWeight).sum();

        if (totalWeight == 0) {
            throw new IllegalStateException("Nenhuma recompensa válida encontrada com peso maior que 0.");
        }

        // Gera um número aleatório entre 0 (inclusive) e totalWeight (exclusivo)
        int randomWeight = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (RewardModel reward : rewards) {
            cumulativeWeight += reward.getWeight();
            if (randomWeight < cumulativeWeight) {
                return reward;
            }
        }

        throw new IllegalStateException("Erro ao selecionar recompensa. Verifique os pesos configurados.");
    }

    /**
     * Dá a recompensa ao jogador (Exemplo de uso).
     *
     * @param player O jogador que receberá a recompensa.
     * @param reward A recompensa selecionada.
     */

    public void giveReward(Player player, RewardModel reward) {
        // Executa o comando pelo console, substituindo %player% pelo nome do jogador
        if (reward.getCommand() != null && !reward.getCommand().isEmpty()) {
            String formattedCommand = reward.getCommand().replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
        }

        player.sendMessage("§aRecompensa entregue: §7" + reward.getDisplayName());
    }
}
