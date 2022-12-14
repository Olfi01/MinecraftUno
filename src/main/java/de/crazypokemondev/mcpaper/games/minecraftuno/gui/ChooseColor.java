package de.crazypokemondev.mcpaper.games.minecraftuno.gui;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.UnoCardColor;
import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.IconHelper;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.BackButton;
import xyz.janboerman.guilib.api.menu.ItemButton;
import xyz.janboerman.guilib.api.menu.MenuHolder;

import java.util.Iterator;

public class ChooseColor extends MenuHolder<MinecraftUno> {
    private final McUnoPlayer player;

    public ChooseColor(McUnoPlayer player) {
        super(MinecraftUno.INSTANCE, 54, "Choose color:");
        setButton(10, new ChooseColorButton(UnoCardColor.RED, new ItemBuilder(Material.RED_WOOL).name("Red")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build(), player.getUnoScreen()));
        setButton(12, new ChooseColorButton(UnoCardColor.GREEN, new ItemBuilder(Material.GREEN_WOOL).name("Green")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build(), player.getUnoScreen()));
        setButton(14, new ChooseColorButton(UnoCardColor.BLUE, new ItemBuilder(Material.BLUE_WOOL).name("Blue")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build(), player.getUnoScreen()));
        setButton(16, new ChooseColorButton(UnoCardColor.YELLOW, new ItemBuilder(Material.YELLOW_WOOL).name("Yellow")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build(), player.getUnoScreen()));
        this.player = player;

        Iterator<UnoCard> cards = player.getCards().iterator();
        for (int i = 27; i < 54; i++) {
            if (!cards.hasNext()) break;
            setButton(i, new ItemButton<ChooseColor>(IconHelper.fromCard(cards.next())));
        }
    }

    private class ChooseColorButton extends BackButton<MinecraftUno> {
        private final UnoCardColor color;

        public ChooseColorButton(UnoCardColor color, ItemStack icon, UnoScreen back) {
            super(color.toString(), back::getInventory);
            this.color = color;
            setIcon(icon);
        }

        @Override
        public void onClick(MenuHolder<MinecraftUno> menuHolder, InventoryClickEvent event) {
            player.sendColorCallback(color);
            super.onClick(menuHolder, event);
        }
    }
}
