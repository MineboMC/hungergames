package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Kangaroo extends Kit {

    private final Set<UUID> canJump = new HashSet<>();

    @Override
    public KitType getKitType() { return KitType.KANGAROO; }

    @Override
    public Material getIcon() { return Material.RABBIT_FOOT; }

    @Override
    public String getName() { return "Kangaroo"; }

    @Override
    public String getDescription() { return "Double-jump by sneaking in mid-air to gain a powerful boost."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack foot = new ItemBuilder(Material.RABBIT_FOOT).setName(ChatColor.GREEN + "Kangaroo Foot").build();
        return List.of(foot);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (!e.isSneaking()) return;
        // only allow double-jump when not on ground
        if (p.isOnGround()) {
            // allow setting availability right after leaving ground
            new BukkitRunnable() {
                @Override
                public void run() { canJump.add(p.getUniqueId()); }
            }.runTaskLater(HungerGames.getInstance(), 1L);
            return;
        }
        if (!canJump.remove(p.getUniqueId())) return;
        // perform kangaroo jump
        Vector v = p.getVelocity();
        v.setY(1.0);
        v.multiply(1.0);
        p.setVelocity(v);
        p.sendMessage(ChatColor.GREEN + "Kangaroo boost!");
    }
}