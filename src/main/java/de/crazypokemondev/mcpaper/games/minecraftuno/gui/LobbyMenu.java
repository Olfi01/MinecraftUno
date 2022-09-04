package de.crazypokemondev.mcpaper.games.minecraftuno.gui;

import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.game.UnoGameLobby;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.LobbyMenuPlayer;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.*;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class LobbyMenu extends MenuHolder<MinecraftUno> {
    private final Set<LobbyMenuPlayer> players;
    private final UnoGameLobby lobby;

    public LobbyMenu(Set<LobbyMenuPlayer> players, UnoGameLobby lobby) {
        super(MinecraftUno.INSTANCE, 27, "UNO Lobby");
        this.players = players;
        this.lobby = lobby;
        setButton(0, new ReadyButton());
        setButton(8, new CloseButton<MinecraftUno>());
        Iterator<LobbyMenuPlayer> iterator = players.iterator();
        for (int row = 1; row <= 2; row++) {
            for (int col = 2; col < 7; col++) {
                if (iterator.hasNext()) {
                    setButton(row * 9 + col, iterator.next().getPlayerButton());
                } else {
                    break;
                }
            }
        }
        setButton(26, new StartGameButton());
    }

    public void update() {
        Iterator<LobbyMenuPlayer> iterator = players.iterator();
        for (int row = 1; row <= 2; row++) {
            for (int col = 2; col < 7; col++) {
                if (iterator.hasNext()) {
                    setButton(row * 9 + col, iterator.next().getPlayerButton());
                } else {
                    unsetButton(row * 9 + col);
                }
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        leaveGame(event.getPlayer().getUniqueId());
    }

    private void leaveGame(UUID playerUid) {
        players.removeIf(player -> player.getPlayer().getUniqueId().equals(playerUid));
    }

    private class ReadyButton extends ToggleButton<LobbyMenu> {
        private static final ItemStack offStack = new ItemBuilder(Material.RED_CONCRETE).name("Not ready").build();
        private static final ItemStack onStack = new ItemBuilder(Material.GREEN_CONCRETE).name("Ready").build();
        public ReadyButton() {
            super(offStack);
        }

        @Override
        public void afterToggle(LobbyMenu menuHolder, InventoryClickEvent event) {
            UUID whoClicked = event.getWhoClicked().getUniqueId();
            players.forEach(player -> {
                if (player.getPlayer().getUniqueId().equals(whoClicked)) player.setReady(isEnabled());
            });
        }

        @Override
        public ItemStack updateIcon(LobbyMenu menuHolder, InventoryClickEvent event) {
            return isEnabled() ? onStack : offStack;
        }
    }

    private class StartGameButton extends PredicateButton<LobbyMenu> {
        private static final ItemStack canStartIcon = new ItemBuilder(Material.TIPPED_ARROW).name("Start game")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA))
                .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.JUMP)))
                .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)).build();
        private static final ItemStack cantStartIcon = new ItemBuilder(Material.TIPPED_ARROW).name("Start game")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA))
                .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL)))
                .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)).build();
        public StartGameButton() {
            super(new ItemButton<>(canStartIcon) {
                @Override
                public void onClick(LobbyMenu holder, InventoryClickEvent event) {
                    lobby.start();
                }
            }, (lobbyMenu, event) -> players.stream().allMatch(LobbyMenuPlayer::isReady) && players.size() > 1);
        }

        @Override
        public ItemStack getIcon() {
            if (!getPredicate().test(null, null)) return cantStartIcon;
            return super.getIcon();
        }
    }
}
