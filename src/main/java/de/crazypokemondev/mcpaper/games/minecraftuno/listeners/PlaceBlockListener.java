package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

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

        Location armorStandLoc = block.getLocation().add(0, -1.65, -0.25);

        ArmorStand armorStand = (ArmorStand)world.spawnEntity(armorStandLoc, EntityType.ARMOR_STAND);
        armorStand.setHeadPose(new EulerAngle(90, 0, 0));
        armorStand.setItem(EquipmentSlot.HEAD, ItemHelper.createUnoDeck());

        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setPersistent(true);
        armorStand.setSilent(true);
    }

    private boolean isUnoDeck(ItemStack inHand) {
        return inHand.getType() == Material.RED_CARPET && inHand.getItemMeta().hasCustomModelData()
                && inHand.getItemMeta().getCustomModelData() == UnoConstants.CUSTOM_MODEL_DATA;
    }
}
