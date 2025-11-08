package net.minebo.hungergames;

import lombok.Getter;
import lombok.Setter;
import net.minebo.cobalt.cooldown.CooldownHandler;
import net.minebo.hungergames.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class HungerGames extends JavaPlugin {

    @Getter public static HungerGames instance;

    public CooldownHandler cooldownHandler;

    public Game game;

    @Override
    public void onEnable() {

        instance = this;

        setCooldownHandler(new CooldownHandler(this));

        setGame(new Game());

    }

}
