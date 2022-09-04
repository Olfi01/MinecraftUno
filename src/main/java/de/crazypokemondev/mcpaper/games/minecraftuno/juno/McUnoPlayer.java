package de.crazypokemondev.mcpaper.games.minecraftuno.juno;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class McUnoPlayer extends UnoPlayer {
    private final Player player;

    public McUnoPlayer(@NotNull Player player) {
        super(player.getName());
        this.player = player;
    }

    @Nullable
    @Override
    public UnoCard playCard(UnoGame game) {
        return null;
    }

    @NotNull
    @Override
    public UnoCardColor chooseColor(UnoGame game) {
        return null;
    }

    @Override
    public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
        return false;
    }

    public Player getMcPlayer() {
        return player;
    }
}
