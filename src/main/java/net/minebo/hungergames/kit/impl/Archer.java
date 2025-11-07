package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class Archer extends Kit {

    public Archer() {
        register();
    }

    @Override
    public KitType getKitType() {
        return KitType.ARCHER;
    }

    @Override
    public Material getIcon() {
        return Material.BOW;
    }

    @Override
    public String getName() {
        return "Archer";
    }

    @Override
    public String getDescription() {
        return "Start with a bow and 10 arrows.";
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack bow = new ItemBuilder(Material.BOW).addEnchantment(Enchantment.POWER, 1).setName(ChatColor.GREEN + "Starter Bow").build();
        ItemStack arrows = new ItemBuilder(Material.ARROW).setSize(10).setName(ChatColor.YELLOW + "Arrows").build();
        return List.of(bow, arrows);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getDamager();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player shooter = (Player) arrow.getShooter();
        if (!hasKitOn(shooter)) return;

        // small bonus damage for archer arrows
        e.setDamage(e.getDamage() + 1.5);
    }
}