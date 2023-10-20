package game.modes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

import board.Board;
import customComponents.GameTimer;
import food.Food;
import food.FoodDispenser;
import game.game.Game;
import game.game.GameComponents;
import game.game.GameScores;
import game.game.GameSettings;
import game.game.GameState;
import game.game.TextWindow;
import handlers.SoundManager;
import handlers.SoundManager.Sound;
import snake.BaseSnake;
import snake.Snake;
import snake.SnakePart;

public abstract class BaseMode implements Runnable {
	
	// RUN SETTINGS
	protected final double gameDelta = 1;
    protected double delta = 0;
    protected long lastTime = 0;
    protected long currentTime = 0;
    protected double drawInterval;
    
    // PLAYER VARIABLES
    protected String[] snakeOneKeys = {"UP", "DOWN", "RIGHT", "LEFT"};
    protected int snakeOneStartRow = 3;
    protected int snakeOneStartCol = 8;
    
    // GAME VARIABLES
    protected Game game;
    protected Thread gameThread;
    protected GameSettings gameSettings;
    protected GameState gameState;
    @Getter protected GameComponents gameComponents;
    @Getter @Setter protected GameScores gameScores;
    @Getter protected TextWindow window;
    
    // COMPONENTS STATUS
    protected ArrayList<Boolean> snakeFoodStatus = new ArrayList<Boolean>();
         
    // TOOLS
    protected Random random = new Random();
    protected SoundManager soundManager = new SoundManager();
    
    public BaseMode(Game game, GameSettings gameSettings) {
    	
    	this.game = game;
    	this.gameSettings = gameSettings;
    	this.gameState = GameState.HOLD;
    	this.gameComponents = loadComponents();
    	this.gameScores = loadGameScores(this.gameComponents);
    	this.window = loadWindow(this.gameComponents);
    	this.drawInterval = 1000000000/gameSettings.getFBS();  
    		
    }
    
    protected abstract GameComponents loadComponents();
    
    protected abstract TextWindow loadWindow(GameComponents components);
    
    protected abstract boolean gameOver();
    
    protected abstract void setEndGame();
    
    protected abstract void drawEndWindow(GameState gameState);
    
    protected abstract void updateRunSettings();
    
    protected abstract void updateGame();
    
    
    public void start() {

        gameThread = new Thread(this);
        gameThread.start();
        
    }
    
	@Override
	public void run() {
		
		soundManager.playMusic(Sound.BACKGROUND);
        
        // GAME LOOP
        while (gameThread != null) {
        	
        	 if (gameOver()) {
             	
                 setEndGame();
                 break;
             
             }
        	
        	// ADD WAIT TIME TO DISPLAY WINDOWS AT THE BEGGINING OF EACH GAME
        	if (gameState == GameState.HOLD) {
        		
        		hold(1);
        		window.inactivate();
        		lastTime = System.nanoTime();
        		gameState = GameState.RUNNING;
        		
        	} else {
        		
        		updateRunSettings();
        		updateGame();
	
        	}
     
        }
		
	}
	
	protected Board loadBoard() {
		
		String folderMap = "static/maps";
		Board board = new Board(gameSettings.getNumberOfRows(), gameSettings.getNumberOfCols(), gameSettings.getTileSize(), folderMap);
		board.loadMap(gameSettings.getMapIndex());
		
		return board;
		
	}
	
	protected ArrayList<BaseSnake> loadSnakes(Board board) {
		
		ArrayList<BaseSnake> snakes = new ArrayList<BaseSnake>();
		Snake snake = addSnake(board, snakeOneStartRow, snakeOneStartCol, snakeOneKeys);
		snakes.add(snake);
			
		if (gameSettings.getNumberOfPlayers() == 2) {
				
			String[] snakeTwoKeys = {"W", "S", "D", "A"};
			int snakeTwoStartRow = 13;
			int snakeTwoStartCol = 8;
			snake = addSnake(board, snakeTwoStartRow, snakeTwoStartCol, snakeTwoKeys);
			snakes.add(snake);
				
		}
			
		return snakes;
		
	}
	
	protected Snake addSnake(Board board, int row, int col, String[] keys) {

        Snake snake = new Snake(board, row, col, keys);
        snakeFoodStatus.add(false);
        updateBoard(board, snake);
        
        return snake;
        
    }
	
	protected void updateBoard(Board board, BaseSnake snake) {
	    	
		// UPDATE THE POSITION OF THE TAIL IN THE SNAKE BEFORE IT MOVES
		// TO ITS NEXT POSITION
		SnakePart previousTail = snake.getPreviousTail();
	    	
		if (previousTail != null) {
	    		
			board.updateCell(previousTail.getRow(), previousTail.getCol(), false);
	    		
		}

		// UPDATE BOARD TILES AS COLLISION. SNAKE'S HEAD TILE POSITION ON THE BOARD
		// REMAINS SET AS COLLISION FALSE  
		LinkedList<SnakePart> snakeParts = snake.getSnake();
	        
		for (int partIx = 1; partIx < snakeParts.size(); partIx++) {
	        	
			SnakePart part = snakeParts.get(partIx);
			board.updateCell(part.getRow(), part.getCol(), true);
	            
		}

	}
	
	protected FoodDispenser loadFoodDispenser(Board board, ArrayList<BaseSnake> snakes) {
		
		FoodDispenser foodSeller = new FoodDispenser(gameSettings.getTileSize());
		Food food = placeFood(foodSeller, board, snakes);
		foodSeller.setFood(food);
		
		return foodSeller;
		
	}
	
