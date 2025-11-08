package net.minebo.hungergames.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {

    public GameState gameState;

    public Integer requiredPlayers;
    public Integer timeTillStart;

    public Game() {
        setGameState(GameState.WAITING);

        setRequiredPlayers(2);
        setTimeTillStart(5 * 60); // 5 minutes -- has to be something times 60 or wont work with the current countdown code
    }

    public void start() {

    }

}
