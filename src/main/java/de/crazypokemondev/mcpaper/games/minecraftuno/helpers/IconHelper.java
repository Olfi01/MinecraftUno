package de.crazypokemondev.mcpaper.games.minecraftuno.helpers;

import com.github.markozajc.juno.cards.UnoCard;
import com.github.markozajc.juno.cards.impl.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class IconHelper {
    public static final int DRAW_PILE_MODEL_DATA = 9211000;

    public static ItemStack fromCard(UnoCard card) {
        if (card == null) return null;
        return new ItemBuilder(Material.PAPER).name(card.toString())
                .changeMeta(meta -> meta.setCustomModelData(modelDataFromCard(card))).build();
    }

    private static int modelDataFromCard(UnoCard c) {
        int modelData = 9211000;
        if (c instanceof UnoNumericCard card) {
            modelData += 100;
            modelData += card.getNumber();
        } else if (c instanceof UnoReverseCard) {
            modelData += 200;
        } else if (c instanceof UnoWildCard) {
            modelData += 300;
        } else if (c instanceof UnoSkipCard) {
            modelData += 400;

        } else if (c instanceof UnoDrawCard card) {
            if (card.getAmount() == 2)
                modelData += 500;
            else
                modelData += 600;
        }
        modelData += switch (c.getColor()) {
            case RED -> 10;
            case GREEN -> 20;
            case BLUE -> 30;
            case YELLOW -> 40;
            case WILD -> 50;
        };
        return modelData;
    }

    public static ItemStack getSkull(String url, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if(url.isEmpty()) return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        headMeta.displayName(Component.text(name));
        headMeta.setCustomModelData(UnoConstants.CUSTOM_MODEL_DATA);
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack getSkullUp() {
        return getSkull("http://textures.minecraft.net/texture/9cdb8f43656c06c4e8683e2e6341b4479f157f48082fea4aff09b37ca3c6995b", "Scroll Up");
    }

    public static ItemStack getSkullDown() {
        return getSkull("http://textures.minecraft.net/texture/61e1e730c77279c8e2e15d8b271a117e5e2ca93d25c8be3a00cc92a00cc0bb85", "Scroll Down");
    }
}
