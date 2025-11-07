package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Fireman extends Kit {

    public Fireman() { register(); }

    @Override
    public KitType getKitType() { return KitType.FIREMAN; }

    @Override
    public Material getIcon() { return Material.WATER_BUCKET; }

    @Override
    public String getName() { return "Fireman"; }

    @Override
    public String getDescription() { return "Immune to fire and lightning damage. Start with a bucket of water."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack bucket = new ItemBuilder(Material.WATER_BUCKET).setName(ChatColor.AQUA + "Water Bucket").build();
        ItemStack flint = new ItemBuilder(Material.FLINT_AND_STEEL).setName(ChatColor.GRAY + "Flame Tool").build();
        return List.of(bucket, flint);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!hasKitOn(p)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            e.setCancelled(true);
        }
    }
}