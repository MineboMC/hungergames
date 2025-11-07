package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
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
import java.util.concurrent.TimeUnit;

public class Fisherman extends Kit {

    public static Cooldown cooldown = new Cooldown();

    public Fisherman() {
        HungerGames.getInstance().getCooldownHandler().registerCooldown("Fisherman", cooldown);
    }

    @Override
    public KitType getKitType() { return KitType.FISHERMAN; }

    @Override
    public Material getIcon() { return Material.FISHING_ROD; }

    @Override
    public String getName() { return "Fisherman"; }

    @Override
    public String getDescription() { return "Reel players in with your rod and get some cooked fish."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack rod = new ItemBuilder(Material.FISHING_ROD).setName(ChatColor.BLUE + "Rod").build();
        ItemStack fish = new ItemBuilder(Material.COOKED_COD).setSize(3).setName(ChatColor.GOLD + "Cooked Fish").build();
        return List.of(rod, fish);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;

        if (cooldown != null && cooldown.onCooldown(p)) {
            p.sendMessage(ChatColor.RED + "You can't use this for " + ChatColor.BOLD + cooldown.getRemaining(p));
            return;
        }

        if (e.getCaught() instanceof Player) {
            Player caught = (Player) e.getCaught();
            Vector dir = p.getLocation().toVector().subtract(caught.getLocation().toVector()).normalize().multiply(1.0);
            caught.setVelocity(dir);
            p.sendMessage(ChatColor.BLUE + "You reeled in " + caught.getName());
            if (cooldown != null) cooldown.applyCooldown(p, 5, TimeUnit.SECONDS, HungerGames.getInstance());
        }
    }
}