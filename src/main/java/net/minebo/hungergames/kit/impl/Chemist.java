package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.BottleType;
import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.cobalt.util.PotionBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Chemist extends Kit {

    private final Map<UUID, BukkitRunnable> tasks = new HashMap<>();

    public Chemist() { register(); }

    @Override
    public KitType getKitType() { return KitType.CHEMIST; }

    @Override
    public Material getIcon() { return Material.POTION; }

    @Override
    public String getName() { return "Chemist"; }

    @Override
    public String getDescription() { return "Start the game with one Insta Damage II, one Poison II and one Weakness II splash potion that regenerate every 15 minutes."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack insta = new ItemBuilder(Material.SPLASH_POTION).setName(ChatColor.DARK_PURPLE + "Insta Damage II (Splash)").build();
        ItemStack poison = new ItemBuilder(Material.SPLASH_POTION).setName(ChatColor.GREEN + "Poison II (Splash)").build();
        ItemStack weakness = new ItemBuilder(Material.SPLASH_POTION).setName(ChatColor.GRAY + "Weakness II (Splash)").build();
        return List.of(insta, poison, weakness);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        givePotions(p);

        // schedule recurring refills
        tasks.computeIfAbsent(p.getUniqueId(), uuid -> new BukkitRunnable() {
            @Override
            public void run() {
                Player pl = Bukkit.getPlayer(uuid);
                if (pl == null || !hasKitOn(pl)) { this.cancel(); tasks.remove(uuid); return; }
                givePotions(pl);
                pl.sendMessage(ChatColor.DARK_PURPLE + "Your chemist potions have regenerated.");
            }
        }).runTaskTimer(HungerGames.getInstance(), 20L * 60 * 15, 20L * 60 * 15);
    }

    private void givePotions(Player p) {
        ItemStack insta = new PotionBuilder().setBasePotionType(PotionType.STRONG_HARMING).setType(BottleType.SPLASH).build();
        ItemStack poison = new PotionBuilder().setBasePotionType(PotionType.STRONG_POISON).setType(BottleType.SPLASH).build();
        ItemStack weakness = new PotionBuilder().setBasePotionType(PotionType.LONG_WEAKNESS).setType(BottleType.SPLASH).build();

        p.getInventory().addItem(insta, poison, weakness);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        BukkitRunnable r = (BukkitRunnable) tasks.remove(e.getPlayer().getUniqueId());
        if (r != null) r.cancel();
    }

}