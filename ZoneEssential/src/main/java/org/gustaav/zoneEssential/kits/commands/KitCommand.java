package org.gustaav.zoneEssential.kits.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.commands.Command;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;

import java.util.List;

public class KitCommand extends Command {

    private final KitManager kitManager;

    public KitCommand(ZoneEssential plugin) {
        super(
                "kit",
                "Pegue um kit do servidor",
                "Use /kit {tipo}",
                new String[]{},
                "zoneessential.kit"
        );
        this.kitManager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar esse comando.");
            return;
        }

        if (args.length != 1) {
            player.sendMessage("§cUso correto: /kit {tipo}");
            return;
        }

        String tipo = args[0];

        // Verificando se o jogador tem permissão para pegar o kit
        if (!player.hasPermission("zoneessential.kit." + tipo)) {
            player.sendMessage("§cVocê não tem permissão para pegar o kit " + tipo + ".");
            return;
        }

        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                selectedKit = kit;
                break;
            }
        }

        if (selectedKit == null) {
            player.sendMessage("§cO kit " + tipo + " não existe.");
            return;
        }

        long lastUsed = kitManager.getKitCooldown(player.getUniqueId(), selectedKit.getKitType());
        long cooldownTime = selectedKit.getDelay() * 1000L; // Delay em milissegundos
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUsed < cooldownTime) {
            long remainingTime = cooldownTime - (currentTime - lastUsed);
            String formattedTime = kitManager.formatTime(remainingTime);
            player.sendMessage("§cVocê precisa esperar " + formattedTime + " para pegar esse kit novamente.");
            return;
        }

        // Dando os itens do kit ao jogador
        List<ItemStack> items = selectedKit.getListaItens();
        for (ItemStack item : items) {
            player.getInventory().addItem(item.clone());
        }

        // Atualizando o cooldown do kit para o jogador
        kitManager.setKitCooldown(player.getUniqueId(), selectedKit.getKitType(), currentTime);

        player.sendMessage("§eVocê recebeu o kit '§f" + selectedKit.getKitName() + "§f'.");
    }
}
