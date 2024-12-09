package org.gustaav.zoneEconomy.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.gustaav.zoneEconomy.ZoneEconomy;
import org.gustaav.zoneEconomy.manager.EconomyManager;
import org.gustaav.zoneEconomy.model.PlayerModel;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static org.gustaav.zoneEconomy.utils.utils.format;
import static org.gustaav.zoneEconomy.utils.utils.mainColor;

@Command("money")
public class MoneyCommand {


    NamespacedKey key;
    EconomyManager economyManager;
    public MoneyCommand(EconomyManager economyManager, ZoneEconomy zoneEconomy) {
        this.economyManager = economyManager;}

    @Command("money")
    public void showBalance(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        double balance = economyManager.getBalance(player);
        sender.sendMessage(ChatColor.of(mainColor) + "Seu saldo: §2$§f" + format(balance));
    }

    @Command({"money ajuda"})
    public void help(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        sender.sendMessage("§r");
        sender.sendMessage(ChatColor.of(mainColor) + "  Comandos disponíveis:");
        sender.sendMessage("  §7/money §8- §fMostra seu saldo.");
        sender.sendMessage("  §7/money <jogador> §8- §fMostra o saldo de outro jogador.");
        sender.sendMessage("  §7/money enviar <jogador> <quantidade> §8- §fEnvia coins para outro jogador.");
        if (sender.hasPermission("zoneeconomy.money.admin")) {
            sender.sendMessage("  §c/money set <jogador> <quantidade> §8- §fDefine o saldo de um jogador.");
            sender.sendMessage("  §c/money remove <jogador> <quantidade> §8- §fRemove coins de um jogador.");
            sender.sendMessage("  §c/money add <jogador> <quantidade> §8- §fAdiciona coins a um jogador.");
        }
        sender.sendMessage("§r");
    }

