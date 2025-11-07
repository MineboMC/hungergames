package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Grandpa extends Kit {

    @Override
    public KitType getKitType() {
        return KitType.GRANDPA;
    }

    @Override
    public Material getIcon() {
        return Material.RED_BED;
    }

    @Override
    public String getName() {
        return "Grandpa";
    }

    @Override
    public String getDescription() {
        return "Start with a knockback 2 wooden stick.";
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        return List.of(new ItemBuilder(Material.STICK).addEnchantment(Enchantment.KNOCKBACK, 2).setName(ChatColor.GOLD + "Walking Cane").build());
    }
}
