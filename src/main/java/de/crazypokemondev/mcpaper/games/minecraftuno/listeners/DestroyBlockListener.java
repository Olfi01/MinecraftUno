package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.customblockdata.events.CustomBlockDataEvent;
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

public class DestroyBlockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDestroyedBlock(BlockDropItemEvent event) {
        Block block = event.getBlock();
        if (block.getState().hasMetadata(UnoConstants.METADATA_KEY)) {
            event.setCancelled(true);
            event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), ItemHelper.createUnoDeck());
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
        PersistentDataContainer blockData = new CustomBlockData(event.getBlock(), MinecraftUno.INSTANCE);
        if (blockData.has(UnoConstants.NAMESPACED_KEY) && event.willDrop()) {
            event.setCancelled(true);
            Block block = event.getBlock();
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), ItemHelper.createUnoDeck());
        }
    }
}
