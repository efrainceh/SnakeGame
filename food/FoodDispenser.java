package food;

import java.awt.Graphics2D;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

public class FoodDispenser {
	
	private FoodManager foodManager = new FoodManager("static/image/food", "png");
	private Random random = new Random();

	@Getter @Setter private Food food;
	@Getter private int tileSize;
	
	public FoodDispenser(int tileSize) {
		
		this.tileSize = tileSize;
		foodManager.scaleImages(tileSize, tileSize);
		food = foodManager.getFood(0);
		
	}
	
	public Food getNewFood(int row, int col) {
	    	
		// SELECT NEW FOOD TYPE
		int newFoodIx = random.nextInt(foodManager.numberOfFoods());
		food = foodManager.getFood(newFoodIx);
		food.setRow(row);
		food.setCol(col);
		
		return food;
	        
	};
	
	public void draw(Graphics2D g2) {

        g2.drawImage(food.getImg(), food.getCol() * tileSize, food.getRow() * tileSize, null);

    }

}
