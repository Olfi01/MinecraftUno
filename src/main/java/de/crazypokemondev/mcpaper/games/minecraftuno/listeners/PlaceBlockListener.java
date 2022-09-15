package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
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
        customBlockData.set(UnoConstants.NAMESPACED_KEY, PersistentDataType.BYTE, (byte)1);


        Block block = event.getBlock();
        World world = block.getWorld();
        ItemFrame frame = (ItemFrame) world.spawnEntity(block.getLocation(), EntityType.ITEM_FRAME);
        frame.setFacingDirection(BlockFace.UP);
        frame.setItem(ItemHelper.createUnoDeck(), false);
        frame.setFixed(true);
    }

    private boolean isUnoDeck(ItemStack inHand) {
        return inHand.getType() == Material.RED_CARPET && inHand.getItemMeta().hasCustomModelData()
                && inHand.getItemMeta().getCustomModelData() == UnoConstants.CUSTOM_MODEL_DATA;
    }
}
