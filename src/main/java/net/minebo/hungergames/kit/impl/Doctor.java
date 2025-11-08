package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Doctor extends Kit {

    @Override
    public Material getIcon() { return Material.GOLDEN_APPLE; }

    @Override
    public String getName() { return "Doctor"; }

    @Override
    public String getDescription() { return "Right-click players to heal them and give regeneration."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack apple = new ItemBuilder(Material.GOLDEN_APPLE).setName(ChatColor.AQUA + "Doctor's Apple").build();
        return List.of(apple);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;
        Player doctor = e.getPlayer();
        if (!hasKitOn(doctor)) return;
        Player target = (Player) e.getRightClicked();

        // heal and give regen (ally aid)
        target.setHealth(Math.min(target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue(), target.getHealth() + 6.0));
        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 1));
        doctor.sendMessage(ChatColor.GREEN + "You healed " + target.getName());
        target.sendMessage(ChatColor.GREEN + "You were healed by a doctor!");
    }
}