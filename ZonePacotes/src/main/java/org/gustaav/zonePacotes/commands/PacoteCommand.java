package org.gustaav.zonePacotes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zonePacotes.packages.PackageManager;
import org.gustaav.zonePacotes.packages.PackageModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class PacoteCommand {

    PackageManager packageManager;

    public PacoteCommand(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @CommandPermission("zonepacotes.verpacotes")
    @Command("verpacotes")
    public void verpacotes(CommandSender sender) {
        sender.sendMessage("§r");
        sender.sendMessage("§a  Lista de pacotes:");
        for(PackageModel model : packageManager.getPackages()) {
            sender.sendMessage("§8  - §f" + model.getId());
        }
        sender.sendMessage("§r");
    }

    @CommandPermission("zonepacotes.admin")
    @Command("pacote reload")
    public void pacoteReload(CommandSender sender) {
        packageManager.loadPackages();
        sender.sendMessage("§aPacotes carregados com sucesso.");
    }

    @CommandPermission("zonepacotes.admin")
    @Command("givepacote <player> <type> <quantia>")
    public void givepacote(CommandSender sender, @Named("player") Player target,@Named("type") String type, @Named("quantia") int quantia) {
        PackageModel model = packageManager.getPackage(type);
        if(model == null) {
            sender.sendMessage("§cPacote não encontrado");
            return;
        }
        if(quantia > 64 || quantia < 0) {
            sender.sendMessage("§cA quantia deve ser entre 0 e 64.");
        }
        ItemStack item = packageManager.givePackage(model);
        item.setAmount(quantia);
        target.getInventory().addItem(item);
        sender.sendMessage(String.format("§eO pacote §f%s §afoi entregue para o jogador §7%s§a.", model.getId(), target.getName()));
    }

}
