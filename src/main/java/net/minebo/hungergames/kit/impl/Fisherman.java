package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class Fisherman extends Kit {

    public Fisherman() { register(); }

    @Override
    public KitType getKitType() { return KitType.FISHERMAN; }

    @Override
    public Material getIcon() { return Material.FISHING_ROD; }

    @Override
    public String getName() { return "Fisherman"; }

    @Override
    public String getDescription() { return "You start the game with a fishing rod that can get 3 cooked fish, and reel players in."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack rod = new ItemBuilder(Material.FISHING_ROD).setName(ChatColor.BLUE + "Reel Rod").build();
        ItemStack fish = new ItemBuilder(Material.COOKED_COD).setSize(3).setName(ChatColor.GOLD + "Cooked Fish").build();
        return List.of(rod, fish);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getCaught() instanceof Player) {
            Player caught = (Player) e.getCaught();
            Vector dir = p.getLocation().toVector().subtract(caught.getLocation().toVector()).normalize().multiply(0.9);
            caught.setVelocity(dir);
            p.sendMessage(ChatColor.BLUE + "You reeled in " + caught.getName());
        }
    }
}