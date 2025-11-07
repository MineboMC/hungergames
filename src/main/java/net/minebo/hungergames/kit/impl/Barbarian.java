package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Barbarian extends Kit {

    public Barbarian() {
        register();
    }

    @Override
    public KitType getKitType() {
        return KitType.BARBARIAN;
    }

    @Override
    public Material getIcon() {
        return Material.WOODEN_SWORD;
    }

    @Override
    public String getName() {
        return "Barbarian";
    }

    @Override
    public String getDescription() {
        return "You start the game with a level-up sword (sharpness increases with each kill).";
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack lvlSword = new ItemBuilder(Material.WOODEN_SWORD).addEnchantment(Enchantment.SHARPNESS, 2).setName(ChatColor.GOLD + "Tyrfing").build();
        return List.of(lvlSword);
    }
}