package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlaceBlockListener implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        ItemStack inHand = event.getItemInHand();
        if (!isUnoDeck(inHand)) return;
        Block blockPlaced = event.getBlockPlaced();
        PersistentDataContainer customBlockData = new CustomBlockData(blockPlaced, MinecraftUno.INSTANCE);
        NamespacedKey key = new NamespacedKey(MinecraftUno.INSTANCE, UnoConstants.METADATA_KEY);
        customBlockData.set(key, PersistentDataType.BYTE, (byte)1);
        blockPlaced.getState().setMetadata(UnoConstants.METADATA_KEY,
                new FixedMetadataValue(MinecraftUno.INSTANCE, true));
    }

    private boolean isUnoDeck(ItemStack inHand) {
        return inHand.getItemMeta().hasCustomModelData()
                && inHand.getItemMeta().getCustomModelData() == UnoConstants.CUSTOM_MODEL_DATA;
    }
}
