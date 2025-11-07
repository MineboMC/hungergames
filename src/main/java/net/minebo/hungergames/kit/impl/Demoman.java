package net.minebo.hungergames.kit.impl;

import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Demoman extends Kit {

    @Override
    public KitType getKitType() { return KitType.DEMOMAN; }

    @Override
    public Material getIcon() { return Material.TNT; }

    @Override
    public String getName() { return "Demoman"; }

    @Override
    public String getDescription() { return "Stone Pressure Plate on top of gravel = boom."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        // give a few TNTs and plates
        return List.of(new ItemStack(Material.TNT, 2), new ItemStack(Material.STONE_PRESSURE_PLATE));
    }

    @EventHandler
    public void onPhysical(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) return;
        Block plate = e.getClickedBlock();
        if (plate == null) return;
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;

        if (plate.getType() == Material.STONE_PRESSURE_PLATE && plate.getRelative(0, -1, 0).getType() == Material.GRAVEL) {
            Location loc = plate.getLocation().add(0.5, 0.5, 0.5);
            plate.getWorld().createExplosion(loc, 4.0F, false, false);
            plate.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        }
    }
}