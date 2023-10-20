package game.modes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import PathFinder.PathFinder;
import board.Board;
import food.Food;
import food.FoodDispenser;
import game.game.Game;
import game.game.GameComponents;
import game.game.GameSettings;
import game.game.GameState;
import game.game.TextWindow;
import handlers.SoundManager.Sound;
import snake.BaseSnake;
import snake.SnakeNPC;

public class NpcMode extends BaseMode {
	
	private final double npcModeDelta = 1.5;
	private double deltaNPC = 0;
	
	// NPC PATH ALGORITHM
	PathFinder pathFinder = new PathFinder();
	
	public NpcMode(Game game, GameSettings settings) {
		
		super(game, settings);
		
	}
	
	@Override
	protected GameComponents loadComponents() {
    	
		Board board = loadBoard(); 
	    	
		// ADD USER SNAKES TO BOARD.
		ArrayList<BaseSnake> snakes = loadSnakes(board);
		
		snakes = addNpcSnake(board, snakes);
	        
		// ADD FOOD TO BOARD. SNAKES NEED TO BE ADDED BEFORE 
		// FOOD SO THAT FOOD IS NOT PLACED IN A SNAKE TILE
		FoodDispenser foodSeller = loadFoodDispenser(board, snakes);
		
		boolean npcMode = true;
		
		return new GameComponents(board, foodSeller, snakes, npcMode);
		
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
		 
		 window.setActive(true);
		 window.activate("GAME OVER");
		 
	 }
	
	@Override
	protected void updateRunSettings() {

        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        deltaNPC += (currentTime - lastTime) / drawInterval;
        lastTime = currentTime;
  
    }

	@Override
	protected void updateGame() {

		if (delta >= gameDelta) {
	            
			if (gameStarted()) {
	        	
				boolean npcMode = true;
				gameComponents = updateGameComponents(npcMode);
	        		
			}
			
			delta = delta - gameDelta;
	    
		}
		
		if (deltaNPC >= npcModeDelta) {
            
			if (gameStarted()) {
				
				gameComponents = updateNpcComponent();
	        		
			}
			
			deltaNPC = deltaNPC - npcModeDelta;
	    
		}
		
		// THIS NEEDS TO BE BEFORE updateScores(), BECAUSE updateScores() CLEARS
		// snakeFoodStatus.
 		SnakeNPC npc = gameComponents.getNpc();
 		
 		if (snakeFoodStatus.get(0) || npc.getPathToFood() == null) {
 			
 			npc.setPathToFood(newNPCPath(npc));
 			
 		}
		
		gameScores = updateGameScores();
		game.update(gameScores, gameComponents);
		
	}
	
	private ArrayList<BaseSnake> addNpcSnake(Board board, ArrayList<BaseSnake> snakes) {
		
		int snakeNpcStartRow = 13;
		int snakeNpcStartCol = 8;
		SnakeNPC npc = new SnakeNPC(board, snakeNpcStartRow, snakeNpcStartCol);
		updateBoard(board, npc);
		snakeFoodStatus.add(false);
		snakes.add(npc);
		
		return snakes;
		
	}
	
	private GameComponents updateNpcComponent() {
    	
    	Board board = gameComponents.getBoard();
    	ArrayList<BaseSnake> snakes = gameComponents.getSnakes();
    	SnakeNPC npc = gameComponents.getNpc();
    	FoodDispenser foodSeller = gameComponents.getFoodSeller();
    	Food food = foodSeller.getFood();
    	
    	boolean hasEaten = snakeAte(food, npc);
            
    	if (hasEaten) {
            	
    		soundManager.playSE(Sound.EAT);
    		snakeFoodStatus.set(snakes.size() - 1, hasEaten);
    		food = placeFood(foodSeller, board, snakes);
    		foodSeller.setFood(food);
    		// A NEW PATH NEEDS TO BE LOADED STRAIGHT AWAY, OTHERWISE THE SNAKE WON'T
    		// KNOW WHERE TO MOVE WHEN IT UPDATES
    		npc.setPathToFood(newNPCPath(npc));
                
    	}
          
    	npc.update(hasEaten);	
    	updateBoard(board, npc);
        
    	gameComponents.update(board, foodSeller, snakes);
    	
    	return gameComponents;
      
	}	
	
	private LinkedList<ArrayList<Integer>> newNPCPath(SnakeNPC npc) {
		
		// COPY THE CURRENT STATE OF THE BOARD
		boolean[][] boardCopy = Arrays.stream(gameComponents.getBoard().getBoard()).map(boolean[]::clone).toArray(boolean[][]::new);
		
		// FIND THE SHORTEST PATH TO food
		pathFinder.loadMatrix(boardCopy);
		Food food = gameComponents.getFoodSeller().getFood();
		LinkedList<ArrayList<Integer>> pathToFood = pathFinder.findPath(npc.getHeadRow(), npc.getHeadCol(), food.getRow(), food.getCol());
		            
		// DISCARD THE FIRST TILE POSITION, IT REPRESENTS THE CURRENT HEAD TILE
		pathToFood.remove();
		
		return pathToFood;
		
	}

}
