package net.minebo.hungergames.task;

import net.minebo.cobalt.util.ColorUtil;
import net.minebo.hungergames.HungerGames;
import net.minebo.hungergames.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

// i was vibing to "ncs music" while writing this -- kab
public class GameStartTask extends BukkitRunnable {

    int[] times = {1, 2, 3, 4, 5, 15, 30, 45}; // Times were we should announce how many seconds until we start

    @Override
    public void run() {

        switch (HungerGames.getInstance().getGame().getGameState()) {
            case WAITING: { // Waiting for players to join
                if(Bukkit.getOnlinePlayers().size() < HungerGames.getInstance().getGame().getRequiredPlayers()) {
                    Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe countdown will not start until &6" + HungerGames.getInstance().getGame().getRequiredPlayers() + " &epeople join the server."));
                } else {
                    HungerGames.getInstance().getGame().setGameState(GameState.STARTING);
                    Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe game will start in &6" + HungerGames.getInstance().getGame().getTimeTillStart() / 60 + " minutes&e."));
                }

                break;
            }

            case STARTING: { // Starting soon (countdown going on here)

                // Cancel the countdown if we dont have enough people
                if(Bukkit.getOnlinePlayers().size() < HungerGames.getInstance().getGame().getRequiredPlayers()) {
                    HungerGames.getInstance().getGame().setGameState(GameState.WAITING);
                    Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe countdown has cancelled as not enough players are online."));
                    break;
                }

                // Start the darn game
                if(HungerGames.getInstance().getGame().timeTillStart == 0) {
                    HungerGames.getInstance().getGame().start();
                    cancel();
                    return;
                }

                HungerGames.getInstance().getGame().timeTillStart -= 1;

                // Complicated math to determine if we're on a minute or not
                if(HungerGames.getInstance().getGame().getTimeTillStart() % 60 == 0) {
                    if(HungerGames.getInstance().getGame().getTimeTillStart() > 60) {
                        Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe game will start in &6" +  HungerGames.getInstance().getGame().getTimeTillStart() / 60 + " minutes&e."));
                    } else {
                        Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe game will start in &61 minute&e."));
                    }
                } else { // For those last few seconds
                    if(Arrays.stream(times).filter(i -> i == HungerGames.getInstance().getGame().getTimeTillStart()).findFirst().isPresent()) {
                        Bukkit.broadcastMessage(ColorUtil.translateColors("&eThe game will start in &6" + HungerGames.getInstance().getGame().getTimeTillStart() +  " seconds&e."));
                    }
                }

                break;

            }

            default: // Cancel if we are in another gamestate even though this should be done by the time that happens ¯_(ツ)_/¯ (i dont know what would happen)
                cancel();
                break;

        }

    }

}
