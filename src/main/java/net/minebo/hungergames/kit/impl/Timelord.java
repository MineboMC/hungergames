package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Timelord extends Kit {

    // record last-known locations of players for short rewind (per-player saved snapshot)
    private final Map<UUID, org.bukkit.Location> snapshot = new HashMap<>();

    public static Cooldown cooldown = new Cooldown();

    public Timelord() {
        HungerGames.getInstance().getCooldownHandler().registerCooldown("Timelord", cooldown);
    }

    @Override
    public Material getIcon() { return Material.CLOCK; }

    @Override
    public String getName() { return "Timelord"; }

    @Override
    public String getDescription() { return "Record a snapshot and rewind players back to it."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack clock = new ItemBuilder(Material.CLOCK).setName(ChatColor.AQUA + "Timepiece").build();
        return List.of(clock);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack it = e.getItem();
        if (it == null || it.getType() != Material.CLOCK) return;

        UUID id = p.getUniqueId();
        if (!snapshot.containsKey(id)) {
            snapshot.put(id, p.getLocation().clone());
            p.sendMessage(ChatColor.AQUA + "Snapshot recorded. Right-click again to rewind nearby players.");
            new BukkitRunnable() {
                @Override public void run() { snapshot.remove(id); }
            }.runTaskLater(HungerGames.getInstance(), 20L * 12);
            return;
        }

        if (cooldown != null && cooldown.onCooldown(p)) {
            p.sendMessage(ChatColor.RED + "You can't use this for " + ChatColor.BOLD + cooldown.getRemaining(p));
            return;
        }

        org.bukkit.Location loc = snapshot.remove(id);
        if (loc == null) { p.sendMessage(ChatColor.GRAY + "Snapshot expired."); return; }

        p.getWorld().getPlayers().stream()
                .filter(pl -> pl.getLocation().distance(loc) <= 12)
                .forEach(pl -> pl.teleport(loc.clone().add(0.5, 0.1, 0.5)));
        p.sendMessage(ChatColor.AQUA + "Nearby players rewound!");

        if (cooldown != null) cooldown.applyCooldown(p, 20, TimeUnit.SECONDS, HungerGames.getInstance()); // 20s cooldown
    }
}