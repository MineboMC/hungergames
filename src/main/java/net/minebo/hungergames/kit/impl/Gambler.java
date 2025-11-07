package net.minebo.hungergames.kit.impl;

import net.minebo.cobalt.util.ItemBuilder;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.Kit;
import net.minebo.hungergames.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Gambler extends Kit {

    private static final String META = "gambler-owner";
    private final Random random = new Random();

    public Gambler() { register(); }

    @Override
    public KitType getKitType() { return KitType.GAMBLER; }

    @Override
    public Material getIcon() { return Material.STONE_BUTTON; }

    @Override
    public String getName() { return "Gambler"; }

    @Override
    public String getDescription() { return "Place a button and get a prize. Will it help you, or will it hurt you?"; }

    @Override
    public List<ItemStack> getDefaultItems() {
        ItemStack button = new ItemBuilder(Material.STONE_BUTTON).setName(ChatColor.GOLD + "Gamble Button").build();
        ItemStack coin = new ItemBuilder(Material.GOLD_NUGGET).setSize(3).setName(ChatColor.YELLOW + "Lucky Chips").build();
        return List.of(button, coin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!hasKitOn(p)) return;
        Block b = e.getBlockPlaced();
        if (b.getType().toString().contains("BUTTON")) {
            b.setMetadata(META, new FixedMetadataValue(HungerGames.getInstance(), p.getUniqueId().toString()));
            p.sendMessage(ChatColor.GOLD + "Gambler button placed. Right-click to gamble.");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Block b = e.getClickedBlock();
        if (!b.hasMetadata(META)) return;
        String ownerStr = b.getMetadata(META).get(0).asString();
        if (ownerStr == null) return;
        Player p = e.getPlayer();
        UUID owner = UUID.fromString(ownerStr);
        if (!owner.equals(p.getUniqueId())) {
            p.sendMessage(ChatColor.GRAY + "This is not your button.");
            return;
        }
        if (!hasKitOn(p)) return;

        int r = random.nextInt(100);
        if (r < 40) {
            p.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 3));
            p.sendMessage(ChatColor.GREEN + "You won some lucky chips!");
        } else if (r < 70) {
            p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 2));
            p.sendMessage(ChatColor.GREEN + "Not bad!");
        } else if (r < 90) {
            p.sendMessage(ChatColor.YELLOW + "Nothing this time...");
        } else {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.POISON, 20 * 6, 0));
            p.sendMessage(ChatColor.RED + "Ouch! That backfired.");
        }
    }
}