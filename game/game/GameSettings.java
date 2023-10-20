package game.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GameSettings {

	// BOARD VARIABLES
	private final int tileSize;
	private final int numberOfRows;
	private final int numberOfCols;
	private int mapIndex = 0;
	
	// GAME VARIABLES
    private int numberOfPlayers = 1;
    private int FBS = 4;
    private final int adventureGoal = 1;
    
    // MODE VARIABLES
    private GameMode gameMode = GameMode.NORMAL;
    
    public void reset() {

        numberOfPlayers = 1;
        FBS = 4;
        mapIndex = 0;
        gameMode = GameMode.NORMAL;

    }


}
