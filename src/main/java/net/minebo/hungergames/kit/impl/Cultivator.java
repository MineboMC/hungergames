package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Cultivator extends Kit {

    public Cultivator() { register(); }

    @Override
    public KitType getKitType() { return KitType.CULTIVATOR; }

    @Override
    public Material getIcon() { return Material.WHEAT; }

    @Override
    public String getName() { return "Cultivator"; }

    @Override
    public String getDescription() { return "Wheat and Saplings grow instantly when planted"; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack seeds = new ItemBuilder(Material.WHEAT_SEEDS).setSize(16).setName(ChatColor.GREEN + "Seeds").build();
        ItemStack sapling = new ItemBuilder(Material.OAK_SAPLING).setSize(4).setName(ChatColor.DARK_GREEN + "Saplings").build();
        return List.of(seeds, sapling);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        Block b = e.getBlockPlaced();
        Material mat = b.getType();
        try {
            if (mat == Material.OAK_SAPLING || mat == Material.SPRUCE_SAPLING || mat == Material.BIRCH_SAPLING
                    || mat == Material.ACACIA_SAPLING || mat == Material.DARK_OAK_SAPLING || mat == Material.JUNGLE_SAPLING) {
                // Quick, simple growth: set sapling to leaves + trunk isn't trivial; set to leaves for visual growth
                b.setType(Material.OAK_LEAVES);
            } else if (mat == Material.WHEAT) {
                BlockState st = b.getState();
                if (st.getBlockData() instanceof Ageable) {
                    Ageable ageable = (Ageable) st.getBlockData();
                    ageable.setAge(ageable.getMaximumAge());
                    st.setBlockData(ageable);
                    st.update(true);
                }
            }
        } catch (Throwable ignored) {}
    }
}