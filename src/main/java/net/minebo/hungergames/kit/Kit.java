package net.minebo.hungergames.kit;

import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.kit.impl.Grandpa;
import net.minebo.hungergames.statistics.HGProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public abstract class Kit implements Listener {

    public static HashMap<KitType, Kit> kits = new HashMap<>();

    public abstract KitType getKitType();

    public abstract Material getIcon();
    public abstract String getName();
    public abstract String getDescription();

    // We skate by with just default items since mcpvp had no armor in their classes.
    public abstract List<ItemStack> getDefaultItems();

    public Boolean hasKitOn(Player player) {
        return HGProfile.get(player).getSelectedKit().equals(this);
    }

    public void register() {
        Kit.kits.put(this.getKitType(), this);
        Bukkit.getPluginManager().registerEvents(this, HungerGames.getInstance());
    }

    public static void registerKits() {
        new Grandpa().register();
    }

}
