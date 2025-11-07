package net.minebo.hungergames;

import lombok.Getter;
import lombok.Setter;
import net.minebo.cobalt.cooldown.CooldownHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class HungerGames extends JavaPlugin {

    @Getter @Setter public static HungerGames instance;

    @Getter @Setter public CooldownHandler cooldownHandler;

    @Override
    public void onEnable() {

        instance = this;
        cooldownHandler = new CooldownHandler(this);

    }

}
