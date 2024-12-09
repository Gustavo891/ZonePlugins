package org.gustaav.zoneMines.managers.classic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.gustaav.zoneBoosters.BoosterAPI;
import org.gustaav.zoneBoosters.model.BoosterTypes;
import org.gustaav.zoneBoosters.model.PlayerModel;
import org.gustaav.zoneEnchants.EnchantAPI;
import org.gustaav.zoneMines.ZoneMines;
import org.gustaav.zoneMines.modules.Cuboid;
import org.gustaav.zoneMines.modules.SellModule;
import org.gustaav.zoneMines.utils.MessageUtil;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LapisManager implements Listener {

    // MINA PADRÃO

    private BukkitRunnable resetTimerTask;
    private final NamespacedKey DisplayNamespace;
    ZoneMines zoneMines;
    Cuboid classica;
    Location spawnMine;
    private TextDisplay textDisplay;

    ClassicSell classicSell;

    NamespacedKey dropIdentifier;
    ItemStack drop = new ItemStack(Material.COPPER_INGOT);
    Material mineBlock = Material.ORANGE_TERRACOTTA;

    private double progress = 100; // Inicialmente 100% de progresso
    private final int resetTime = 300;  // 5 minutos (em segundos)
    private int resetTimeRemaining = resetTime; // Contagem regressiva para o reset

    public LapisManager(ZoneMines zoneMines, SellModule sellModule) {
        this.zoneMines = zoneMines;
        this.spawnMine = new Location(Bukkit.getWorld("minas"), 107.5, 86, 69.5);
        this.DisplayNamespace = new NamespacedKey(zoneMines, "textdisplay");
        this.dropIdentifier = new NamespacedKey(zoneMines, "dropidentifier");
        this.classica = new Cuboid(
                new Location(Bukkit.getWorld("minas"), 46, 83, 92),
                new Location(Bukkit.getWorld("minas"), 92, 57, 46));
        loadDrop(drop);
        this.classicSell = new ClassicSell(zoneMines);
    }

    public void loadDrop(ItemStack drop) {
        ItemMeta meta = drop.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<color:#b87333>Fragmento de Cobre</color>").decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Resquício metálico pulsante,"));
        lore.add(Component.text("§7com vestígios de eletricidade."));
        lore.add(Component.text("§r"));
        lore.add(Component.text("§8Venda-o no guardião da mina."));
        meta.lore(lore);

        meta.getPersistentDataContainer().set(dropIdentifier, PersistentDataType.STRING, "cobre");

        drop.setItemMeta(meta);
    }
    public Cuboid getCuboid() {
        return classica;
    }
    public void reset() {
        progress = 100;  // Reseta o progresso para 100%
        resetTimeRemaining = resetTime; // Reseta o tempo para o reset
        textDisplay.setText(generateText());

        classica.getPlayers().forEach(player -> {
            player.teleport(spawnMine);

            player.sendMessage("§r");
            player.sendMessage("§r  §e§lEi! §r§eNovidade por aqui.");
            player.sendMessage("§f  A mina §7Clássica§f foi resetada, aproveite-a!");
            player.sendMessage("§r");

        });

        for (Block block : classica.getBlocks()) {
            block.setType(mineBlock);
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
                ChatColor.of(new java.awt.Color(0xff6f00)) + "§lMINA CLÁSSICA§r\n" +
                "\n" +
                "     §fTem §7" + getPlayers() + " §fpessoa(s) minerando.     \n" +
                "§fProgresso: §b" + barraProgresso() + "\n\n" +
                "§fA mina irá resetar em §7" + timeLeft + "\n";
    }
    public int getPlayers() {
        return Objects.requireNonNull(Bukkit.getWorld("minas")).getPlayers().size();
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
                reset();
            }
        }.runTaskLater(zoneMines, 60);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(e.getBlock().getLocation().getWorld() == Bukkit.getWorld("minas")) {
            if(classica.contains(e.getBlock())) {
                // Criativo não acontece nada
                if(player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                // Picareta do jogador
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                int explosivoLevel = EnchantAPI.getInstance().getEnchantLevel(itemInHand, "explosivo");
                int fortune = itemInHand.getEnchantmentLevel(Enchantment.FORTUNE);

                PlayerModel booster = BoosterAPI.getInstance().getBoosterManager().getTypeBooster(player.getUniqueId(), BoosterTypes.MINERACAO);
                int multiplier = 1;
                if(booster != null) {
                    multiplier = booster.getMultiplier();
                }
                Random rand = new Random();

                // Caso ele tenha explosão
                if(explosivoLevel > 0) {
                    double chance = ThreadLocalRandom.current().nextDouble(1, 100);

                    if(chance < ((double) explosivoLevel)) {
                        int radius = explosivoLevel + 1;
                        int totalBlocos = 0;
                        Location location = e.getBlock().getLocation();
                        for (int x = -radius; x <= radius; x++) {
                            for (int y = -radius; y <= radius; y++) {
                                for (int z = -radius; z <= radius; z++) {
                                    double distance = Math.sqrt(x * x + y * y + z * z);
                                    if (distance <= radius) {
                                        Block block = location.clone().add(x, y, z).getBlock();
                                        if (classica.contains(block.getLocation())) {
                                            totalBlocos++;
                                            block.setType(Material.AIR);
                                        }
                                    }
                                }
                            }
                        }
                        ItemStack drops = drop.clone();
                        int fortuneMultiplier = (fortune > 0) ? rand.nextInt(1, fortune+2) : 1;
                        drops.setAmount(multiplier * fortuneMultiplier * totalBlocos);
                        player.getInventory().addItem(drops);
                        location.getWorld().spawnParticle(Particle.EXPLOSION, location, radius);
                        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        MessageUtil.sendFormattedMessage(player, String.format(
                                "<#AA3000><b>Explosão </b><dark_gray>✦ <white>Quebrou um total de <#AA680D>%s <white>blocos."
                                , SellModule.format(totalBlocos)));
                    }
                }
                int fortuneMultiplier = (fortune > 0) ? rand.nextInt(1, fortune+2) : 1;
                ItemStack drops = drop.clone();
                drops.setAmount(multiplier * fortuneMultiplier);
                player.getInventory().addItem(drops);

                e.setCancelled(false);
                player.giveExp(e.getExpToDrop());
                e.setExpToDrop(0);
                e.setDropItems(false);
                if(player.getInventory().firstEmpty() == -1) {
                    player.sendTitle(ChatColor.of(new java.awt.Color(0xff0f17)) + "§lAlerta!",ChatColor.of(new java.awt.Color(0xff0f17)) + "§cSeu inventário está cheio!", 0, 70, 20);
                }

            } else {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    return;
                }
                e.setCancelled(true);
            }
        }
    }

    public double checkProgress() {

        double totalBlocos = classica.getBlocks().size();
        double airCount = classica.getBlocks().stream()
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

        return "§6" + "⬛".repeat(Math.max(0, barrasPreenchidas)) +
                "§8" +
                "⬛".repeat(Math.max(0, barrasVazias)) +
                " §7" + (int) percentual + "%";
    }

}
