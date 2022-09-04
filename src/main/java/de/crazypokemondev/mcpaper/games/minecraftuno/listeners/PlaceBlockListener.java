package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.NamespacedKey;
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
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), MinecraftUno.INSTANCE);
        NamespacedKey key = new NamespacedKey(MinecraftUno.INSTANCE, UnoConstants.METADATA_KEY);
        customBlockData.set(key, PersistentDataType.BYTE, (byte)1);
    }

    private boolean isUnoDeck(ItemStack inHand) {
        return inHand.getItemMeta().hasCustomModelData()
                && inHand.getItemMeta().getCustomModelData() == UnoConstants.CUSTOM_MODEL_DATA;
    }
}
