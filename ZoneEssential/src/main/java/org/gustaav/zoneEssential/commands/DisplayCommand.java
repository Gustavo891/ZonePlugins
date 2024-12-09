package org.gustaav.zoneEssential.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.gustaav.zoneEssential.ZoneEssential;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Suggest;

public class DisplayCommand {

    NamespacedKey key;

    public DisplayCommand(ZoneEssential plugin) {
        key = new NamespacedKey(plugin, "displayId");
    }

    @Command("display create <id>")
    public void displayCreate(CommandSender sender, @Named("id") String id) {

        if(!(sender instanceof Player player)) {
            return;
        }

        for(World world : Bukkit.getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                    if(display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                        sender.sendMessage("§cJá existe um display com esse ID.");
                        return;
                    }
                }
            }
        }

        Location loc = player.getLocation().clone().add(0, 1, 0);

        TextDisplay display = (TextDisplay) loc.getWorld().spawn(loc, TextDisplay.class);
        display.setShadowed(true);
        display.setPersistent(true);
        display.setBillboard(Display.Billboard.CENTER);
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);

        player.sendMessage("§aDisplay criado com o ID: " + id);
    }

    @Command("display remove <id>")
    public void displayRemove(CommandSender sender, @Named("id") String id) {

        if (!(sender instanceof Player player)) {
            return;
        }
            for(Entity entity : player.getWorld().getEntities()) {
                if(entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                    if(display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                        display.remove();
                        sender.sendMessage("§cDisplay removido");
                        return;
                    }
                }
            }

        sender.sendMessage("§cNenhum display com esse ID encontrado.");

    }

    @Command("display text <id> <texto>")
    public void displayEdit(CommandSender sender, @Named("id") String id, @Named("texto") String[] texto) {
        TextDisplay displayId = null;
        if(!(sender instanceof Player player)) {
            return;
        }



            for (Entity entity : player.getWorld().getEntities()) {
                if (entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                    if (display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                        displayId = display;
                        break; // Sai do loop de entidades
                    }
                }
            }


        if (displayId == null) {
            player.sendMessage("§cID não encontrado.");
            return;
        }

        // Juntar os elementos do texto em uma única string e substituir \n por uma quebra de linha real
        String displayText = String.join(" ", texto).replace("\\n", "\n").replace("&", "§");

        // Definir o texto do display com as quebras de linha reais
        displayId.setText(displayText);
        player.sendMessage("§aTexto do display atualizado com sucesso.");

    }

    @Command("display color <id> <tipo>")
    public void displayColor(CommandSender sender,  @Named("id") String id, @Named("tipo") @Suggest({"transparent", "default"}) String tipo) {
        if (!(sender instanceof Player player)) {
            return;
        }
        TextDisplay displayId = null;



            for (Entity entity : player.getWorld().getEntities()) {
                if (entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                    if (display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                        displayId = display;
                        break; // Sai do loop de entidades
                    }
                }
            }


        if (displayId == null) {
            player.sendMessage("§cID não encontrado.");
            return;
        }

        switch (tipo) {
            case "transparent":
                displayId.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                player.sendMessage("§aCor do backgroud definida para transparente.");
                return;
            case "default":
                displayId.setDefaultBackground(true);
                player.sendMessage("§aCor do backgroud definida para default.");
                return;
            default:
                player.sendMessage("§cCor inválida.");
                return;
        }

    }
    @Command("display transformation <id> <tipo>")
    public void displayTransformation(CommandSender sender,  @Named("id") String id, @Named("tipo") float tipo) {
        if (!(sender instanceof Player player)) {
            return;
        }
        TextDisplay displayId = null;

        for (Entity entity : player.getWorld().getEntities()) {
                if (entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                    if (display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                        displayId = display;
                        break; // Sai do loop de entidades
                    }
                }
        }


        if (displayId == null) {
            player.sendMessage("§cID não encontrado.");
            return;
        }

        Transformation transformation =
                new Transformation(
                        new Vector3f(0),
                        new AxisAngle4f(0, 0, 0, 0),
                        new Vector3f(tipo, tipo, tipo),
                        new AxisAngle4f(0, 0, 0, 0));
        displayId.setTransformation(transformation);
        player.sendMessage("§aTamanho do display definido para: " + tipo);
    }

    @Command("display type <id> <tipo>")
    public void displayType(CommandSender sender,  @Named("id") String id, @Suggest({"FIXO", "VERTICAL", "HORIZONTAL", "CENTER"}) @Named("tipo") String tipo) {
        if (!(sender instanceof Player player)) {
            return;
        }
        TextDisplay displayId = null;

        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                if (display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                    displayId = display;
                    break; // Sai do loop de entidades
                }
            }
        }


        if (displayId == null) {
            player.sendMessage("§cID não encontrado.");
            return;
        }

        switch (tipo) {
            case "FIXO":
                displayId.setBillboard(Display.Billboard.FIXED);
            case "CENTER":
                displayId.setBillboard(Display.Billboard.CENTER);
            case "HORIZONTAL":
                displayId.setBillboard(Display.Billboard.HORIZONTAL);
            case "VERTICAL":
                displayId.setBillboard(Display.Billboard.VERTICAL);
        }
        displayId.getLocation().setPitch(0);
        player.sendMessage("§aTipo do display definido para: " + tipo);
    }

    @Command("display tp <id>")
    public void displayTp(CommandSender sender,  @Named("id") String id) {
        if (!(sender instanceof Player player)) {
            return;
        }
        TextDisplay displayId = null;

        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof TextDisplay display && display.getPersistentDataContainer().has(key)) {
                if (display.getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(id)) {
                    displayId = display;
                    break; // Sai do loop de entidades
                }
            }
        }


        if (displayId == null) {
            player.sendMessage("§cID não encontrado.");
            return;
        }

        Location location = player.getLocation();
        location.setPitch(0);
        displayId.teleport(location);
        player.sendMessage("§aDisplay teleportado com sucesso");
    }
}
