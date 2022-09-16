package de.crazypokemondev.mcpaper.games.minecraftuno.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.game.UnoGameLobby;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ArmorStandHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.BlockPos;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getHand() != EquipmentSlot.HAND
                || event.getPlayer().isSneaking())
            return;
        Block clicked = event.getClickedBlock();
        PersistentDataContainer customBlockData = new CustomBlockData(Objects.requireNonNull(clicked),
                MinecraftUno.INSTANCE);
        if (customBlockData.has(UnoConstants.NAMESPACED_KEY)) {
            event.setUseItemInHand(Event.Result.DENY);
            UUID worldUid = clicked.getWorld().getUID();
            BlockPos blockPos = new BlockPos(clicked);
            MinecraftUno plugin = MinecraftUno.INSTANCE;
            if (!plugin.lobbies.containsKey(worldUid)) {
                plugin.lobbies.put(worldUid, new HashMap<>());
            }
            if (!plugin.lobbies.get(worldUid).containsKey(blockPos)) {
                plugin.lobbies.get(worldUid).put(blockPos, new UnoGameLobby(ArmorStandHelper.getAnyArmorStand(
                        event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation())));
            }
            UnoGameLobby lobby = plugin.lobbies.get(worldUid).get(blockPos);
            lobby.join(event.getPlayer());
        }
    }
}
