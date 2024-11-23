package org.gustaav.zoneMonsters.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.gustaav.zoneMonsters.manager.MonsterManager;
import org.gustaav.zoneMonsters.models.MonsterModel;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;

public class MonsterCommand {

    MonsterManager monsterManager;
    public MonsterCommand(MonsterManager monsterManager) {
        this.monsterManager = monsterManager;
    }

    @Command("monster killall <tipo>")
    public void killAllMonsters(CommandSender sender, @Named("tipo") @Optional String tipo) {
            if(!(sender instanceof Player player)) return;

            for(Entity entity : player.getWorld().getEntities()) {
                if(entity instanceof LivingEntity) {
                    PersistentDataContainer data = entity.getPersistentDataContainer();
                    if(data.has(monsterManager.getBOSS_TYPE_KEY())) {
                        if(tipo == null) {
                            entity.remove();
                        } else if(data.get(monsterManager.getBOSS_TYPE_KEY(), PersistentDataType.STRING).equals(tipo)){
                            entity.remove();
                        }
                    }
                }
            }

    }

    @Command("monster <jogador> <tipo> <quantidade>")
    public void monsterCommand(CommandSender sender, @Named("jogador") Player jogador,@Named("tipo") @Suggest("profundo") String tipo,@Named("quantidade") Integer quantidade) {


        for(MonsterModel monster : monsterManager.getMonsters()) {
            if (monster.getId().equalsIgnoreCase(tipo)) {
                ItemStack egg = monsterManager.giveMonster(monster, quantidade);
                jogador.getInventory().addItem(egg);
            }
        }


    }

}
