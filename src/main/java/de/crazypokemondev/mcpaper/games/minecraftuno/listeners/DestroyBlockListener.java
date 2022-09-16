package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.customblockdata.events.CustomBlockDataEvent;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.game.UnoGameLobby;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.BlockPos;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ArmorStandHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Map;

public class DestroyBlockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDestroyedBlock(BlockBreakEvent event) {
        if (cleanGameForBrokenTable(event.getBlock(), !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))) {
            event.setDropItems(false);
        }
    }

    @EventHandler
    public void onDestroyedBlock(BlockDestroyEvent event) {
        if (cleanGameForBrokenTable(event.getBlock(), true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        cleanGameForBrokenTable(event.getBlock(), false);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().forEach(block -> cleanGameForBrokenTable(block, false));
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> cleanGameForBrokenTable(block, false));
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        Block blockTo = event.getToBlock();
        CustomBlockData blockData = new CustomBlockData(blockTo, MinecraftUno.INSTANCE);
        if (blockData.has(UnoConstants.NAMESPACED_KEY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        event.getBlocks().forEach(block -> {
            CustomBlockData blockData = new CustomBlockData(block, MinecraftUno.INSTANCE);
            if (blockData.has(UnoConstants.NAMESPACED_KEY)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        event.getBlocks().forEach(block -> {
            CustomBlockData blockData = new CustomBlockData(block, MinecraftUno.INSTANCE);
            if (blockData.has(UnoConstants.NAMESPACED_KEY)) {
                event.setCancelled(true);
            }
        });
    }


    private static boolean cleanGameForBrokenTable(Block block, boolean dropItem) {
        CustomBlockData blockData = new CustomBlockData(block, MinecraftUno.INSTANCE);
        if (blockData.has(UnoConstants.NAMESPACED_KEY)) {
            block.setType(Material.AIR);
            if (dropItem) block.getWorld().dropItemNaturally(block.getLocation(), ItemHelper.createUnoDeck());
            blockData.clear();

            World world = block.getWorld();
            ArmorStandHelper.removeArmorStands(world, block.getLocation());

            Map<BlockPos, UnoGameLobby> lobbies = MinecraftUno.INSTANCE.lobbies.get(world.getUID());
            if (lobbies != null) lobbies.remove(new BlockPos(block));

            return true;
        }
        return false;
    }
}
