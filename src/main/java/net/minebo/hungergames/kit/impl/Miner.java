package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class Miner extends Kit {

    private final Random random = new Random();

    @Override
    public Material getIcon() { return Material.IRON_PICKAXE; }

    @Override
    public String getName() { return "Miner"; }

    @Override
    public String getDescription() { return "Improved mining drops / extra ore chance when breaking ore blocks."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack pick = new ItemBuilder(Material.IRON_PICKAXE).setName(ChatColor.GRAY + "Miner's Pick").build();
        return List.of(pick);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        Block b = e.getBlock();
        Material m = b.getType();

        // Increase chance for extra drops for ores
        if (m == Material.COAL_ORE || m == Material.IRON_ORE || m == Material.GOLD_ORE ||
                m == Material.REDSTONE_ORE || m == Material.DIAMOND_ORE || m == Material.EMERALD_ORE ||
                m == Material.COPPER_ORE || m == Material.LAPIS_ORE) {
            if (random.nextDouble() < 0.35) { // 35% chance for bonus drop
                Material drop = m;
                // map ore -> item where appropriate (e.g., diamond)
                if (m == Material.DIAMOND_ORE) drop = Material.DIAMOND;
                else if (m == Material.EMERALD_ORE) drop = Material.EMERALD;
                else if (m == Material.COAL_ORE) drop = Material.COAL;
                else if (m == Material.LAPIS_ORE) drop = Material.LAPIS_LAZULI;
                else if (m == Material.REDSTONE_ORE) drop = Material.REDSTONE;
                else if (m == Material.COPPER_ORE) drop = Material.COPPER_INGOT;
                else if (m == Material.IRON_ORE) drop = Material.IRON_INGOT;
                else if (m == Material.GOLD_ORE) drop = Material.GOLD_INGOT;

                // drop bonus item naturally at block location
                b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(drop, 1));
                p.sendMessage(ChatColor.GOLD + "Lucky dig! You found extra " + drop.name().toLowerCase());
            }
        }

        // Optional: faster stone breaking could be implemented by a temporary dig speed potion in other events
    }
}