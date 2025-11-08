package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class Snail extends Kit {

    @Override
    public Material getIcon() { return Material.SLIME_BALL; }

    @Override
    public String getName() { return "Snail"; }

    @Override
    public String getDescription() { return "Slow but resilient: you move slower but receive less knockback and reduced damage."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack shell = new ItemBuilder(Material.SLIME_BALL).setName(ChatColor.GRAY + "Snail Shell").build();
        return List.of(shell);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) {
            if (p.hasPotionEffect(PotionEffectType.SLOWNESS)) p.removePotionEffect(PotionEffectType.SLOWNESS);
            return;
        }
        // Give mild slowness to simulate snail speed
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 0, true, false, true));
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player target = (Player) e.getEntity();
        if (!hasKitOn(target)) return;
        // reduce knockback by damping velocity after attack
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (target.isValid()) {
                    Vector v = target.getVelocity();
                    target.setVelocity(v.multiply(0.4)); // damp knockback
                }
            }
        }.runTaskLater(HungerGames.getInstance(), 1L);
        // slightly reduce incoming damage
        e.setDamage(e.getDamage() * 0.85);
    }
}