    @Command("money <jogador>")
    public void showOtherBalance(CommandSender sender, @Named("jogador") Player target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return;
        }
        if(target == null || !target.isOnline()) {
            player.sendMessage("§cJogador não encontrado.");
            return;
        }
        double balance = economyManager.getBalance(target);
        sender.sendMessage(ChatColor.of(mainColor) + "Saldo de §f" + target.getName() + ChatColor.of(mainColor) + ": §2$§f" + format(balance));
    }

    @Command({"money pay <jogador> <quantidade>"})
    public void enviar(CommandSender sender, @Named("jogador") Player target, @Named("quantidade") double value) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if(target == null || !target.isOnline()) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }
        if(value <= 0) {
            sender.sendMessage("§cValor deve ser superior a 0.");
            return;
        }
        if(value >= economyManager.getBalance((Player) sender)) {
            economyManager.withdraw((Player) sender, value);
            economyManager.deposit((Player) target, value);
            sender.sendMessage(ChatColor.of(mainColor) + "Você enviou §2$§f" + format(value) + ChatColor.of(mainColor) + " para §F" + target.getName());
        } else {
            sender.sendMessage("§cVocê não possui dinheiro suficiente.");
        }
    }

    @Command("money top")
    public void top(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        sender.sendMessage("§r");
        sender.sendMessage(ChatColor.of(mainColor) + "  Jogadores mais ricos:");
        int index = 0;
        for(PlayerModel model : economyManager.getTop10()) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(model.getUuid());
            String prefix = PlaceholderAPI.setPlaceholders(target, "%luckperms_prefix%");
            index++;
            sender.sendMessage("  §7" + index + "º §f" + ChatColor.translateAlternateColorCodes('&', prefix) + target.getName() + "§7: §2$§f" + format(model.getMoney()));
        }
        sender.sendMessage("§r");
        sender.sendMessage(ChatColor.of(mainColor) + "  §7Atualizado a cada §f1 " + ChatColor.of(mainColor) + "§7minuto(s).");
        sender.sendMessage("§r");

    }

    @CommandPermission("zoneeconomy.top.UPDATE")
    @Command("money top update")
    public void updateTop(CommandSender sender) {
        economyManager.setMoneyTop();
        sender.sendMessage("§aO money top foi atualizado com sucesso.");

    }



    @CommandPermission("zoneeconomy.top.display")
    @Command("money top display <value>")
    public void topDisplay(CommandSender sender, @Named("value") float value) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return;
        }

        BlockFace face = player.getFacing().getOppositeFace();
        Block targetBlock = player.getTargetBlockExact(10);
        assert targetBlock != null;
        Location loc = targetBlock.getLocation().clone().setDirection(face.getDirection());
        player.sendMessage("§aface: " + face);
        switch (face) {
            case SOUTH:
                loc.add(0, 0, 1.05);
                break;
            case NORTH:
                loc.add(0, 0, -0.05);
                break;
            case EAST:
                loc.add(1.02, 0, -2.48);
                break;
            case WEST:
                loc.add(-0.05, 0, 0);
                break;
            case UP:
                loc.add(0.5, 1.0, 0.5);
                break;
            case DOWN:
                loc.add(0.5, -1.0, 0.5);
                break;
        }

        TextDisplay display = (TextDisplay) loc.getWorld().spawn(loc, TextDisplay.class);
        Transformation transformation =
                new Transformation(
                        new Vector3f(0),
                        new AxisAngle4f(0, 0, 0, 0),
                        new Vector3f(value, value, value),
                        new AxisAngle4f(0, 0, 0, 0));

        display.setTransformation(transformation);
        display.getPersistentDataContainer().set(economyManager.getKey(), PersistentDataType.STRING, "display");
        economyManager.loadTopDisplay(display);

    }

    @CommandPermission("zoneeconomy.admin")
    @Command("money set <jogador> <quantidade>")
    public void setMoney(CommandSender sender, @Named("jogador") Player target, @Named("quantidade") Double value) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if(target == null || !target.isOnline()) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }
        if(value <= 0) {
            sender.sendMessage("§cValor deve ser superior a 0.");
            return;
        }
        economyManager.setBalance(target, value);
        sender.sendMessage(ChatColor.of(mainColor) + "Você definiu o saldo de §f" + target.getName() + ChatColor.of(mainColor) + " para §2$§f" + format(value));
    }

    @Command("money remove <jogador> <quantidade>")
    @CommandPermission("zoneeconomy.admin")
    public void remove(CommandSender sender, @Named("jogador") Player target, @Named("quantidade") double value) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if(target == null || !target.isOnline()) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }
        if(value <= 0) {
            sender.sendMessage("§cValor deve ser superior a 0.");
            return;
        }
        if(value >= economyManager.getBalance((Player) sender)) {
            economyManager.withdraw(target, value);
            sender.sendMessage("§cVocê removeu §2$§f" + format(value) + "§c do saldo de §f" + target.getName());
        } else {
            sender.sendMessage("§cO valor é maior do que o saldo do jogador.");
        }
    }

    @Command({"money add <jogador> <quantidade> <flag>"})
    @CommandPermission("zoneeconomy.admin")
    public void add(CommandSender sender, @Named("jogador") Player target, @Named("quantidade") double value, @Optional @Named("flag") String flag) {
        boolean console = flag != null && flag.equalsIgnoreCase("-c");

        if(target == null || !target.isOnline() ) {
            if(!console) {
                sender.sendMessage("§cJogador não encontrado.");
            }
            return;
        }
        if(value <= 0) {
            if(!console) {
                sender.sendMessage("§cValor deve ser superior a 0.");
            }
            return;
        }
        economyManager.deposit(target, value);
        if(!console) {
            sender.sendMessage(ChatColor.of(mainColor) + "Você adicionou §2$§f" + format(value) + ChatColor.of(mainColor) + " do saldo de §f" + target.getName());

        }
    }


}
