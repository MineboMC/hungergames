package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import java.util.List;

public class Switcher extends Kit {

    @Override
    public Material getIcon() { return Material.SPECTRAL_ARROW; }

    @Override
    public String getName() { return "Switcher"; }

    @Override
    public String getDescription() { return "Hit a player to swap positions with them."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack item = new ItemBuilder(Material.ENDER_PEARL).setName(ChatColor.LIGHT_PURPLE + "Switcher Pearl").build();
        return List.of(item);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player hitter = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (!hasKitOn(hitter)) return;

        Location a = hitter.getLocation().clone();
        Location b = victim.getLocation().clone();
        hitter.teleport(b.add(0, 0.2, 0));
        victim.teleport(a.add(0, 0.2, 0));
        hitter.sendMessage(ChatColor.LIGHT_PURPLE + "Swapped positions with " + victim.getName());
    }
}