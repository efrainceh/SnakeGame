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


public class NormalMode extends BaseMode{
	
	public NormalMode(Game game, GameSettings settings) {
		
		super(game, settings);
		
	}
	
	@Override
	protected GameComponents loadComponents() {
	    
		Board board = loadBoard(); 
	    	
		// ADD USER SNAKES TO BOARD
	    ArrayList<BaseSnake> snakes = loadSnakes(board);
	        
		// ADD FOOD TO BOARD. SNAKES NEED TO BE ADDED BEFORE 
		// FOOD SO THAT FOOD IS NOT PLACED IN A SNAKE TILE
		FoodDispenser foodSeller = loadFoodDispenser(board, snakes);
		
		return new GameComponents(board, foodSeller, snakes);
		
	}
	
	@Override
	protected TextWindow loadWindow(GameComponents components) {
		
		TextWindow window = new TextWindow(components.getBoard());
		String text = "LOADING...";
    	window.activate(text);
    	
    	return window;
    	
    }

	@Override
	protected boolean gameOver() {
		
		return invalidSnakePosition(gameComponents.getSnakes());
		
	}
	
	@Override
	protected void setEndGame() {
		
		setGameOver(GameState.GAMEOVER, Sound.GAME_OVER);
		
	}
	
	 @Override
	 protected void drawEndWindow(GameState gameState) {
		 
		 window.activate("GAME OVER");
		 
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
	
		gameScores= updateGameScores();
		game.update(gameScores, gameComponents);

	}

}
