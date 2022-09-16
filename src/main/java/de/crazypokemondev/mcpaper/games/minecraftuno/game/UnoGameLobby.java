package de.crazypokemondev.mcpaper.games.minecraftuno.game;

import de.crazypokemondev.mcpaper.games.minecraftuno.MinecraftUno;
import de.crazypokemondev.mcpaper.games.minecraftuno.gui.LobbyMenu;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.LobbyMenuPlayer;
import de.crazypokemondev.mcpaper.games.minecraftuno.helpers.UnoConstants;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoGame;
import de.crazypokemondev.mcpaper.games.minecraftuno.juno.McUnoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.*;

public class UnoGameLobby {
    private final Set<LobbyMenuPlayer> players = new HashSet<>();
    private final List<McUnoPlayer> mcUnoPlayers = new ArrayList<>();
    private final StartGameButton startGameButton = new StartGameButton();
    private final ArmorStand armorStand;

    private boolean running;

    public UnoGameLobby(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public void join(Player player) {
        if (this.running){
            Optional<McUnoPlayer> present = mcUnoPlayers.stream().filter(p -> p.getMcPlayer().getUniqueId().equals(player.getUniqueId()))
                    .findAny();
            present.ifPresent(p -> player.openInventory(p.getUnoScreen().getInventory()));
        } else if (players.size() < 9) {
            LobbyMenu lobbyMenu = new LobbyMenu(players, this);
            players.add(new LobbyMenuPlayer(player, false, lobbyMenu));
            player.openInventory(lobbyMenu.getInventory());
            players.forEach(lobbyPlayer -> lobbyPlayer.getLobbyMenu().update());
        }
    }

    public void start() {
        if (players.stream().anyMatch(player -> !player.isReady()) || players.size() < 2 || players.size() > 9) return;
        mcUnoPlayers.clear();
        for (LobbyMenuPlayer player : players) {
            mcUnoPlayers.add(new McUnoPlayer(player.getPlayer()));
        }
        Collections.shuffle(mcUnoPlayers);
        McUnoGame junoGame = new McUnoGame(armorStand, mcUnoPlayers.toArray(new McUnoPlayer[0]));
        Bukkit.getServer().getScheduler().runTaskAsynchronously(MinecraftUno.INSTANCE, () -> {
            try {
                junoGame.play();
            } catch (Exception ex) {
                ex.printStackTrace();
                MinecraftUno.LOGGER.error(ex);
            }
            this.running = false;
            this.players.clear();
        });
        this.running = true;
    }

    public StartGameButton getStartGameButton() {
        return startGameButton;
    }

    public class StartGameButton extends ItemButton<LobbyMenu> {
        private static final ItemStack canStartIcon = new ItemBuilder(Material.TIPPED_ARROW).name("Start game")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA))
                .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.JUMP)))
                .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)).build();
        private static final ItemStack cantStartIcon = new ItemBuilder(Material.TIPPED_ARROW).name("Start game")
                .changeMeta(meta -> meta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA))
                .changeMeta((PotionMeta meta) -> meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL)))
                .changeMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)).build();
        public StartGameButton() {
            super(cantStartIcon);
        }

        @Override
        public void onClick(LobbyMenu holder, InventoryClickEvent event) {
            if (players.stream().allMatch(LobbyMenuPlayer::isReady) && players.size() > 1)
                start();
        }

        public void update() {
            if (players.stream().allMatch(LobbyMenuPlayer::isReady) && players.size() > 1)
                setIcon(canStartIcon);
            else
                setIcon(cantStartIcon);
        }
    }
}
