package game.modes;

import java.util.ArrayList;

import board.Board;
import food.FoodDispenser;
import game.game.Game;
import game.game.GameComponents;
import game.game.GameSettings;
import game.game.GameState;
import game.game.TextWindow;
import handlers.SoundManager.Sound;
import snake.BaseSnake;


public class AdventureMode extends BaseMode {
	
	private int adventureGoal;
	
    public AdventureMode(Game game, GameSettings gameSettings) {
    	
    	super(game, gameSettings);
    	this.adventureGoal = gameSettings.getAdventureGoal();
    	
    }
    
    @Override
	protected GameComponents loadComponents() {
	    	
    	Board board = loadBoard(); 
	    	
		// ADD USER SNAKES TO BOARD.
    	ArrayList<BaseSnake> snakes = loadSnakes(board);
	        
		// ADD FOOD TO BOARD. SNAKES NEED TO BE ADDED BEFORE 
		// FOOD SO THAT FOOD IS NOT PLACED IN A SNAKE TILE
		FoodDispenser foodSeller = loadFoodDispenser(board, snakes);
		
		return new GameComponents(board, foodSeller, snakes);
		
	}
    
    @Override
	protected TextWindow loadWindow(GameComponents components) {
    	
    	TextWindow window = new TextWindow(components.getBoard());
		String text = "LEVEL " + Integer.toString(components.getBoard().getMapIndex() + 1);;
    	window.activate(text);
    	
    	return window;
    	
    }
    
    @Override
	protected boolean gameOver() {
    	
		return invalidSnakePosition(gameComponents.getSnakes()) || tacoGoalReached();
		
	}
    
    @Override
  	protected void setEndGame() {
    	
    	soundManager.stopMusic();
    	gameScores.stopTimer();
  		
      	if (invalidSnakePosition(gameComponents.getSnakes())) {
      		
      		setGameOver(GameState.GAMEOVER, Sound.GAME_OVER);
      		
      	} 
      	
      	if (tacoGoalReached()) {
      		
      		nextLevel();
      		
      	}
  		
  	}
    
    @Override
    protected void drawEndWindow(GameState gameState) {
    	
    	window.setActive(true);
    
    	switch (gameState) {
    		
    		case GAMEOVER:
    			window.activate("GAME OVER");
    			break;
    			
    		case NEXTLEVEL:
    			window.activate("NEXT LEVEL");
    			break;
    			
    		case FINISHED:
    			window.activate("YOU WIN!");
    			break;
    			
    		default:
              // Nothing is drawn
              break;
    	}
    
    }
    
    @Override
	protected void updateRunSettings() {

        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        lastTime = currentTime;

    }

    @Override
	protected void updateGame() {

		if (delta >= gameDelta) {
	            
			if (gameStarted()) {
	        		
				boolean npcMode = false;
				gameComponents = updateGameComponents(npcMode);
	        		
			}
			
			delta = delta - gameDelta;
	    
		}
		
		gameScores = updateGameScores();
		game.update(gameScores, gameComponents);

	}
   
    private boolean tacoGoalReached() {
    	
        return gameScores.getScores().contains(adventureGoal);

    }
  
    private void nextLevel() {
    	
        // GO TO NEXT MAP, OR THE END IF IT WAS THE LAST MAP
        int numberOfMaps = gameComponents.getBoard().getNumberOfMaps();
        
        if (gameSettings.getMapIndex() < numberOfMaps - 1) {
        	
        	setNextLevel(GameState.NEXTLEVEL, Sound.NEXT_LEVEL);
        	
        } else {
        	
        	setGameOver(GameState.FINISHED, Sound.NEXT_LEVEL);
        	
        }
        
    }
    
    private void setNextLevel(GameState gameState, Sound sound) {
    	
    	this.gameState = gameState;
    	soundManager.playSE(sound);
    	drawEndWindow(gameState);
    	game.update(gameScores, gameComponents);
        hold(1);
        gameThread = null;
        game.end(gameState, gameScores);
        
    }

}
