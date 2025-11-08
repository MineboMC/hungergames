package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Cannibal extends Kit {

    @Override
    public Material getIcon() { return Material.ROTTEN_FLESH; }

    @Override
    public String getName() { return "Cannibal"; }

    @Override
    public String getDescription() { return "Damage done to other players heals you; hitting players gives them food-poisoning."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack sword = new ItemBuilder(Material.STONE_SWORD).addEnchantment(org.bukkit.enchantments.Enchantment.SHARPNESS,1).setName(ChatColor.DARK_RED + "Cannibal's Blade").build();
        ItemStack rotten = new ItemBuilder(Material.ROTTEN_FLESH).setSize(2).setName(ChatColor.DARK_GREEN + "Tainted Meal").build();
        return List.of(sword, rotten);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (!hasKitOn(attacker)) return;

        double heal = Math.min(4.0, e.getDamage() * 0.5);
        attacker.setHealth(Math.min(attacker.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), attacker.getHealth() + heal));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 6, 0));
        attacker.sendMessage(ChatColor.DARK_RED + "You feed on your foe!");
    }
}