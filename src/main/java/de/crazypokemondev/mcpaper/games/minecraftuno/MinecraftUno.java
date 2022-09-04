package de.crazypokemondev.mcpaper.games.minecraftuno;

import de.crazypokemondev.mcpaper.games.minecraftuno.game.UnoGameLobby;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.BlockPos;
import de.crazypokemondev.mcpaper.games.minecraftuno.listeners.PlaceBlockListener;
import de.crazypokemondev.mcpaper.games.minecraftuno.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.janboerman.guilib.GuiLibrary;
import xyz.janboerman.guilib.api.GuiListener;

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

    @Override
    public void onEnable() {
        INSTANCE = this;
        GuiLibrary guiLibrary = (GuiLibrary) getServer().getPluginManager().getPlugin("GuiLib");
        guiListener = Objects.requireNonNull(guiLibrary).getGuiListener();

        getServer().getPluginManager().registerEvents(new PlaceBlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable() {
        INSTANCE = null;

    }
}
