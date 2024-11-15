package org.gustaav.zoneEssential.kits.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.commands.Command;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;

import java.util.List;

public class DarkitCommand extends Command {

    private final KitManager kitManager;

    public DarkitCommand(ZoneEssential plugin) {
        super(
                "darkit",
                "Pegue um kit do servidor",
                "Use /darkit <jogador> {tipo}",
                new String[]{},
                "zoneessential.darkit"
        );
        this.kitManager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar esse comando.");
            return;
        }

        if (args.length != 2) {
            player.sendMessage("§cUso correto: /darkit <player> {tipo}");
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage("§cJogador inválido.");
            return;
        }

        String tipo = args[1];

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

        // Dando os itens do kit ao jogador
        List<ItemStack> items = selectedKit.getListaItens();
        for (ItemStack item : items) {
            target.getInventory().addItem(item.clone());
        }

        player.sendMessage("§eVocê enviou o kit §f" + tipo + " §epara o jogador §f" + target.getName() + "§e.");
    }
}
