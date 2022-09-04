package de.crazypokemondev.mcpaper.games.minecraftuno.gui;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.players.UnoPlayer;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.IconHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoGame;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;
import xyz.janboerman.guilib.api.menu.MenuButton;
import xyz.janboerman.guilib.api.menu.MenuHolder;

import java.util.Iterator;

public class UnoScreen extends MenuHolder<MinecraftUno> {
    private final McUnoGame game;
    private final McUnoPlayer player;
    private final ScrollButton pageButton;
    private int cardOffset = 0;
    private boolean waitingForAction = false;

    public UnoScreen(McUnoGame game, McUnoPlayer player) {
        super(MinecraftUno.INSTANCE, 54, "UNO");
        this.game = game;
        this.player = player;

        setButton(12, new DrawPileButton());
        setButton(14, new DiscardPileButton());
        Iterator<UnoPlayer> players = game.getPlayers().iterator();
        for (int i = 18; i < 27; i++) {
            if (!players.hasNext()) break;
            setButton(i, new OpponentButton((McUnoPlayer) players.next()));
        }
        Iterator<UnoCard> cards = player.getCards().iterator();
        for (int i = 0; i < cardOffset * 9; i++) {
            if (!iterator().hasNext()) break;
            cards.next();
        }
        this.pageButton = new ScrollButton(new ItemBuilder(Material.LADDER).name("1")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build(), 0);
        for (int i = 27; i < 54; i++) {
            if (!cards.hasNext()) break;
            if (i == 35) {
                setButton(i, new ScrollButton(IconHelper.getSkullUp(), -1));
            } else if (i == 44) {
                setButton(i, pageButton);
            } else if (i == 53) {
                setButton(i, new ScrollButton(IconHelper.getSkullDown(), 1));
            } else {
                setButton(i, new UnoCardButton(cards.next()));
            }
        }
    }

    public void update() {
        getButtons().forEach(this::updateButton);
    }

    private void updateButton(int i, MenuButton<?> b) {
        if (b instanceof DiscardPileButton button) {
            button.update(game.getTopCard());
        } else if (b instanceof OpponentButton button) {
            button.update();
        } else if (b instanceof UnoCardButton button) {
            int cardIndex = cardOffset * 8 + i - 27;
            if (i > 35) cardIndex--;
            if (i > 44) cardIndex--;
            if (cardIndex < player.getHandSize()) button.update(player.getHand().getCards().get(cardIndex));
        }
    }

    private class DrawPileButton extends ItemButton<UnoScreen> {
        public DrawPileButton() {
            ItemStack icon = new ItemBuilder(Material.PAPER).name("Draw")
                    .changeMeta(meta -> meta.setCustomModelData(IconHelper.DRAW_PILE_MODEL_DATA)).build();
            setIcon(icon);
        }

        @Override
        public void onClick(UnoScreen holder, InventoryClickEvent event) {
            if (!waitingForAction) return;
            player.sendCardCallback(null);
            waitingForAction = false;
        }
    }

    private class DiscardPileButton extends ItemButton<UnoScreen> {
        public DiscardPileButton() {
            super(IconHelper.fromCard(game.getTopCard()));
        }

        public void update(UnoCard card) {
            setIcon(IconHelper.fromCard(card));
        }
    }

    private static class OpponentButton extends ItemButton<UnoScreen> {
        private final McUnoPlayer player;
        private final ItemStack icon;

        public OpponentButton(McUnoPlayer player) {
            this.player = player;
            icon = new ItemBuilder(Material.PLAYER_HEAD).name(player.getName())
                    .changeMeta((SkullMeta meta) -> meta.setOwningPlayer(player.getMcPlayer()))
                    .amount(player.getHandSize()).build();
        }

        public void update() {
            icon.setAmount(player.getHandSize());
            setIcon(icon);
        }
    }

    private class ScrollButton extends ItemButton<UnoScreen> {
        private final int offset;

        public ScrollButton (ItemStack icon, int offset) {
            super(icon);
            this.offset = offset;
        }

        @Override
        public void onClick(UnoScreen holder, InventoryClickEvent event) {
            if (offset == 0 || cardOffset + offset < 0 || cardOffset + offset > (player.getHandSize() / 8) - 2) return;
            cardOffset += offset;
            ItemStack icon = pageButton.getIcon();
            icon.setAmount(cardOffset + 1);
            icon.getItemMeta().displayName(Component.text(cardOffset + 1));
            pageButton.setIcon(icon);
        }
    }

    private class UnoCardButton extends ItemButton<UnoScreen> {
        private UnoCard card;
        public UnoCardButton(UnoCard card) {
            super(IconHelper.fromCard(card));
            this.card = card;
        }

        @Override
        public void onClick(UnoScreen holder, InventoryClickEvent event) {
            if (!waitingForAction) return;
            player.sendCardCallback(card);
            waitingForAction = false;
        }

        public void update(UnoCard card) {
            this.card = card;
            setIcon(IconHelper.fromCard(card));
        }
    }

    public void setWaiting() {
        this.waitingForAction = true;
    }

}
