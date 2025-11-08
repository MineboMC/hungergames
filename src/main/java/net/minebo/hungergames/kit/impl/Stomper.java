package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Stomper extends Kit {

    @Override
    public Material getIcon() { return Material.IRON_BOOTS; }

    @Override
    public String getName() { return "Stomper"; }

    @Override
    public String getDescription() { return "Fall deals area damage to nearby players; stomper reduces their own fall damage."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS).setName(ChatColor.GRAY + "Stomp Boots").build();
        return List.of(boots);
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!hasKitOn(p)) return;
        double damage = e.getDamage();
        // Stomper deals damage to nearby players equal to (fallDamage - 2)
        double splash = Math.max(0, damage - 2.0);
        Location loc = p.getLocation();
        p.getWorld().getPlayers().stream()
                .filter(pl -> !pl.equals(p) && pl.getWorld().equals(p.getWorld()) && pl.getLocation().distance(loc) <= 3.0)
                .forEach(pl -> pl.damage(splash, p));
        // reduce stomper fall damage
        e.setDamage(Math.max(0, damage * 0.3));
        // small delay message
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(ChatColor.DARK_RED + "You stomped the ground!");
            }
        }.runTaskLater(HungerGames.getInstance(), 1L);
    }
}