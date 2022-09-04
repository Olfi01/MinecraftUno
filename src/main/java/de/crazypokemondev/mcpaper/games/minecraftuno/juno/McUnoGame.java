package de.crazypokemondev.mcpaper.games.minecraftuno.juno;

import com.github.markozajc.juno.decks.UnoDeck;
import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoWinner;
import com.github.markozajc.juno.players.UnoPlayer;
import com.github.markozajc.juno.players.impl.UnoStrategicPlayer;
import com.github.markozajc.juno.rules.pack.UnoRulePack;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.LobbyMenuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class McUnoGame extends UnoControlledGame {
    private final BukkitScheduler scheduler;

    public McUnoGame(McUnoPlayer... players) {
        super(UnoStandardDeck.getDeck(), 5, UnoOfficialRules.getPack(), players);
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public void onEvent(@NotNull String format, Object @NotNull ... arguments) {
        scheduler.runTask(MinecraftUno.INSTANCE, () ->
                getPlayers().forEach(player -> ((McUnoPlayer) player).getMcPlayer()
                        .sendMessage(String.format(format, arguments))));
    }

    @NotNull
    @Override
    public UnoWinner play() {
        // TODO: open inventory for all players. CAUTION: USE SCHEDULER! THIS IS RUNNING AS AN ASYNC TASK!
        UnoWinner winner = super.play();
        switch (winner.getEndReason()) {
            case REQUESTED:
                onEvent("The game was aborted. Maybe someone left unexpectedly?");
                break;
            case FALLBACK:
                onEvent("No more cards to draw! %s wins the game.",
                        Objects.requireNonNull(winner.getWinner()).getName());
                break;
            case VICTORY:
                onEvent("%s won the game!", Objects.requireNonNull(winner.getWinner()).getName());
                break;
            case UNKNOWN:
            default:
                onEvent("Oops, something went wrong. Please contact your server administrator.");
                break;
        }
        return winner;
    }
}
