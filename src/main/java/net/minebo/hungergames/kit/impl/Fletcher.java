package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Fletcher extends Kit {

    public Fletcher() { register(); }

    @Override
    public KitType getKitType() { return KitType.FLETCHER; }

    @Override
    public Material getIcon() { return Material.FEATHER; }

    @Override
    public String getName() { return "Fletcher"; }

    @Override
    public String getDescription() { return "Gravel always drops 1 flint, chickens drops 2 feathers."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        return List.of(new ItemBuilder(Material.FLINT).setName("Starter Flint").build());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getBlock().getType() == Material.GRAVEL) {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.FLINT, 1));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntityType() == org.bukkit.entity.EntityType.CHICKEN) {
            Player killer = e.getEntity().getKiller();
            if (killer != null && hasKitOn(killer)) {
                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.FEATHER, 2));
            }
        }
    }
}