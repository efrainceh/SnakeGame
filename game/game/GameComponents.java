package game.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import board.Board;
import food.FoodDispenser;
import snake.BaseSnake;
import snake.SnakeNPC;


@Getter
@Setter
@RequiredArgsConstructor
public class GameComponents {
	
	@NonNull private Board board;
	@NonNull private FoodDispenser foodSeller;
	@NonNull private ArrayList<BaseSnake> snakes;
	private boolean npcMode = false;
	
	public GameComponents(Board board, FoodDispenser foodSeller, ArrayList<BaseSnake> snakes, boolean npcMode) {
		
		this.board = board;
		this.foodSeller = foodSeller;
		this.snakes = snakes;
		
		this.npcMode = npcMode;
		
	}
	
	public void update(Board board, FoodDispenser foodSeller, ArrayList<BaseSnake> snakes) {
		
		this.board = board;
		this.foodSeller = foodSeller;
		this.snakes = snakes;
		
	}
	
	public SnakeNPC getNpc() {
		
		if (npcMode) {
			
			return (SnakeNPC)snakes.get(snakes.size() - 1);
			
		} else {
			
			// Need to throw Exception
			return null;
		
		}
		
		
	}
    
    public void draw(Graphics2D g2) {
    	
    	 board.draw(g2);
         foodSeller.draw(g2);
         
         for (BaseSnake snake : snakes) {
         	
             snake.draw(g2);
         
         }
         	
    }
    
}
