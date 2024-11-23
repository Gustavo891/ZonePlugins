package org.gustaav.zoneEssential.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.gustaav.zoneEssential.ZoneEssential;
import org.gustaav.zoneEssential.kits.KitManager;
import org.gustaav.zoneEssential.kits.KitModel;
import org.gustaav.zoneEssential.gui.kits.KitsGUI;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.gustaav.zoneEssential.manager.utils.formatTime;
import static org.gustaav.zoneEssential.utils.MessageUtil.sendFormattedMessage;

public class KitCommand {

    KitManager kitManager;
    ZoneEssential plugin;

    public KitCommand(KitManager manager, ZoneEssential plugin) {
        this.kitManager = manager;
        this.plugin = plugin;
    }

    @Command("kit <tipo>")
    public void getKit(CommandSender sender, String tipo) {
        if(!(sender instanceof Player player)) {
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
            sendFormattedMessage(player, String.format("${Colors.RED}O kit %s não existe.", tipo));
            return;
        }
        if(!player.hasPermission("zoneEssential.kit." + selectedKit.getKitType())) {
            sendFormattedMessage(player, "${Colors.RED}Sem permissão para coletar esse kit.");
        }

        long currentTime = System.currentTimeMillis();
        if(!kitManager.getKitCooldown(player.getUniqueId(), selectedKit, true)) {
            return;
        }
        List<ItemStack> items = selectedKit.getListaItens();
        for (ItemStack item : items) {
            player.getInventory().addItem(item.clone());
        }

        kitManager.setKitCooldown(player.getUniqueId(), selectedKit.getKitType(), currentTime); // "${Colors.YELLOW}Você recebeu o kit '${Colors.WHITE}" + selectedKit.getKitName() + "${Colors.YELLOW}'."
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Você recebeu o kit '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}'.", selectedKit.getKitName()));

    }

    @Command("listkits")
    public void getKits(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }
        player.sendMessage("§r");
        sendFormattedMessage(player, "  ${Colors.PURPLE_LIGHT}Lista de kits disponíveis:");
        boolean tem = false;
        for(KitModel kit : kitManager.getKits()) {
            if(player.hasPermission("zoneessential.kit." + kit.getKitType().toLowerCase())) {
                sendFormattedMessage(player, String.format("${Colors.GRAY}  → ${Colors.WHITE}%s ${Colors.GRAY}[%s]", kit.getKitName(), kit.getKitType()));
                tem = true;
            }
        }
        if(!tem) {
            sendFormattedMessage(player, "  ${Colors.RED}Não possui acesso a nenhum kit.");
        }
        player.sendMessage("§r");
    }

    @Command("kits")
    public void getKitsMenu(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            return;
        }
        KitsGUI kitsGUI = new KitsGUI(plugin, player);
    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit <tipo> <jogador>")
    public void giveKit(CommandSender sender, String tipo, Player jogador) {
        if(!(sender instanceof Player player)) {
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
            sendFormattedMessage(player, String.format("${Colors.RED}O kit '${Colors.WHITE}%s${Colors.RED}' não existe", tipo));
            return;
        }
        if(jogador == null || !jogador.isOnline()) {
            sendFormattedMessage(player, "${Colors.RED}Jogador não encontrado.");
            return;
        }
        List<ItemStack> items = selectedKit.getListaItens();
        for (ItemStack item : items) {
            jogador.getInventory().addItem(item.clone());
        }
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Você enviou o kit '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}' para o jogador ${Colors.WHITE}%s${Colors.PURPLE_LIGHT}.", selectedKit.getKitName(), jogador.getName()));

    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit criar <tipo> <delay>")
    public void criarKit(CommandSender sender, String tipo, int delay) {
        if(!(sender instanceof Player player)) {return;}

        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                selectedKit = kit;
                break;
            }
        }
        if (selectedKit != null) {
            sendFormattedMessage(player, String.format("${Colors.RED}O kit '${Colors.WHITE}%s${Colors.RED}' já existe", tipo));
            return;
        }
        ItemStack[] inventoryItems = player.getInventory().getContents();
        List<ItemStack> itemList = new ArrayList<>();

        for (ItemStack item : inventoryItems) {
            if (item != null) {
                itemList.add(item.clone());
            }
        }
        if (itemList.isEmpty()) {
            sendFormattedMessage(player, "${Colors.RED}Você precisa ter itens no inventário para criar um kit.");
            return;
        }

        KitModel newKit = new KitModel(tipo, tipo, delay, itemList);
        kitManager.addKit(newKit);

        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}O kit '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}' foi criado com sucesso", tipo));

    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit remover <tipo>")
    public void removerKit(CommandSender sender, String tipo) {
        if(!(sender instanceof Player player)) {return;}
        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                kitManager.removeKit(kit);
                sendFormattedMessage(player, String.format("${Colors.RED}O kit '${Colors.WHITE}%s${Colors.RED}' foi removido", tipo));
                return;
            }
        }
        sendFormattedMessage(player, "${Colors.RED}O kit não foi encontrado.");

    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit editar <tipo> tempo <segundos>")
    public void editKit(CommandSender sender, String tipo, int segundos) {
        if(!(sender instanceof Player player)) {return;}
        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                selectedKit = kit;
            }
        }
        if (selectedKit == null) {
            sendFormattedMessage(player, "${Colors.RED}O kit não foi encontrado.");
            return;
        }
        selectedKit.setDelay(segundos);
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Tempo de cooldown definido para '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}'.", formatTime(segundos)));
    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit editar <tipo> itens")
    public void editKit(CommandSender sender, String tipo) {
        if(!(sender instanceof Player player)) {return;}
        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                selectedKit = kit;
            }
        }
        if (selectedKit == null) {
            sendFormattedMessage(player, "${Colors.RED}O kit não foi encontrado.");
            return;
        }
        ItemStack[] itens = player.getInventory().getContents();

        List<ItemStack> itensLista = new ArrayList<>();
        for (ItemStack item : itens) {
            if (item != null) {
                itensLista.add(item.clone());
            }
        }
        selectedKit.setListaItens(itensLista);
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Itens do kit '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}' atualizados com sucesso.", selectedKit.getKitName()));
    }

    @CommandPermission("zoneessential.kits.admin")
    @Command("kit editar <tipo> nome <nome>")
    public void editKit(CommandSender sender, String tipo, String nome) {
        if(!(sender instanceof Player player)) {return;}
        List<KitModel> kits = kitManager.getKits();
        KitModel selectedKit = null;
        for (KitModel kit : kits) {
            if (kit.getKitType().equalsIgnoreCase(tipo)) {
                selectedKit = kit;
            }
        }
        if (selectedKit == null) {
            sendFormattedMessage(player, "${Colors.RED}O kit não foi encontrado.");
            return;
        }

        selectedKit.setKitName(nome);
        sendFormattedMessage(player, String.format("${Colors.PURPLE_LIGHT}Nome do kit atualizado para: '${Colors.WHITE}%s${Colors.PURPLE_LIGHT}'.", nome));
    }


}
