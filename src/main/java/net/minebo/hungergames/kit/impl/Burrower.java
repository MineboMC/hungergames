package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Burrower extends Kit {

    private final Map<UUID, List<Location>> burrowedBy = new ConcurrentHashMap<>();
    private final Map<Location, Material> previous = new ConcurrentHashMap<>();

    public Burrower() {
        register();
    }

    @Override
    public KitType getKitType() { return KitType.BURROWER; }

    @Override
    public Material getIcon() { return Material.ENDER_CHEST; }

    @Override
    public String getName() { return "Burrower"; }

    @Override
    public String getDescription() { return "Create your own personal panic room in an instant!"; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack obs = new ItemBuilder(Material.OBSIDIAN).setSize(16).setName(ChatColor.DARK_GRAY + "Burrow Blocks").build();
        ItemStack shovel = new ItemBuilder(Material.IRON_SHOVEL).setName(ChatColor.GRAY + "Burrow Shovel").build();
        return List.of(obs, shovel);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (!p.isSneaking()) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // Require empty hand
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand != null && hand.getType() != Material.AIR) return;

        Location center = p.getLocation().getBlock().getLocation();
        List<Location> changed = new ArrayList<>();
        int radius = 1;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= 1; y++) {
                    Location loc = center.clone().add(x, y, z);
                    Block b = loc.getBlock();
                    if (b.getType() == Material.AIR || b.getType() == Material.GRASS_BLOCK || b.getType() == Material.DIRT) {
                        previous.put(loc, b.getType());
                        changed.add(loc);
                        b.setType(Material.OBSIDIAN);
                    }
                }
            }
        }

        burrowedBy.put(p.getUniqueId(), changed);
        p.teleport(center.clone().add(0.5, 0.1, 0.5));
        p.sendMessage(ChatColor.GREEN + "Panic room created for 25 seconds!");

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> list = burrowedBy.remove(p.getUniqueId());
                if (list == null) return;
                for (Location loc : list) {
                    Material prev = previous.remove(loc);
                    if (prev == null || prev == Material.AIR) loc.getBlock().setType(Material.AIR);
                    else loc.getBlock().setType(prev);
                }
            }
        }.runTaskLater(HungerGames.getInstance(), 20 * 25);
    }
}