package game.game;

import lombok.Getter;
import lombok.NonNull;

import game.modes.AdventureMode;
import game.modes.NormalMode;
import game.modes.NpcMode;
import panels.GamePanel;

public class Game {
	
	@Getter @NonNull private GamePanel gamePanel;
	@Getter @NonNull private GameSettings gameSettings;
	@Getter @NonNull private GameComponents gameComponents;
	@Getter @NonNull private GameScores gameScores;
	@Getter @NonNull private TextWindow window;
	private GameMode gameMode;
	
	// DIFFERENT GAME MODES
	private AdventureMode adventureMode; 
	private NormalMode normalMode;
	private NpcMode npcMode;
	
    public Game(GamePanel gamePanel, GameSettings gameSettings) {
    	
    	this.gamePanel = gamePanel;
    	this.gameSettings = gameSettings;
    	this.gameMode = loadGameMode(gameSettings);
    	
    }
    
    public void start() {
    	
    	switch (gameMode) {
    	
    		case NORMAL:
    			normalMode.start();
    			break;
    		
    		case ADVENTURE:
    			adventureMode.start();
    			break;
    		
    		case NPC:
    			npcMode.start();
    			break;
    	
    	}
    	
    }
    
    public void end(GameState gameState, GameScores gameScores) {
    	
    	switch (gameMode) {
    	
			case NORMAL:
				gamePanel.endGame(gameScores);
				break;
			
			case ADVENTURE:
				nextMap(gameState, gameScores);
				break;
			
			case NPC:
				gamePanel.endGame(gameScores);
				break;
		
		}
    	
    }
    
    private void nextMap(GameState gameState, GameScores gameScores) {
    	
    	switch (gameState) {
    	
    		case GAMEOVER:
    			gamePanel.endGame(gameScores);
    			break;
    			
    		case NEXTLEVEL:
    			int currentMap = gameSettings.getMapIndex();
    			gameSettings.setMapIndex(currentMap + 1);
    			gamePanel.nextMap();
    			break;
    			
    		case FINISHED:
    			gamePanel.endGame(gameScores);
    			break;
    			
    		default:
    			gamePanel.endGame(gameScores);
    			break;
    	
    	}
    	
    }
    
    public void update(GameScores gameScores, GameComponents gamecomponents) {
    	
    	switch (gameMode) {
    	
			case NORMAL:
				gamePanel.updateNormal(gameScores, gameComponents);
				break;
			
			case ADVENTURE:
				gamePanel.updateAdventure(gameScores, gameComponents);
				break;
			
			case NPC:
	    		gamePanel.updateNpc(gameScores, gameComponents);;
				break;
		
		}
    	
    }
    
    private GameMode loadGameMode(GameSettings gameSettings) {
    	
    	GameMode mode = gameSettings.getGameMode();
    	
    	switch (mode) {
    	
			case NORMAL:
				normalMode = new NormalMode(this, gameSettings);
	    		gameComponents = normalMode.getGameComponents();
	    		gameScores = normalMode.getGameScores();
	    		window = normalMode.getWindow();
				break;
			
			case ADVENTURE:
				adventureMode = new AdventureMode(this, gameSettings);
	    		gameComponents = adventureMode.getGameComponents();
	    		gameScores = adventureMode.getGameScores();
	    		window = adventureMode.getWindow();
				break;
			
			case NPC:
				npcMode = new NpcMode(this, gameSettings);
	    		gameComponents = npcMode.getGameComponents();
	    		gameScores = npcMode.getGameScores();
	    		window = npcMode.getWindow();
				break;
		
		}
    	
    	return mode;
    	
    }

}
