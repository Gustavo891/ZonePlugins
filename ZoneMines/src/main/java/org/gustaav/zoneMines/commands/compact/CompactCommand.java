package org.gustaav.zoneMines.commands.compact;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.CompactModel;
import revxrsal.commands.annotation.Command;

import java.util.*;
import java.util.stream.Collectors;

public class CompactCommand {

    private final List<UUID> compactList = new ArrayList<UUID>();

    private final List<CompactModel> list = Arrays.asList(
            new CompactModel(new ItemStack(Material.LAPIS_LAZULI), 9, new ItemStack(Material.LAPIS_BLOCK)),
            new CompactModel(new ItemStack(Material.DIAMOND), 9, new ItemStack(Material.DIAMOND_BLOCK))
    );

    public CompactCommand(ZoneMines plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : compactList) {
                    Player player = Bukkit.getPlayer(uuid);
                    assert player != null;
                    compactPlayer(player);
                }
            }
        }.runTaskTimer(plugin, 0, 100L);
    }

    @Command({"autocompact", "autocompactar"})
    public void autoCompact(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }
        if(!player.hasPermission("zonemines.autocompactar")) {
            player.sendMessage("§cSem permissão.");
        }

        if(compactList.contains(player.getUniqueId())) {
            player.sendMessage("§cVocê desativou o modo auto-compactar.");
            compactList.remove(player.getUniqueId());
        } else {
            compactList.add(player.getUniqueId());
            player.sendMessage("§aVocê ativou o modo auto-compactar.");
        }

    }

    @Command({"compactar", "compact"})
    public void compact(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return;
        }
        if(!player.hasPermission("zonemines.compactar")) {
            player.sendMessage("§cSem permissão.");
        }
        compactPlayer(player);
    }

    private void compactPlayer(Player player) {
        int compactable = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                for (CompactModel model : list) {
                    if (isItemMatch(item, model.getBase())) {
                        if(compactItem(player, item, model)){
                            compactable++;
                        }
                    }
                }
            }
        }
        if(compactable>0) {
            player.sendMessage("§aItens compactados com sucesso!");
        }
    }

    private boolean isItemMatch(ItemStack item, ItemStack base) {
        return item.getType() == base.getType() && item.getDurability() == base.getDurability();
    }

    private boolean compactItem(Player player, ItemStack item, CompactModel model) {
        if (item.getAmount() >= model.getRecipe()) {
            int compactedAmount = item.getAmount() / model.getRecipe();
            int remainingAmount = item.getAmount() % model.getRecipe();

            ItemStack compactedItem = model.getResult();
            compactedItem.setAmount(compactedAmount);

            player.getInventory().removeItem(item);
            player.getInventory().addItem(compactedItem);

            if (remainingAmount > 0) {
                item.setAmount(remainingAmount);
                player.getInventory().addItem(item);
                return true;
            }
        }
        return false;
    }
}
