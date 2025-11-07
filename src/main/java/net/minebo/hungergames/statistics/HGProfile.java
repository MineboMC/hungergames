package net.minebo.hungergames.statistics;

import lombok.Getter;
import lombok.Setter;
import net.minebo.hungergames.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class HGProfile {

    public static HashMap<UUID, HGProfile> profiles = new HashMap<>();

    public String lastUsername;

    public Statistic kills = new Statistic();

    @Getter @Setter public Kit selectedKit;

    public static HGProfile get(Player player) {
        if (profiles.containsKey(player.getUniqueId())) return profiles.get(player.getUniqueId());
        return new HGProfile(player.getUniqueId());
    }

    // Constructors
    public HGProfile(String username){
        this.lastUsername = username;
    }

    public HGProfile(UUID uuid, String username){
        profiles.put(uuid, new HGProfile(username));
    }

    public HGProfile(UUID uuid) {
        profiles.put(uuid, new HGProfile(Bukkit.getPlayer(uuid).getName()));
    }

    public HGProfile(Player player){
        profiles.put(player.getUniqueId(), new HGProfile(player.getName()));
    }

}
