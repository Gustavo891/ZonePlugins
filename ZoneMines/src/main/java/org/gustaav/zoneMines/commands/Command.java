package org.gustaav.zoneMines.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Command extends BukkitCommand {
    protected Command(String name, String description, String usageMessage, String[] aliases, String permission) {
        super(name);
        this.setPermission(permission);
        this.setPermissionMessage("§cVocê não tem permissão para executar esse comando.");
        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setUsage(usageMessage);

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(name,this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        execute(commandSender, strings);
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);
}

