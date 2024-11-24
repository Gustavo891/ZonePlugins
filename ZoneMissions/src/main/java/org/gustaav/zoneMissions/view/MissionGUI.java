package org.gustaav.zoneMissions.view;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.gustaav.zoneMissions.manager.MissionCreator;
import org.gustaav.zoneMissions.manager.MissionManager;
import org.gustaav.zoneMissions.models.MissionModel;
import org.gustaav.zoneMissions.models.PlayerModel;
import org.gustaav.zoneMissions.models.RewardModel;
import org.gustaav.zoneMissions.models.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MissionGUI {

    ChestGui missionGui;
    PlayerModel playerModel;
    Player player;
    MissionCreator missionCreator;
    MissionModel currentMission;

    public MissionGUI(PlayerModel playerModel, Player player, MissionCreator missionCreator) {
        currentMission = playerModel.getMission();
        missionGui = new ChestGui(4, "Missões:");
        this.playerModel = playerModel;
        this.player = player;
        this.missionCreator = missionCreator;
        PaginatedPane pane = new PaginatedPane(Slot.fromIndex(10), 7, 1);

        missionGui.setOnGlobalClick(e -> {
            e.setCancelled(true);
        });

        loadMission(pane);
        missionGui.addPane(pane);
        missionGui.show(player);

    }

    private void loadMission(PaginatedPane pane) {

        List<GuiItem> items = new ArrayList<>();

        for(MissionModel missionModel : missionCreator.getMissions()) {

            //carregar atual
            if(missionModel == currentMission) {
                items.add(loadCurrent());
            } else if(missionCreator.getMissions().indexOf(missionModel) < missionCreator.getMissions().indexOf(currentMission)) {
                items.add(loadComplete(missionModel));
            } else {
                items.add(loadNext(missionModel));
            }

        }

        pane.populateWithGuiItems(items);

    }

    private GuiItem loadCurrent() {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI5MTYyMmM1MTA0ZjFjYzk0YTJiNGQzM2FkN2IyYzliMjE1ZmY5NDk1MDFlOGMyNTBjZmQ1YTg2MWU2YjkwNyJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#ffcb30>" + currentMission.getName())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        String desc = currentMission.getDescription();
        String[] lines = desc.split("\n");
        for (String line : lines) {
            lore.add(Component.text("§7" + line));
        }
        lore.add(Component.text("§r"));
        int total = 0;
        int total2 = 0;
        for(TaskModel taskModel : currentMission.getTasks()) {
            int value = playerModel.getTaskProgress().get(taskModel);
            total += value;
            total2 += taskModel.getAmount();
            lore.add(Component.text(String.format("  §c✖ §f%s §7[%s/%s]", taskModel.getDescription(), value, taskModel.getAmount())));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§e  Recompensas:"));
        for(RewardModel rewardModel : currentMission.getRewards()) {
            lore.add(Component.text("  §7◆ §f" + rewardModel.getNome()));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§fProgresso: §2" + MissionCreator.criarBarraProgresso(total, total2)));
        meta.lore(lore);
        item.setItemMeta(meta);

        return new GuiItem(item);

    }
    private GuiItem loadComplete(MissionModel currentMission) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZlYzNkMjVhZTBkMTQ3YzM0MmM0NTM3MGUwZTQzMzAwYTRlNDhhNWI0M2Y5YmI4NThiYWJmZjc1NjE0NGRhYyJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#00B018>" + currentMission.getName())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        String desc = currentMission.getDescription();
        String[] lines = desc.split("\n");
        for (String line : lines) {
            lore.add(Component.text("§7" + line));
        }
        lore.add(Component.text("§r"));
        for(TaskModel taskModel : currentMission.getTasks()) {
            lore.add(Component.text(String.format("  §2✔ §f%s §7[%s/%s]", taskModel.getDescription(), taskModel.getAmount(), taskModel.getAmount())));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§a  Recompensas:"));
        for(RewardModel rewardModel : currentMission.getRewards()) {
            lore.add(Component.text("  §7◆ §f" + rewardModel.getNome()));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§8Você completou essa missão!"));
        meta.lore(lore);
        item.setItemMeta(meta);

        return new GuiItem(item);

    }
    private GuiItem loadNext(MissionModel currentMission) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQ4ZDdkMWUwM2UxYWYxNDViMDEyNWFiODQxMjg1NjcyYjQyMTI2NWRhMmFiOTE1MDE1ZjkwNTg0MzhiYTJkOCJ9fX0=";
        UUID uuid = UUID.randomUUID();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid, "");
        playerProfile.setProperty(new ProfileProperty("textures", base64Texture));
        meta.setPlayerProfile(playerProfile);
        Component component = MiniMessage.miniMessage().deserialize("<#AA0018>" + currentMission.getName())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(component);
        List<Component> lore = new ArrayList<>();
        String desc = currentMission.getDescription();
        String[] lines = desc.split("\n");
        for (String line : lines) {
            lore.add(Component.text("§7" + line));
        }
        lore.add(Component.text("§r"));
        for(TaskModel taskModel : currentMission.getTasks()) {
            lore.add(Component.text(String.format("  §c⚑ §f%s §7[0/%s]", taskModel.getDescription(), taskModel.getAmount())));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§c  Recompensas:"));
        for(RewardModel rewardModel : currentMission.getRewards()) {
            lore.add(Component.text("  §7◆ §f" + rewardModel.getNome()));
        }
        lore.add(Component.text("§r"));
        lore.add(Component.text("§8Você ainda não liberou essa missão"));
        meta.lore(lore);
        item.setItemMeta(meta);

        return new GuiItem(item);

    }

}
