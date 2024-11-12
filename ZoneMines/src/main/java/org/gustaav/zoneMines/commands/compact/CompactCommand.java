package org.gustaav.zoneMines.commands.compact;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneMines.commands.Command;

import java.util.Arrays;
import java.util.List;

public class CompactCommand extends Command {

    // Lista de compactações disponíveis
    private final List<CompactModel> list = Arrays.asList(
            new CompactModel(new ItemStack(Material.LAPIS_LAZULI), 9, new ItemStack(Material.LAPIS_BLOCK)),
            new CompactModel(new ItemStack(Material.DIAMOND), 9, new ItemStack(Material.DIAMOND_BLOCK))
    );

    public CompactCommand() {
        super(
                "compactar",
                "Compacte os itens de seu inventário",
                "§cUse /compactar",
                new String[]{"compact"},
                "zonemines.compactar"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }

        // Percorre o inventário do jogador
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) { // Verifica se o item não é nulo
                for (CompactModel model : list) {
                    // Verifica se o item no inventário corresponde ao item base do modelo
                    if (isItemMatch(item, model.getBase())) {
                        compactItem(player, item, model);
                    }
                }
            }
        }

        player.sendMessage("§aItens compactados com sucesso!");
    }

    // Método que verifica se o item no inventário corresponde ao item base
    private boolean isItemMatch(ItemStack item, ItemStack base) {
        return item.getType() == base.getType() && item.getDurability() == base.getDurability();
    }

    // Método que realiza a compactação do item
    private void compactItem(Player player, ItemStack item, CompactModel model) {
        // Verifica se o item é de um tipo que pode ser compactado
        if (item.getAmount() >= model.getRecipe()) {
            int compactedAmount = item.getAmount() / model.getRecipe(); // Quantidade de itens compactados
            int remainingAmount = item.getAmount() % model.getRecipe(); // Quantidade restante do item

            // Cria o item compactado
            ItemStack compactedItem = model.getResult();
            compactedItem.setAmount(compactedAmount);

            // Remove os itens originais e adiciona os itens compactados
            player.getInventory().removeItem(item);
            player.getInventory().addItem(compactedItem);

            // Se ainda sobrou algum item, adiciona de volta ao inventário
            if (remainingAmount > 0) {
                item.setAmount(remainingAmount);
                player.getInventory().addItem(item);
            }
        }
    }
}
