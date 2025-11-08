package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.cooldown.construct.Cooldown;
import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Thor extends Kit {

    public static Cooldown cooldown = new Cooldown();

    // Configurable behavior to mirror the LibsMcpvp example
    private static final int DEFAULT_COOLDOWN_SECONDS = 5;
    private static final boolean DO_NETHERRACK_AND_FIRE = true;
    private static final boolean PROTECT_THORER = true;
    private static final Material THOR_ITEM = Material.WOODEN_AXE; // matches Libs example (Material.WOOD_AXE)

    public Thor() {
        // register this kit's cooldown with the global handler, then register the kit listener
        HungerGames.getInstance().getCooldownHandler().registerCooldown("Thor", cooldown);
        register();
    }

    @Override
    public Material getIcon() { return Material.GOLDEN_AXE; }

    @Override
    public String getName() { return "Thor"; }

    @Override
    public String getDescription() { return "Right-click a block with Mjolnir to call lightning and optionally set netherrack/fire. (5s cooldown)"; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack hammer = new ItemBuilder(THOR_ITEM).setName(ChatColor.GOLD + "Mjolnir").build();
        return List.of(hammer);
    }

    // Prevent the caster from being damaged by lightning they summon
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LightningStrike)) return;
        if (!(event.getEntity() instanceof Player)) return;

        LightningStrike strike = (LightningStrike) event.getDamager();
        if (!strike.hasMetadata("DontHurt")) return;

        String name = strike.getMetadata("DontHurt").get(0).value().toString();
        if (((Player) event.getEntity()).getName().equals(name)) {
            event.setCancelled(true);
        }
    }

    // Activation: right-click a block with the Thor item
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player p = event.getPlayer();
        if (!hasKitOn(p)) return;

        if (event.getItem() == null || event.getItem().getType() != THOR_ITEM) return;

        // Cooldown check using the registered Cooldown instance
        if (cooldown != null && cooldown.onCooldown(p)) {
            p.sendMessage(ChatColor.RED + "You can't use this for " + ChatColor.BOLD + cooldown.getRemaining(p));
            return;
        }

        // Apply the optional netherrack/fire behaviour
        if (DO_NETHERRACK_AND_FIRE) {
            try {
                if (event.getClickedBlock().getType() != Material.BEDROCK) {
                    event.getClickedBlock().setType(Material.NETHERRACK);
                }
                if (event.getClickedBlock().getRelative(BlockFace.UP) != null) {
                    event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
                }
            } catch (Throwable ignored) {}
        }

        // Strike lightning at the highest block at the clicked block's X/Z
        var strikeLocation = event.getClickedBlock().getLocation().getWorld()
                .getHighestBlockAt(event.getClickedBlock().getLocation()).getLocation().clone().add(0, 1, 0);
        LightningStrike strike = p.getWorld().strikeLightning(strikeLocation);

        // Tag lightning so it doesn't hurt the caster (optional)
        if (PROTECT_THORER) {
            strike.setMetadata("DontHurt", new FixedMetadataValue(HungerGames.getInstance(), p.getName()));
        }

        p.sendMessage(ChatColor.GOLD + "You called down Thor's wrath!");

        // Apply cooldown to the player
        if (cooldown != null) cooldown.applyCooldown(p, DEFAULT_COOLDOWN_SECONDS, TimeUnit.SECONDS, HungerGames.getInstance());
    }
}