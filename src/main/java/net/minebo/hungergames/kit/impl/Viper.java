package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class Viper extends Kit {

    private final Random random = new Random();

    @Override
    public KitType getKitType() { return KitType.VIPER; }

    @Override
    public Material getIcon() { return Material.SLIME_BALL; }

    @Override
    public String getName() { return "Viper"; }

    @Override
    public String getDescription() { return "Attacks poison victims on hit."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack sword = new ItemBuilder(Material.STONE_SWORD).setName(ChatColor.DARK_GREEN + "Viper Blade").build();
        return List.of(sword);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (!hasKitOn(attacker)) return;

        if (random.nextDouble() < 0.20) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 0));
            attacker.sendMessage(ChatColor.GREEN + "Your bite injected venom!");
            victim.sendMessage(ChatColor.DARK_GREEN + "You feel venom coursing through your veins...");
        }
    }

}