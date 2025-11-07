package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Berserker extends Kit {

    public Berserker() { register(); }

    @Override
    public KitType getKitType() { return KitType.BERSERKER; }

    @Override
    public Material getIcon() { return Material.IRON_AXE; }

    @Override
    public String getName() { return "Berserker"; }

    @Override
    public String getDescription() { return "When you kill someone, your bloodlust gives you strength and confusion."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack axe = new ItemBuilder(Material.IRON_AXE).addEnchantment(Enchantment.SHARPNESS, 1).setName(ChatColor.RED + "Berserker Axe").build();
        return List.of(axe);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        Player killer = dead.getKiller();
        if (killer == null) return;
        if (!hasKitOn(killer)) return;

        killer.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 20 * 6, 1)); // Strength II 6s
        killer.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 20 * 4, 0)); // Nausea 4s
        killer.sendMessage(ChatColor.RED + "Your bloodlust surges!");
    }
}