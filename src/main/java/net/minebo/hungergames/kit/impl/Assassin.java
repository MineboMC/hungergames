package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Assassin extends Kit {

    private final Map<Player, Integer> index = new ConcurrentHashMap<>();
    private final Map<Player, Long> cooldown = new ConcurrentHashMap<>();

    public Assassin() {
        register();
    }

    @Override
    public KitType getKitType() {
        return KitType.ASSASSIN;
    }

    @Override
    public Material getIcon() {
        return Material.COMPASS;
    }

    @Override
    public String getName() {
        return "Assassin";
    }

    @Override
    public String getDescription() {
        return "Cycle your compass through targets to pick the perfect enemy.";
    }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack sword = new ItemBuilder(Material.IRON_SWORD).setName(ChatColor.DARK_PURPLE + "Assassin's Blade").build();
        ItemStack compass = new ItemBuilder(Material.COMPASS).setName(ChatColor.GRAY + "Target Compass").build();
        return List.of(sword, compass);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        if (e.getItem() == null || e.getItem().getType() != Material.COMPASS) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        long now = System.currentTimeMillis();
        if (cooldown.getOrDefault(p, 0L) > now) {
            p.sendMessage(ChatColor.YELLOW + "Compass is still cycling...");
            return;
        }

        List<Player> candidates = new ArrayList<>();
        for (Player o : Bukkit.getOnlinePlayers()) {
            if (o.equals(p)) continue;
            if (o.getWorld().equals(p.getWorld()) &&
                    (o.getGameMode() == GameMode.SURVIVAL || o.getGameMode() == GameMode.ADVENTURE)) {
                candidates.add(o);
            }
        }

        if (candidates.isEmpty()) {
            p.sendMessage(ChatColor.GRAY + "No targets available.");
            return;
        }

        int i = index.getOrDefault(p, -1);
        i = (i + 1) % candidates.size();
        index.put(p, i);
        Player target = candidates.get(i);
        p.setCompassTarget(target.getLocation());
        p.sendMessage(ChatColor.GREEN + "Compass now points to " + target.getName());
        cooldown.put(p, now + 1000); // 1s cooldown
    }
}