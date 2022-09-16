package de.crazypokemondev.mcpaper.games.minecraftuno.helpers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class ArmorStandHelper {
    public static String getScoreboardTag(Location blockLoc) {
        return String.format("uno_game_%d_%d_%d",
                blockLoc.getBlockX(),
                blockLoc.getBlockY(),
                blockLoc.getBlockZ());
    }


    public static void createArmorStand(World world, Location blockLoc) {
        Location armorStandLoc = blockLoc.clone().add(0.5, -1.1, 1.25);

        ArmorStand armorStand = (ArmorStand)world.spawnEntity(armorStandLoc, EntityType.ARMOR_STAND);
        armorStand.setHeadPose(new EulerAngle(-Math.PI / 2, 0, 0));
        armorStand.setItem(EquipmentSlot.HEAD, ItemHelper.createUnoDeckArmorStand());

        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setPersistent(true);
        armorStand.setSilent(true);

        armorStand.addScoreboardTag("uno_game");
        armorStand.addScoreboardTag(getScoreboardTag(blockLoc));
    }

    public static void removeArmorStands(World world, Location blockLoc) {
        Collection<ArmorStand> entities = getArmorStands(world, blockLoc);
        entities.forEach(Entity::remove);
    }

    @NotNull
    public static Collection<ArmorStand> getArmorStands(World world, Location blockLoc) {
        Collection<ArmorStand> entities = world.getNearbyEntitiesByType(ArmorStand.class, blockLoc, 20);

        String scoreTag = getScoreboardTag(blockLoc);
        return entities.stream()
                .filter(stand -> stand.getScoreboardTags().contains(scoreTag))
                .collect(Collectors.toList());
    }

    @Nullable
    public static ArmorStand getAnyArmorStand(World world, Location blockLoc) {
        return getArmorStands(world, blockLoc).stream().findFirst().orElse(null);
    }

}
