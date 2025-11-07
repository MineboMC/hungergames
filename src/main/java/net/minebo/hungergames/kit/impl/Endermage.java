package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Endermage extends Kit {

    private final Map<UUID, Location> portals = new HashMap<>();

    public Endermage() { register(); }

    @Override
    public KitType getKitType() { return KitType.ENDERMAGE; }

    @Override
    public Material getIcon() { return Material.ENDER_PEARL; }

    @Override
    public String getName() { return "Endermage"; }

    @Override
    public String getDescription() { return "Summon your portal, you and people above or below is dragged to that spot"; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack pearl = new ItemBuilder(Material.ENDER_PEARL).setSize(4).setName(ChatColor.DARK_PURPLE + "Ender Portal Pearl").build();
        ItemStack staff = new ItemBuilder(Material.BLAZE_ROD).setName(ChatColor.LIGHT_PURPLE + "Endermage Staff").build();
        return List.of(pearl, staff);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack it = e.getItem();
        if (it == null) return;

        Material m = it.getType();
        if (m == Material.BLAZE_ROD) {
            portals.put(p.getUniqueId(), p.getLocation().clone());
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Endermage portal set.");
        } else if (m == Material.ENDER_PEARL) {
            Location portal = portals.get(p.getUniqueId());
            if (portal == null) {
                p.sendMessage(ChatColor.GRAY + "No portal set. Right-click with your staff to set one.");
                return;
            }
            double portalY = portal.getY();
            for (Player other : p.getWorld().getPlayers()) {
                if (other.equals(p)) continue;
                double oy = other.getLocation().getY();
                if (oy > portalY + 1 || oy < portalY - 1) {
                    other.teleport(portal.clone().add(0.5, 0.1, 0.5));
                }
            }
            p.teleport(portal.clone().add(0.5, 0.1, 0.5));
            p.sendMessage(ChatColor.DARK_PURPLE + "Portal summoned. Nearby players dragged in.");
        }
    }
}