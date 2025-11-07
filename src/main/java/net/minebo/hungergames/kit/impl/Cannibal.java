package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Cannibal extends Kit {

    public Cannibal() { register(); }

    @Override
    public KitType getKitType() { return KitType.CANNIBAL; }

    @Override
    public Material getIcon() { return Material.ROTTEN_FLESH; }

    @Override
    public String getName() { return "Cannibal"; }

    @Override
    public String getDescription() { return "Damage done to other players is food for you. You give players food-poisoning when you hit them."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack sword = new ItemBuilder(Material.STONE_SWORD).addEnchantment(Enchantment.SHARPNESS,1).setName(ChatColor.DARK_RED + "Cannibal's Blade").build();
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

        double heal = Math.min(4.0, e.getFinalDamage() * 0.5);
        double newHealth = Math.min(attacker.getHealth() + heal, attacker.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
        attacker.setHealth(newHealth);
        victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 6, 0));
        attacker.sendMessage(ChatColor.DARK_RED + "You feed on your foe!");
    }
}