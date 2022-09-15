package de.crazypokemondev.mcpaper.games.minecraftuno;

import com.jeff_media.customblockdata.CustomBlockData;
import de.crazypokemondev.mcpaper.games.minecraftuno.game.UnoGameLobby;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.BlockPos;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import de.crazypokemondev.mcpaper.games.minecraftuno.listeners.DestroyBlockListener;
import de.crazypokemondev.mcpaper.games.minecraftuno.listeners.PlaceBlockListener;
import de.crazypokemondev.mcpaper.games.minecraftuno.listeners.PlayerInteractListener;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.janboerman.guilib.GuiLibrary;
import xyz.janboerman.guilib.api.GuiListener;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class MinecraftUno extends JavaPlugin {
    public final Map<UUID, Map<BlockPos, UnoGameLobby>> lobbies = new HashMap<>();
    private GuiListener guiListener;

    public GuiListener getGuiListener() {
        return guiListener;
    }

    public static MinecraftUno INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("uno");

    @Override
    public void onEnable() {
        INSTANCE = this;
        GuiLibrary guiLibrary = (GuiLibrary) getServer().getPluginManager().getPlugin("GuiLib");
        guiListener = Objects.requireNonNull(guiLibrary).getGuiListener();

        CustomBlockData.registerListener(this);

        getServer().getPluginManager().registerEvents(new PlaceBlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new DestroyBlockListener(), this);

        ItemStack unoDeck = ItemHelper.createUnoDeck();

        ShapedRecipe recipe = new ShapedRecipe(UnoConstants.NAMESPACED_KEY, unoDeck);
        recipe.shape("DMD", "MHM", "DMD");
        recipe.setIngredient('D', Material.COBBLED_DEEPSLATE);
        recipe.setIngredient('M', Material.MANGROVE_PLANKS);
        recipe.setIngredient('H', Material.HAY_BLOCK);
        getServer().addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        // INSTANCE = null;
    }
}
