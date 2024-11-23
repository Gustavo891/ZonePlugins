package org.gustaav.zonePrivateMines.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.gustaav.zonePrivateMines.Cuboid;
import org.gustaav.zonePrivateMines.ZonePrivateMines;
import revxrsal.commands.annotation.Command;

public class MineCommand {

    private final Cuboid mine;
    private final ZonePrivateMines plugin;

    public MineCommand(ZonePrivateMines plugin) {
        this.plugin = plugin;
        setupProtocolLibListener();
        mine = new Cuboid(
                new Location(Bukkit.getWorld("spawn"), -2, 117, 8),
                new Location(Bukkit.getWorld("spawn"), 2, 113, 4)
        );
    }

    @Command("privatemine")
    public void generateMine(CommandSender sender) {

        if (!(sender instanceof Player player)) {
            return;
        }

        // Posição inicial da mina
        Location center = player.getLocation().add(0, 6, 0);
        int size = 5; // Tamanho da mina (5x5)
        Material material = Material.DIAMOND_ORE;

        for(Block block : mine.getBlocks()) {
            player.sendBlockChange(block.getLocation(), material.createBlockData());
        }

        player.sendMessage("§aMina privada gerada!");

    }


    private void setupProtocolLibListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        // Captura da quebra de bloco
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.BLOCK_CHANGE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                var position = event.getPacket().getBlockPositionModifier().read(0);
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();

                if(mine.contains(x, y, z)) {
                    //event.getPacket().getBlockData().write(0, WrappedBlockData.createData(Material.DIAMOND_ORE));
                }

            }
        });



        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {

                var packet = event.getPacket();
                Player player = event.getPlayer();
                var action = packet.getPlayerDigTypes().read(0);

                if(action == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK){
                    BlockPosition blockPosition = packet.getBlockPositionModifier().read(0);
                    //if(mine.contains(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())) {
                        player.sendMessage("§abloco quebrado");
                        PacketContainer packetContainer = buildSingleBlockPacket(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), Material.AIR);
                        protocolManager.sendServerPacket(player, packetContainer);
                        event.setCancelled(true);
                    //}
                }
            }
        });
    }

    public PacketContainer buildSingleBlockPacket(int blockX, int blockY, int blockZ, Material material) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);

        packet.getBlockPositionModifier().write(0, new BlockPosition(blockX, blockY, blockZ));
        packet.getBlockData().write(0, WrappedBlockData.createData(material));

        return packet;
    }

}
