package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class CookieMonster extends Kit {

    private final Random random = new Random();

    public CookieMonster() { register(); }

    @Override
    public KitType getKitType() { return KitType.COOKIE_MONSTER; }

    @Override
    public Material getIcon() { return Material.COOKIE; }

    @Override
    public String getName() { return "CookieMonster"; }

    @Override
    public String getDescription() { return "Find cookies in the grass - they give you food, health, or speed."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack cookie = new ItemBuilder(Material.COOKIE).setSize(3).setName(ChatColor.GOLD + "Lucky Cookie").build();
        ItemStack woodenSword = new ItemBuilder(Material.WOODEN_SWORD).setName(ChatColor.YELLOW + "Cookie Knife").build();
        return List.of(cookie, woodenSword);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        Block b = e.getBlock();
        if (b.getType() == Material.GRASS_BLOCK || b.getType() == Material.TALL_GRASS) {
            if (random.nextDouble() < 0.15) {
                ItemStack cookie = new ItemStack(Material.COOKIE, 1);
                b.getWorld().dropItemNaturally(p.getLocation(), cookie);
                p.sendMessage(ChatColor.GOLD + "You found a cookie!");
            }
        }
    }
}