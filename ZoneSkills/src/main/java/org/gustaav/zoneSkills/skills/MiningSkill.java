package org.gustaav.zoneSkills.skills;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.gustaav.zoneSkills.Database.PlayerDataManager;
import org.gustaav.zoneSkills.rewards.RewardModel;
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.utils.RandomUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningSkill implements Listener {

    private final ZoneSkills plugin;
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, Long> playerMiningStartTime = new HashMap<>();
    private final Map<Material, Double> blockXpMap = new HashMap<>();

    public MiningSkill(ZoneSkills plugin, PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
        this.plugin = plugin;

        blockXpMap.put(Material.LAPIS_ORE, 200.0);
        blockXpMap.put(Material.DIAMOND_ORE, 1500.0); // Exemplo para pedras comuns
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        double exp = getExpForBlock(event.getBlock().getType());
        if (exp <= 0) return;  // Ignora blocos sem XP

        boolean levelUp = playerDataManager.addXp(uuid, SkillType.mining, exp);

        // Obtém o nível e XP atual do jogador para a skill
        int level = playerDataManager.getLevel(uuid, SkillType.mining);
        double xp = playerDataManager.getXp(uuid, SkillType.mining);
        double xpRequired = playerDataManager.getXpRequiredForLevel(level);

        // Atualiza ou cria a BossBar para o jogador
        BossBar bossBar = playerBossBars.computeIfAbsent(uuid, id -> createBossBar(player));
        bossBar.setTitle("§aMineração §l" + level + "§r §7(" + RandomUtil.format((int) xp) + "/" + RandomUtil.format((int) xpRequired) + "§7 XP)");
        bossBar.setProgress(Math.min(xp / xpRequired, 1.0));

        playerMiningStartTime.put(uuid, System.currentTimeMillis());

        if(levelUp) {
            levelUp(player);
        }

        scheduleBossBarRemoval(uuid);
    }

    public void levelUp(Player player) {
        int level = playerDataManager.getLevel(player.getUniqueId(), SkillType.mining);

        player.sendMessage("");
        player.sendMessage(ChatColor.of(new Color(0x4eff03)) + "  §lMineração §7[§m" + (level-1) + "§r§8 ➡ §7" + level + "]");
        player.sendMessage("  §fNível evoluido com sucesso.");
        player.sendMessage("  ");

        if(level == 20) {
            player.sendMessage("     §7Recompensas:");
            player.sendMessage("     §8❒ §fComando " + ChatColor.of(new Color(0x70ff45)) + "/compactar §fliberado.");
            player.sendMessage("§r");
            plugin.getPermissions().playerAdd(player, "zonemines.compactar");
        } else if (level == 50) {
            player.sendMessage("     §7Recompensas:");
            player.sendMessage("     §8❒ §fComando " + ChatColor.of(new Color(0x70ff45)) + "/vender §fliberado.");
            player.sendMessage("§r");
            plugin.getPermissions().playerAdd(player, "zonemines.vender");
        } else if (level == 100) {
            player.sendMessage("     §7Recompensas:");
            player.sendMessage("     §8❒ §fComando " + ChatColor.of(new Color(0x70ff45)) + "/autovender §fliberado.");
            player.sendMessage("§r");
            plugin.getPermissions().playerAdd(player, "zonemines.autovender");
        }

    }

    private BossBar createBossBar(Player player) {
        BossBar bossBar = Bukkit.createBossBar("Progresso de Mineração", BarColor.GREEN, BarStyle.SEGMENTED_10);
        bossBar.addPlayer(player);
        return bossBar;
    }

    private double getExpForBlock(Material blockType) {
        return blockXpMap.getOrDefault(blockType, 0.0);
    }

    private void scheduleBossBarRemoval(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Verifica se o jogador não está minerando há mais de 5 segundos
                if (System.currentTimeMillis() - playerMiningStartTime.getOrDefault(uuid, 0L) > 5000) {
                    removeBossBar(uuid);
                } else {
                    // Caso o jogador continue minerando, agende a remoção para daqui a mais 2 segundos
                    scheduleBossBarRemoval(uuid);
                }
            }
        }.runTaskLater(plugin, 40L); // Verifica a cada 2 segundos (40 ticks)
    }

    private void removeBossBar(UUID uuid) {
        BossBar bossBar = playerBossBars.remove(uuid);
        if (bossBar != null) {
            bossBar.removeAll(); // Remove a BossBar de todos os jogadores
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        removeBossBar(uuid);
    }

}
