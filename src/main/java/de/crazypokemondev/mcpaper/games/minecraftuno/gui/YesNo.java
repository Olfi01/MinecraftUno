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

public class YesNo extends MenuHolder<MinecraftUno> {

    private final McUnoPlayer player;

    public YesNo(McUnoPlayer player, UnoCard card) {
        super(MinecraftUno.INSTANCE, InventoryType.HOPPER, "Place drawn card?");
        this.player = player;

        setButton(1, new BooleanButton(true, new ItemBuilder(Material.GREEN_CONCRETE).name("Yes")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build()));
        setButton(3, new BooleanButton(false, new ItemBuilder(Material.RED_CONCRETE).name("No")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA)).build()));
        setButton(2, new ItemButton<YesNo>(IconHelper.fromCard(card)));
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
