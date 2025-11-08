package net.minebo.hungergames.kit;

import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.statistics.HGProfile;
import net.minebo.hungergames.kit.impl.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Kit implements Listener {

    public static List<Kit> kits = new ArrayList<>();

    public abstract Material getIcon();
    public abstract String getName();
    public abstract String getDescription();

    // We skate by with just default items since mcpvp had no armor in their classes.
    public abstract List<ItemStack> getDefaultItems();

    public Boolean hasKitOn(Player player) {
        return HGProfile.get(player).getSelectedKit().equals(this);
    }

    public Kit get(String kitName) { return kits.stream().filter(n -> n.getName().equalsIgnoreCase(kitName)).findFirst().orElse(null); }

    public void register() {
        Kit.kits.add(this);
        Bukkit.getPluginManager().registerEvents(this, HungerGames.getInstance());
    }

    public static void registerKits() {
        new Archer().register();
        new Cannibal().register();
        new Demoman().register();
        new Doctor().register();
        new Endermage().register();
        new Fisherman().register();
        new Grandpa().register();
        new Kangaroo().register();
        new Miner().register();
        new Phantom().register();
        new Snail().register();
        new Stomper().register();
        new Switcher().register();
        new Tank().register();
        new Thor().register();
        new Timelord().register();
        new Viper().register();
    }

}
