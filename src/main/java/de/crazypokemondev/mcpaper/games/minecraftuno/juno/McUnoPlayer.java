package de.crazypokemondev.mcpaper.games.minecraftuno.juno;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import com.github.markozajc.juno.game.UnoGame;
import com.github.markozajc.juno.players.UnoPlayer;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.gui.ChooseColor;
import de.crazypokemondev.mcpaper.games.minecraftuno.gui.UnoScreen;
import de.crazypokemondev.mcpaper.games.minecraftuno.gui.YesNo;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class McUnoPlayer extends UnoPlayer {
    private final Player player;
    private final BukkitScheduler scheduler;
    private UnoScreen unoScreen;
    private final Callback<UnoCard> cardCallback = new Callback<>();
    private final Callback<UnoCardColor> colorCallback = new Callback<>();
    private final Callback<Boolean> booleanCallback = new Callback<>();

    public McUnoPlayer(@NotNull Player player) {
        super(player.getName());
        this.player = player;
        this.scheduler = Bukkit.getScheduler();
    }

    @Nullable
    @Override
    public UnoCard playCard(UnoGame game) {
        unoScreen.update();
        unoScreen.setWaiting();
        synchronized (cardCallback) {
            while (!cardCallback.hasReturned()) {
                try {
                    cardCallback.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return cardCallback.get();
    }

    @NotNull
    @Override
    public UnoCardColor chooseColor(UnoGame game) {
        scheduler.runTask(MinecraftUno.INSTANCE, () -> {
            ChooseColor gui = new ChooseColor(this);
            player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            player.openInventory(gui.getInventory());
        });
        synchronized (colorCallback) {
            while (!colorCallback.hasReturned()) {
                try {
                    colorCallback.wait();
                } catch (InterruptedException e) {
                    game.endGame();
                }
            }
        }
        return colorCallback.get();
    }

    @Override
    public boolean shouldPlayDrawnCard(UnoGame game, UnoCard drawnCard) {
        scheduler.runTask(MinecraftUno.INSTANCE, () -> {
            YesNo gui = new YesNo(this, drawnCard);
            player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            player.openInventory(gui.getInventory());
        });
        synchronized (booleanCallback) {
            while (!booleanCallback.hasReturned()) {
                try {
                    booleanCallback.wait();
                } catch (InterruptedException e) {
                    game.endGame();
                }
            }
        }
        return booleanCallback.get();
    }

    public void initUnoScreen(McUnoGame game) {
        unoScreen = new UnoScreen(game, this);
    }

    public void sendCardCallback(UnoCard card) {
        cardCallback.set(card);
    }

    public void sendColorCallback(UnoCardColor color) {
        colorCallback.set(color);
    }

    public void sendBooleanCallback(boolean bool) {
        booleanCallback.set(bool);
    }

    public Player getMcPlayer() {
        return player;
    }

    public UnoScreen getUnoScreen() {
        return unoScreen;
    }
}
