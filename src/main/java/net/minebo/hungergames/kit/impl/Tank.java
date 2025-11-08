package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class Tank extends Kit {

    @Override
    public Material getIcon() { return Material.SHIELD; }

    @Override
    public String getName() { return "Tank"; }

    @Override
    public String getDescription() { return "High damage reduction and knockback resistance."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack shield = new ItemBuilder(Material.SHIELD).setName(ChatColor.DARK_RED + "Tank Shield").build();
        return List.of(shield);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!hasKitOn(p)) return;

        // Reduce incoming damage significantly
        e.setDamage(e.getDamage() * 0.6);

        // reduce knockback effect by damping velocity shortly after
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (p.isValid()) {
                    Vector v = p.getVelocity();
                    p.setVelocity(v.multiply(0.4));
                }
            }
        }.runTaskLater(net.minebo.hungergames.HungerGames.getInstance(), 1L);
    }
}