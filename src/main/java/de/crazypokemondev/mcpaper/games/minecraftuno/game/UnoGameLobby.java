package de.crazypokemondev.mcpaper.games.minecraftuno.game;

import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.gui.LobbyMenu;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.LobbyMenuPlayer;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoGame;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.*;

public class UnoGameLobby {
    private final Set<LobbyMenuPlayer> players = new HashSet<>();

    private boolean running;

    public void join(Player player) {
        if (this.running) return;
        LobbyMenu lobbyMenu = new LobbyMenu(players, this);
        players.add(new LobbyMenuPlayer(player, false, lobbyMenu));
        player.openInventory(lobbyMenu.getInventory());
        players.forEach(lobbyPlayer -> lobbyPlayer.getLobbyMenu().update());
    }

    public void spectate(Player player) {
        // TODO: open GUI to spectate UNO game
    }

    public void start() {
        if (players.stream().anyMatch(player -> !player.isReady()) || players.size() < 2 || players.size() > 10) return;
        List<McUnoPlayer> mcUnoPlayers = new ArrayList<>();
        for (LobbyMenuPlayer player : players) {
            mcUnoPlayers.add(new McUnoPlayer(player.getPlayer()));
        }
        McUnoGame junoGame = new McUnoGame(mcUnoPlayers.toArray(new McUnoPlayer[0]));
        Bukkit.getServer().getScheduler().runTaskAsynchronously(MinecraftUno.INSTANCE, junoGame::play);
        this.running = true;
    }

    public boolean isRunning() {
        return running;
    }
}
