package de.crazypokemondev.mcpaper.games.minecraftuno.gui;

import com.github.markozajc.juno.cards.UnoCard;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.IconHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.BackButton;
import xyz.janboerman.guilib.api.menu.ItemButton;
import xyz.janboerman.guilib.api.menu.MenuHolder;

import java.util.Iterator;

public class YesNo extends MenuHolder<MinecraftUno> {

    private final McUnoPlayer player;

    public YesNo(McUnoPlayer player, UnoCard card) {
        super(MinecraftUno.INSTANCE, 54, "Place drawn card?");
        this.player = player;

        setButton(12, new BooleanButton(true, new ItemBuilder(Material.GREEN_CONCRETE).name("Yes")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build()));
        setButton(14, new BooleanButton(false, new ItemBuilder(Material.RED_CONCRETE).name("No")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build()));
        setButton(13, new ItemButton<YesNo>(IconHelper.fromCard(card)));

        Iterator<UnoCard> cards = player.getCards().iterator();
        for (int i = 27; i < 54; i++) {
            if (!cards.hasNext()) break;
            setButton(i, new ItemButton<ChooseColor>(IconHelper.fromCard(cards.next())));
        }
    }

    private class BooleanButton extends BackButton<MinecraftUno> {
        private final boolean value;

        public BooleanButton(boolean value, ItemStack icon) {
            super(player.getUnoScreen()::getInventory);
            setIcon(icon);
            this.value = value;
        }

        @Override
        public void onClick(MenuHolder<MinecraftUno> menuHolder, InventoryClickEvent event) {
            player.sendBooleanCallback(value);
            super.onClick(menuHolder, event);
        }
    }
}
