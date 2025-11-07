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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Phantom extends Kit {

    // track scheduled flight tasks and previous flight state for restoration
    private final Map<UUID, BukkitRunnable> activeFlights = new HashMap<>();
    private final Map<UUID, Boolean> previousAllowFlight = new HashMap<>();
    private final Map<UUID, Boolean> previousFlying = new HashMap<>();

    // shared cooldown instance for this kit (registered with the global handler)
    public static Cooldown cooldown = new Cooldown();

    private static final long FLIGHT_SECONDS = 5L;
    private static final long COOLDOWN_SECONDS = 15L;

    public Phantom() {
        // register this kit's cooldown with the global handler before registering events
        HungerGames.getInstance().getCooldownHandler().registerCooldown("Phantom", cooldown);
        register();
    }

    @Override
    public KitType getKitType() { return KitType.PHANTOM; }

    @Override
    public Material getIcon() { return Material.FEATHER; }

    @Override
    public String getName() { return "Phantom"; }

    @Override
    public String getDescription() { return "Right-click the Phantom Feather to gain flight for 5 seconds."; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack feather = new ItemBuilder(Material.FEATHER).setName(ChatColor.DARK_PURPLE + "Phantom Feather").build();
        return List.of(feather);
    }

    @EventHandler
    public void onUseFeather(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;

        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if (item == null || item.getType() != Material.FEATHER) return;

        // ensure the feather is the Phantom Feather (by display name) if present
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String name = item.getItemMeta().getDisplayName();
            if (!name.equals(ChatColor.DARK_PURPLE + "Phantom Feather")) {
                return;
            }
        }

        UUID uuid = p.getUniqueId();

        // if flight already active, ignore the activation
        if (activeFlights.containsKey(uuid)) {
            p.sendMessage(ChatColor.GRAY + "Phantom flight already active.");
            return;
        }

        // cooldown check
        if (cooldown != null && cooldown.onCooldown(p)) {
            p.sendMessage(ChatColor.RED + "You can't use this for " + ChatColor.BOLD + cooldown.getRemaining(p));
            return;
        }

        // save previous flight state to restore later
        previousAllowFlight.put(uuid, p.getAllowFlight());
        previousFlying.put(uuid, p.isFlying());

        // grant flight immediately
        p.setAllowFlight(true);
        p.setFlying(true);
        p.sendMessage(ChatColor.DARK_PURPLE + "You take to the air for " + FLIGHT_SECONDS + " seconds!");

        // apply cooldown
        if (cooldown != null) cooldown.applyCooldown(p, COOLDOWN_SECONDS, TimeUnit.SECONDS, HungerGames.getInstance());

        // schedule restoration after FLIGHT_SECONDS
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Player pl = HungerGames.getInstance().getServer().getPlayer(uuid);
                if (pl != null && pl.isOnline()) {
                    Boolean prevAllow = previousAllowFlight.remove(uuid);
                    Boolean prevFly = previousFlying.remove(uuid);

                    if (prevAllow != null) {
                        pl.setAllowFlight(prevAllow);
                    } else {
                        pl.setAllowFlight(false);
                    }

                    if (prevFly != null && prevFly) {
                        pl.setFlying(true);
                    } else {
                        if (pl.isFlying()) pl.setFlying(false);
                    }

                    pl.sendMessage(ChatColor.GRAY + "Your phantom flight ended.");
                }

                activeFlights.remove(uuid);
            }
        };

        activeFlights.put(uuid, task);
        task.runTaskLater(HungerGames.getInstance(), FLIGHT_SECONDS * 20L);

        e.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        BukkitRunnable task = activeFlights.remove(uuid);
        if (task != null) {
            task.cancel();
            Boolean prevAllow = previousAllowFlight.remove(uuid);
            Boolean prevFly = previousFlying.remove(uuid);
            if (prevAllow != null) p.setAllowFlight(prevAllow);
            else p.setAllowFlight(false);
            if (prevFly != null && prevFly) p.setFlying(true);
            else if (p.isFlying()) p.setFlying(false);
        }
    }
}