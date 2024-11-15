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

public class EditarKitCommand extends Command {

    private final KitManager kitManager;

    public EditarKitCommand(ZoneEssential plugin) {
        super(
                "editarkit",
                "Edite um kit no servidor",
                "Use /editarkit {nome do kit} {novo tipo} {novo tempo em segundos}",
                new String[]{},
                "zoneessential.editarkit");
        this.kitManager = plugin.getKitManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando pode ser executado apenas por jogadores.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUso correto: /editarkit nome|itens|tempo {kit} [novo valor]");
            return;
        }

        String subcommand = args[0].toLowerCase();
        String kitName = args[1];

        for (KitModel kit : kitManager.getKits()) {
            if (kit.getKitType().equalsIgnoreCase(kitName)) {
                switch (subcommand) {
                    case "nome":
                        if (args.length < 3) {
                            player.sendMessage("§cUso correto: /editarkit nome {kit} {novo nome}");
                            return;
                        }

                        // Junta todos os argumentos após args[2] em um único nome
                        StringBuilder novoNomeBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            novoNomeBuilder.append(args[i]).append(" ");
                        }
                        String novoNome = novoNomeBuilder.toString().trim(); // Remove espaço extra no final

                        kit.setKitName(novoNome);
                        player.sendMessage("§eNome do kit atualizado para: §f" + novoNome);
                        break;

                    case "itens":
                        ItemStack[] itens = player.getInventory().getContents();

                        // Cria uma lista mutável para permitir itens nulos
                        List<ItemStack> itensLista = new ArrayList<>();
                        for (ItemStack item : itens) {
                            if (item != null) {
                                itensLista.add(item.clone());
                            }
                        }
                        kit.setListaItens(itensLista);
                        player.sendMessage("Itens do kit '" + kitName + "' atualizados com os itens do seu inventário.");
                        break;

                    case "tempo":
                        if (args.length < 3) {
                            player.sendMessage("§cUso correto: /editarkit tempo {kit} {tempo em segundos}");
                            return;
                        }
                        int novoTempo;
                        try {
                            novoTempo = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cO tempo deve ser um número válido.");
                            return;
                        }
                        kit.setDelay(novoTempo);
                        player.sendMessage("§eTempo de cooldown do kit atualizado para: §f" + novoTempo + " §esegundos.");
                        break;

                    default:
                        player.sendMessage("§cSubcomando inválido. Use nome, itens ou tempo.");
                        break;
                }
                return;
            }
        }

        player.sendMessage("§cKit não encontrado");
    }
}
