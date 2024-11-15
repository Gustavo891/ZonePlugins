package org.gustaav.zoneEssential.kits.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.commands.Command;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;

import java.util.ArrayList;
import java.util.List;

public class CriarKitCommand extends Command {

    private final KitManager kitManager;

    public CriarKitCommand(ZoneEssential plugin) {
        super(
                "criarkit",
                "Crie um kit no servidor",
                "Use /criarkit {tipo} {tempo em segundos}",
                new String[]{},
                "zoneessential.criarkit");
        this.kitManager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar esse comando.");
            return;
        }

        if (args.length != 2) {
            player.sendMessage("§cUso correto: /criarkit {tipo} {tempo em segundos}");
            return;
        }

        String tipo = args[0];
        int delay;

        try {
            delay = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cO tempo deve ser um número válido.");
            return;
        }

        // Pegando itens do inventário do jogador
        ItemStack[] inventoryItems = player.getInventory().getContents();
        List<ItemStack> itemList = new ArrayList<>();

        for (ItemStack item : inventoryItems) {
            if (item != null) {
                itemList.add(item.clone());
            }
        }

        if (itemList.isEmpty()) {
            player.sendMessage("§cVocê precisa ter itens no inventário para criar o kit.");
            return;
        }

        // Criando e salvando o kit
        KitModel newKit = new KitModel(tipo, tipo, delay, itemList);
        kitManager.addKit(newKit);

        player.sendMessage("§aKit " + tipo + " criado com sucesso!");
    }
}
