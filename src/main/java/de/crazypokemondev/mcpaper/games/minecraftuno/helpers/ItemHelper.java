package de.crazypokemondev.mcpaper.games.minecraftuno.helpers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;

public class ItemHelper {
    public static ItemStack createUnoDeck() {
        return new ItemBuilder(Material.RED_CARPET).name("UNO Deck")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build();
    }

    public static ItemStack createUnoDeckArmorStand() {
        return new ItemBuilder(Material.PAPER)
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA_DECK_PAPER)).build();
    }
}
