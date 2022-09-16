package de.crazypokemondev.mcpaper.games.minecraftuno.juno;

import com.github.markozajc.juno.decks.impl.UnoStandardDeck;
import com.github.markozajc.juno.game.UnoControlledGame;
import com.github.markozajc.juno.game.UnoWinner;
import com.github.markozajc.juno.rules.pack.impl.UnoOfficialRules;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ArmorStandHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.IconHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class McUnoGame extends UnoControlledGame {
    private final BukkitScheduler scheduler;
    private final ArmorStand armorStand;

    public McUnoGame(ArmorStand armorStand, McUnoPlayer... players) {
        super(UnoStandardDeck.getDeck(), 5, UnoOfficialRules.getPack(), players);
        this.armorStand = armorStand;
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public void onEvent(@NotNull String format, Object @NotNull ... arguments) {
        scheduler.runTask(MinecraftUno.INSTANCE, () -> {
                getPlayers().forEach(player -> {
                    McUnoPlayer unoPlayer = (McUnoPlayer) player;
                    unoPlayer.getMcPlayer().sendMessage(String.format(format, arguments));
                    unoPlayer.getUnoScreen().update();
                });
            armorStand.setItem(EquipmentSlot.HEAD, IconHelper.fromCard(getTopCard()));
        });
    }

    @NotNull
    @Override
    public UnoWinner play() {
        getPlayers().forEach(player -> {
            McUnoPlayer unoPlayer = ((McUnoPlayer) player);
            unoPlayer.initUnoScreen(this);
        });
        scheduler.runTask(MinecraftUno.INSTANCE, () -> getPlayers().forEach(player -> {
            McUnoPlayer unoPlayer = ((McUnoPlayer) player);
            Player mcPlayer = unoPlayer.getMcPlayer();
            mcPlayer.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            mcPlayer.openInventory(unoPlayer.getUnoScreen().getInventory());
        }));
        UnoWinner winner = super.play();
        getPlayers().forEach(player -> {
            McUnoPlayer unoPlayer = ((McUnoPlayer) player);
            unoPlayer.getUnoScreen().update();
        });
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

        scheduler.runTask(MinecraftUno.INSTANCE,
                () -> armorStand.setItem(EquipmentSlot.HEAD, ItemHelper.createUnoDeckArmorStand()));

        return winner;
    }
}
