package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import net.minebo.hungergames.kit.data.PortalInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.*;

public class Endermage extends Kit {

    // active portal per-player
    private final Map<UUID, PortalInfo> activePortals = new HashMap<>();

    @Override
    public KitType getKitType() { return KitType.ENDERMAGE; }

    @Override
    public Material getIcon() { return Material.ENDER_PEARL; }

    @Override
    public String getName() { return "Endermage"; }

    @Override
    public String getDescription() { return "Place a temporary 2-block portal that drags nearby players; block restored after ~5s and staff returned."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack pearl = new ItemBuilder(Material.ENDER_PEARL).setSize(4).setName(ChatColor.DARK_PURPLE + "Ender Pearl").build();
        ItemStack staff = new ItemBuilder(Material.BLAZE_ROD).setName(ChatColor.LIGHT_PURPLE + "Endermage Staff").build();
        return List.of(pearl, staff);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        if (item == null || item.getType() != Material.BLAZE_ROD) return;
        Block clicked = e.getClickedBlock();
        if (clicked == null) return;

        Location bottomLoc = clicked.getLocation();
        Location topLoc = bottomLoc.clone().add(0, 1, 0);
        UUID uuid = p.getUniqueId();

        // restore existing portal if any
        PortalInfo existing = activePortals.remove(uuid);
        if (existing != null) {
            if (existing.restoreTask != null) existing.restoreTask.cancel();
            try {
                existing.bottomLoc.getBlock().setType(existing.prevBottomMat);
                existing.bottomLoc.getBlock().setBlockData(existing.prevBottomData);
            } catch (Throwable ignored) {}
            try {
                existing.bottomLoc.clone().add(0,1,0).getBlock().setType(existing.prevTopMat);
                existing.bottomLoc.clone().add(0,1,0).getBlock().setBlockData(existing.prevTopData);
            } catch (Throwable ignored) {}
            Player owner = HungerGames.getInstance().getServer().getPlayer(uuid);
            if (owner != null && owner.isOnline()) owner.getInventory().addItem(existing.savedRod);
        }

        // save previous states
        Material prevBottomMat = clicked.getType();
        BlockData prevBottomData = clicked.getBlockData().clone();
        Material prevTopMat = topLoc.getBlock().getType();
        BlockData prevTopData = topLoc.getBlock().getBlockData().clone();
        ItemStack savedRod = item.clone();

        // place portal blocks
        try { clicked.setType(Material.NETHER_PORTAL); } catch (Throwable t) { clicked.setType(Material.PURPLE_CONCRETE); }
        try { topLoc.getBlock().setType(Material.NETHER_PORTAL); } catch (Throwable t) { topLoc.getBlock().setType(Material.PURPLE_CONCRETE); }

        // remove one rod from hand
        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand.getAmount() > 1) hand.setAmount(hand.getAmount() - 1);
            else p.getInventory().setItemInMainHand(null);
        }

        // teleport players within +/-2 (X/Z)
        double px = bottomLoc.getX() + 0.5;
        double pz = bottomLoc.getZ() + 0.5;
        double py = bottomLoc.getY() + 0.5;
        for (Player other : p.getWorld().getPlayers()) {
            if (!other.isOnline()) continue;
            if (other.equals(p)) continue;
            Location ol = other.getLocation();
            double dx = Math.abs(ol.getX() - px);
            double dz = Math.abs(ol.getZ() - pz);
            if (dx <= 2.0 && dz <= 2.0) {
                other.teleport(new Location(bottomLoc.getWorld(), px, py, pz));
                other.playSound(other.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                other.spawnParticle(Particle.PORTAL, other.getLocation(), 12, 0.3, 0.6, 0.3, 0.02);
            }
        }

        // teleport owner
        p.teleport(new Location(bottomLoc.getWorld(), px, py, pz));
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        p.spawnParticle(Particle.PORTAL, p.getLocation(), 24, 0.4, 0.8, 0.4, 0.02);

        PortalInfo info = new PortalInfo(bottomLoc.clone(), prevBottomMat, prevBottomData, prevTopMat, prevTopData, savedRod);
        activePortals.put(uuid, info);

        BukkitRunnable restoreTask = new BukkitRunnable() {
            @Override public void run() {
                try { info.bottomLoc.getBlock().setType(info.prevBottomMat); info.bottomLoc.getBlock().setBlockData(info.prevBottomData); } catch (Throwable ignored) {}
                try { info.bottomLoc.clone().add(0,1,0).getBlock().setType(info.prevTopMat); info.bottomLoc.clone().add(0,1,0).getBlock().setBlockData(info.prevTopData); } catch (Throwable ignored) {}
                Player owner = HungerGames.getInstance().getServer().getPlayer(uuid);
                if (owner != null && owner.isOnline()) {
                    owner.getInventory().addItem(info.savedRod);
                    owner.playSound(owner.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8f, 1.2f);
                    owner.sendMessage(ChatColor.LIGHT_PURPLE + "Your portal returned to you.");
                }
                activePortals.remove(uuid);
            }
        };
        info.restoreTask = restoreTask;
        restoreTask.runTaskLater(HungerGames.getInstance(), 20L * 5);
    }
}