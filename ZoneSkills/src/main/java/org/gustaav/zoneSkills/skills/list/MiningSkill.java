package org.gustaav.zoneSkills.skills.list;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.gustaav.zoneSkills.ZoneSkills;
import org.gustaav.zoneSkills.skills.SkillType;
import org.gustaav.zoneSkills.utils.RandomUtil;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MiningSkill implements Listener {

    private final ZoneSkills plugin;
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, Long> playerMiningStartTime = new HashMap<>();
    private final Map<Material, Double> blockXpMap = new HashMap<>();

    private final List<UUID> miningTop = new ArrayList<>();

    public MiningSkill(ZoneSkills plugin, PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
        this.plugin = plugin;

        blockXpMap.put(Material.LAPIS_ORE, 10.0);
        blockXpMap.put(Material.DIAMOND_ORE, 35.0);
        updateMiningTop();
        startMiningTopUpdater();
    }

    private void startMiningTopUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateMiningTop();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 60 * 20);
    }

    private void updateMiningTop() {
        miningTop.clear();

        List<UUID> sortedPlayers = playerDataManager.getAllPlayerData().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(
                        playerDataManager.getLevel(entry2.getKey(), SkillType.mining),
                        playerDataManager.getLevel(entry1.getKey(), SkillType.mining)))
                .map(Map.Entry::getKey)
                .limit(5)
                .toList();

        miningTop.addAll(sortedPlayers);
    }

    public List<UUID> getMiningTop() {
        List<UUID> sortedTop = new ArrayList<>(miningTop);

        sortedTop.sort((uuid1, uuid2) -> {
            int level1 = playerDataManager.getLevel(uuid1, SkillType.mining);
            int level2 = playerDataManager.getLevel(uuid2, SkillType.mining);
            return Integer.compare(level2, level1);
        });

        return Collections.unmodifiableList(sortedTop);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

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

        player.sendMessage(String.format("§a§l✬ §r§aVocê chegou ao nível %s de mineração.", level));

        if(level == 20) {
            plugin.getPermissions().playerAdd(player, "zonemines.compactar");
        } else if (level == 50) {
            plugin.getPermissions().playerAdd(player, "zonemines.vender");
        } else if (level == 100) {
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
