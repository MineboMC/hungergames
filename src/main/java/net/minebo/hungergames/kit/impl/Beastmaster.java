package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Beastmaster extends Kit {

    public Beastmaster() {
        register();
    }

    @Override
    public KitType getKitType() {
        return KitType.BEASTMASTER;
    }

    @Override
    public Material getIcon() {
        return Material.WOLF_SPAWN_EGG;
    }

    @Override
    public String getName() {
        return "Beastmaster";
    }

    @Override
    public String getDescription() {
        return "You start the game with 3 wolf eggs and 4 bones.";
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack eggs = new ItemBuilder(Material.WOLF_SPAWN_EGG).setSize(3).setName(ChatColor.GREEN + "Wolf Egg").build();
        ItemStack bones = new ItemBuilder(Material.BONE).setSize(4).setName(ChatColor.WHITE + "Bones").build();
        return List.of(eggs, bones);
    }
}