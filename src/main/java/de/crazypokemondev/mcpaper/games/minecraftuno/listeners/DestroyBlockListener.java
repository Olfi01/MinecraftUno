package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.customblockdata.events.CustomBlockDataEvent;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;

public class DestroyBlockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDestroyedBlock(BlockDropItemEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (state.hasMetadata(UnoConstants.METADATA_KEY)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (player.getGameMode() != GameMode.CREATIVE)
                player.getWorld().dropItemNaturally(block.getLocation(), ItemHelper.createUnoDeck());

            World world = event.getBlock().getWorld();
            Collection<Entity> entities = world.getNearbyEntitiesByType(ItemFrame.class, block.getLocation(), 0.5);
            entities.stream().findAny().ifPresent(Entity::remove);

            state.removeMetadata(UnoConstants.METADATA_KEY, MinecraftUno.INSTANCE);
            state.update();
        }
    }

    @EventHandler
    public void onBlockDataRemoved(CustomBlockDataEvent event) {
        if (event.getBukkitEvent() instanceof BlockBreakEvent
                && event.getCustomBlockData().has(UnoConstants.NAMESPACED_KEY)) {
            BlockState state = event.getBlock().getState();
            state.setMetadata(UnoConstants.METADATA_KEY, new FixedMetadataValue(MinecraftUno.INSTANCE, true));
            state.update();
        }
    }

    @EventHandler
    public void onDestroyedBlock(BlockDestroyEvent event) {
        CustomBlockData blockData = new CustomBlockData(event.getBlock(), MinecraftUno.INSTANCE);
        if (blockData.has(UnoConstants.NAMESPACED_KEY) && event.willDrop()) {
            event.setCancelled(true);
            Block block = event.getBlock();
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), ItemHelper.createUnoDeck());
            blockData.clear();
            Collection<ItemFrame> entities =
                    block.getWorld().getNearbyEntitiesByType(ItemFrame.class, block.getLocation(), 0.5);
            entities.stream().filter(frame -> frame.getItem().isSimilar(ItemHelper.createUnoDeck())).forEach(Entity::remove);
        }
    }
}
