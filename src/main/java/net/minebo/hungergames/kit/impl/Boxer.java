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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Boxer extends Kit {

    public Boxer() { register(); }

    @Override
    public KitType getKitType() { return KitType.BOXER; }

    @Override
    public Material getIcon() { return Material.LEATHER_BOOTS; }

    @Override
    public String getName() { return "Boxer"; }

    @Override
    public String getDescription() { return "Fists do almost as much dmg as stone sword; Incoming damage slightly reduced."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack glove = new ItemBuilder(Material.LEATHER_BOOTS).addEnchantment(Enchantment.UNBREAKING, 1).setName(ChatColor.GOLD + "Boxer's Wraps").build();
        return List.of(glove);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        if (!hasKitOn(attacker)) return;

        ItemStack hand = attacker.getInventory().getItemInMainHand();
        boolean empty = hand == null || hand.getType() == Material.AIR;
        if (empty) {
            e.setDamage(Math.max(e.getDamage(), 5.0)); // approximate stone sword
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (!hasKitOn(p)) return;
        e.setDamage(e.getDamage() * 0.9); // slight reduction
    }
}