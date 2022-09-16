package de.crazypokemondev.mcpaper.games.minecraftuno.helpers;

import de.crazypokemondev.mcpaper.games.minecraftuno.gui.LobbyMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class LobbyMenuPlayer {
    private final Player player;
    private boolean ready;
    private final LobbyMenu lobbyMenu;

    private final PlayerButton playerButton = new PlayerButton();
    private final ItemStack readyIcon;
    private final ItemStack notReadyIcon;

    public LobbyMenuPlayer(Player player, boolean ready, LobbyMenu lobbyMenu) {
        this.player = player;
        this.ready = ready;
        this.lobbyMenu = lobbyMenu;
        this.readyIcon = new ItemBuilder(Material.PLAYER_HEAD).name(getPlayer().getName())
                .changeMeta((SkullMeta meta) -> meta.setOwningPlayer(getPlayer().getPlayer()))
                .addLore("Ready").build();
        this.notReadyIcon = new ItemBuilder(Material.PLAYER_HEAD).name(getPlayer().getName())
                .changeMeta((SkullMeta meta) -> meta.setOwningPlayer(getPlayer().getPlayer()))
                .addLore("Not ready").build();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        this.playerButton.setIcon(ready ? readyIcon : notReadyIcon);
    }

    public LobbyMenu getLobbyMenu() {
        return lobbyMenu;
    }

    public PlayerButton getPlayerButton() {
        return playerButton;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LobbyMenuPlayer)) return false;
        return ((LobbyMenuPlayer) obj).getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
    }

    @Override
    public int hashCode() {
        return this.getPlayer().getUniqueId().hashCode();
    }

    public class PlayerButton extends ItemButton<LobbyMenu> {
        public PlayerButton() {
            super(isReady() ? readyIcon : notReadyIcon);
        }
    }
}
