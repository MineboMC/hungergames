package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Frosty extends Kit {

    public Frosty() { register(); }

    @Override
    public KitType getKitType() { return KitType.FROSTY; }

    @Override
    public Material getIcon() { return Material.SNOWBALL; }

    @Override
    public String getName() { return "Frosty"; }

    @Override
    public String getDescription() { return "Run with the speed of the blizzard [Speed 2] when on snow."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack sword = new ItemBuilder(Material.IRON_SWORD).addEnchantment(Enchantment.SHARPNESS,1).setName(ChatColor.AQUA + "Frost Blade").build();
        ItemStack snow = new ItemBuilder(Material.SNOWBALL).setSize(8).setName(ChatColor.WHITE + "Snowballs").build();
        return List.of(sword, snow);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) {
            if (p.hasPotionEffect(PotionEffectType.SPEED) && !isOnSnow(p)) p.removePotionEffect(PotionEffectType.SPEED);
            return;
        }
        if (isOnSnow(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12, 1, true, false, true)); // Speed II short
        }
    }

    private boolean isOnSnow(Player p) {
        Block b = p.getLocation().getBlock();
        return b.getType() == Material.SNOW || b.getType() == Material.SNOW_BLOCK || b.getRelative(0, -1, 0).getType() == Material.SNOW;
    }
}