    protected Food placeFood(FoodDispenser foodSeller, Board board, ArrayList<BaseSnake> snakes) {

        // FOOD IS PLACED IN A RANDOM NO COLLISION TILE. HOWEVER, OCCASIONALLY FOOD IS
        // PLACED IN THE CURRENT HEAD SNAKE POSITION (SINCE HEAD COLLISION IS SET TO FALSE). THIS
        // WHILE LOOP PREVENTS THAT
        
    	Food food = foodSeller.getNewFood(0, 0);
    	
    	do {
    		
        	int tilesPerRow = gameSettings.getNumberOfRows();
        	int tilesPerCol = gameSettings.getNumberOfCols();
            
            // GET NEW ROW AND COL COORDINATES
            int row = random.nextInt(tilesPerRow);
            int col = random.nextInt(tilesPerCol);
            
            while (board.isCollision(row, col)) {
            	
            	row = random.nextInt(tilesPerRow);
                col = random.nextInt(tilesPerCol);
                
            }
            
            // UPDATE FOOD POSITION
            food.setRow(row);
            food.setCol(col);
            
    	}
    	
    	while (foodIsInSnakeHeadTile(food, snakes));
    	
    	return food;


    }
    
    protected boolean foodIsInSnakeHeadTile(Food food, ArrayList<BaseSnake> snakes) {

        for (BaseSnake snake : snakes) {
        	
            if (food.getRow() == snake.getHeadRow() && food.getCol() == snake.getHeadCol()) {
            	
                return true;
                
            }
            
        }

        return false;
        
    }
    
    protected GameScores loadGameScores(GameComponents components) {
    	
		GameTimer timer = new GameTimer();
		ArrayList<BaseSnake> snakes = components.getSnakes();
		ArrayList<Integer> scores = new ArrayList<Integer>();
		Integer initialScore = 0;
		
		for (int ix = 0; ix < snakes.size(); ix++) {
			
			scores.add(initialScore);
			
		}
		
		return new GameScores(timer, scores);
		
	}
    
	protected boolean gameStarted() {
		
		ArrayList<BaseSnake> snakes = gameComponents.getSnakes();
		
		for (BaseSnake snake : snakes) {
		
			if (snake.isMoving()) {

				gameScores.startTimer();
				return true;
				
			}
			
		}
		
		return false;
	}
	
    protected void setGameOver(GameState gameState, Sound sound) {
    	
    	this.gameState = gameState;
    	soundManager.playSE(sound);
    	drawEndWindow(gameState);
    	game.update(gameScores, gameComponents);
        hold(1);
        gameThread = null;
        game.end(gameState, gameScores);
        
    }
    
    protected void hold(int seconds) {

        try {

            Thread.sleep(seconds * 1000);

        } catch(InterruptedException e) {

            e.printStackTrace();

        }

    }
    
    protected GameScores updateGameScores() {
    	
    	ArrayList<Integer> scores = gameScores.getScores();

        for (int snakeIx = 0; snakeIx < snakeFoodStatus.size(); snakeIx++) {
        	
            if (snakeFoodStatus.get(snakeIx)) {
            	
            	int currentScore = scores.get(snakeIx);
                int newScore = currentScore + 1;
                
                // UPDATE SCORE ARRAY
                scores.set(snakeIx, newScore);

                // RESET FOOD STATUS TO FALSE  
                snakeFoodStatus.set(snakeIx, false);
                
            }
            
        }
        
        gameScores.update(scores);
        
        return gameScores;

    }
    
    protected GameComponents updateGameComponents(boolean npcMode) {
    	
    	int snakeSize = gameComponents.getSnakes().size();
    	int start = 0;
    	int end = (npcMode ? snakeSize - 1 : snakeSize);
    	
    	return updateGameComponents(start, end);
    	
    }
    
    protected GameComponents updateGameComponents(int start, int end) {
    
    	Board board = gameComponents.getBoard();
    	ArrayList<BaseSnake> snakes = gameComponents.getSnakes();
    	FoodDispenser foodSeller = gameComponents.getFoodSeller();
    	Food food = foodSeller.getFood();
    	
    	for (int snakeIx = start; snakeIx < end; snakeIx++) {
    		
    		BaseSnake snake = snakes.get(snakeIx);
        	boolean hasEaten = snakeAte(food, snake);
            
            if (hasEaten) {
            	
            	soundManager.playSE(Sound.EAT);
                snakeFoodStatus.set(snakeIx, hasEaten);
                food = placeFood(foodSeller, board, snakes);
                foodSeller.setFood(food);
                
            }
          
            snake.update(hasEaten);	
            updateBoard(board, snake);
            
        }
    	
    	gameComponents.update(board, foodSeller, snakes);
       
        return gameComponents;
 
    }
    
    protected boolean snakeAte(Food food, BaseSnake snake) {
    	
    	return snake.getHeadRow() == food.getRow() && snake.getHeadCol() == food.getCol();
    	
    }
    
    protected boolean invalidSnakePosition(ArrayList<BaseSnake> snakes) {

        for (BaseSnake snake : snakes) {
        	
            if (snake.isOutOfPanel() || snake.collided()) {
            	
                return true;
            
            }
            
        }
        
        return false;

    }

}
