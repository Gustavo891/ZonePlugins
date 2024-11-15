package org.gustaav.zoneMines.managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.Cuboid;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LapisManager implements Listener {

    // Mina Lápis Lazuli
    private BukkitRunnable resetTimerTask;
    private final NamespacedKey DisplayNamespace;
    ZoneMines zoneMines;
    Cuboid lapis;
    Location spawnMine;
    private TextDisplay textDisplay;

    private double progress = 100; // Inicialmente 100% de progresso
    private final int resetTime = 300;  // 5 minutos (em segundos)
    private int resetTimeRemaining = resetTime; // Contagem regressiva para o reset

    public LapisManager(ZoneMines zoneMines) {
        this.zoneMines = zoneMines;
        this.spawnMine = new Location(Bukkit.getWorld("minas"), 107.5, 86, 69.5);
        this.DisplayNamespace = new NamespacedKey(zoneMines, "textdisplay");
        this.lapis = new Cuboid(
                new Location(Bukkit.getWorld("minas"), 46, 83, 92),
                new Location(Bukkit.getWorld("minas"), 92, 57, 46));
    }


    public void reset() {
        progress = 100;  // Reseta o progresso para 100%
        resetTimeRemaining = resetTime; // Reseta o tempo para o reset
        textDisplay.setText(generateText());

        lapis.getPlayers().forEach(player -> {
            player.teleport(spawnMine);

            player.sendMessage("§r");
            player.sendMessage("§r  §e§lEi! §r§eNovidade por aqui.");
            player.sendMessage("§f  A mina §7Lapis§f foi resetada, aproveite-a!");
            player.sendMessage("§r");

        });

        for (Block block : lapis.getBlocks()) {
            double random = Math.random() * 100;
            if (random < 97) {
                block.setType(Material.LAPIS_ORE);
            }
            else {
                block.setType(Material.DIAMOND_ORE);
            }
        }


    }

    public void resetPorcentagem() {
        if (progress <= 50) {
            reset();
        }
    }

    public void createTextDisplay() {
        Location textLocation = new Location(Bukkit.getWorld("minas"), 40.5, 90, 69.5, -90, 0);
        textDisplay = Objects.requireNonNull(textLocation.getWorld()).spawn(textLocation, TextDisplay.class);
        textDisplay.getPersistentDataContainer().set(DisplayNamespace, PersistentDataType.BOOLEAN, true);
        textDisplay.setSeeThrough(true);

        Transformation transformation =
                new Transformation(
                        new Vector3f(0),
                        new AxisAngle4f(0, 0, 0, 0),
                        new Vector3f(5, 5, 5),
                        new AxisAngle4f(0, 0, 0, 0));

        textDisplay.setTransformation(transformation);
        textDisplay.setText(generateText());
        textDisplay.setBackgroundColor(Color.fromARGB(60, 0, 0, 0));
    }

    private String generateText() {
        String timeLeft = formatTime(resetTimeRemaining);
        return "\n" +
                ChatColor.of(new java.awt.Color(0x73ff8c)) + "§lÁrea de Mineração§r\n" +
                "\n" +
                "     §fTem §7" + Objects.requireNonNull(Bukkit.getWorld("minas")).getPlayers().size() + " §fpessoa(s) minerando.     \n" +
                "§fProgresso: §b" + barraProgresso() + "\n\n" +
                "§fA mina irá resetar em §7" + timeLeft + "\n";
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void startResetTimer() {

        if (resetTimerTask != null) {
            resetTimerTask.cancel();
        }

        resetTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                resetPorcentagem();
                if (resetTimeRemaining > 0) {
                    resetTimeRemaining--;
                    textDisplay.setText(generateText());
                } else {
                    reset();
                }
            }
        };

        resetTimerTask.runTaskTimer(zoneMines, 20, 20);
    }

    @EventHandler
    public void remove(PluginEnableEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof TextDisplay) {
                            if (entity.getPersistentDataContainer().has(DisplayNamespace, PersistentDataType.BOOLEAN)) {
                                entity.remove();
                            }
                        }
                    }
                }
                createTextDisplay();
                startResetTimer();
            }
        }.runTaskLater(zoneMines, 60);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(e.getBlock().getLocation().getWorld() == Bukkit.getWorld("minas")) {
            if(lapis.contains(e.getBlock())) {
                if(player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                List<ItemStack> drops = e.getBlock().getDrops(player.getInventory().getItemInMainHand()).stream().toList();
                for (ItemStack drop : drops) {
                    player.getInventory().addItem(drop);  // Adiciona o item ao inventário
                }
                e.setCancelled(false);
                player.giveExp(e.getExpToDrop());
                e.setExpToDrop(0);
                e.setDropItems(false);
                if(player.getInventory().firstEmpty() == -1) {
                    player.sendTitle(ChatColor.of(new java.awt.Color(0xff0f17)) + "§lAlerta!",ChatColor.of(new java.awt.Color(0xff0f17)) + "§cSeu inventário está cheio!", 0, 70, 20);
                }

            } else {
                e.setCancelled(true);
            }
        }
    }


    public double checkProgress() {


        double totalBlocos = lapis.getBlocks().size();
        double airCount = lapis.getBlocks().stream()
                .filter(block -> block.getType() == Material.AIR) // Filtra blocos que são "air"
                .count();
        return ((totalBlocos-airCount)/totalBlocos)*100;


    }


    public String barraProgresso() {
        int barrasTotais = 10;

        double percentual = checkProgress();
        percentual = Math.min(percentual, 100);

        int barrasPreenchidas = (int) (percentual / 100 * barrasTotais);
        int barrasVazias = barrasTotais - barrasPreenchidas;

        return "§2" + "⬛".repeat(Math.max(0, barrasPreenchidas)) +
                "§8" +
                "⬛".repeat(Math.max(0, barrasVazias)) +
                " §7" + (int) percentual + "%";
    }


}
