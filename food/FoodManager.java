package food;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import board.*;
import panels.GamePanel.BoardPanel;
import utilityTool.UtilityTool;

public class FoodManager {

    final UtilityTool utilityTool = new UtilityTool();
    final static Random random = new Random();
    static ArrayList<Food> foods = loadFoods();

    BoardPanel gp;
    Board board;
    int currentFoodIx;

    public FoodManager(BoardPanel gp) {

        this.gp = gp;
        this.board = gp.getBoard();
        currentFoodIx = 0;

    }

    public Food getFood() { return foods.get(currentFoodIx); }

    private static ArrayList<Food> loadFoods() {

        ArrayList<Food> tempFoods = new ArrayList<Food>();
        tempFoods.add(new Food("static/image/food/taco.png", 20));
        return tempFoods;

    }

    public void scaleImages(int width, int height) {
        
        // SCALING THE IMAGES INSIDE EACH FOOD
        for (Food food : foods) {
            food.img = utilityTool.scaleImage(food.img, width, height);
        }

    }

    /* 
        UPDATE METHODS
    */
    
    public Food updateFood() {
        
        // SELECT NEW FOOD TYPE
        currentFoodIx = random.nextInt(foods.size());

        // GET NEW ROW AND COL COORDINATES
        int row = random.nextInt(gp.getNumberOfRows());
        int col = random.nextInt(gp.getNumberOfCols());
        while (board.isCollision(row, col)) {
            row = random.nextInt(gp.getNumberOfRows());
            col = random.nextInt(gp.getNumberOfCols());
        }

        // UPDATE FOOD POSITION
        foods.get(currentFoodIx).row = row;
        foods.get(currentFoodIx).col = col;
        
        return foods.get(currentFoodIx);
    }
 
    /* 
        DRAW METHODS
    */

    public void draw(Graphics2D g2) {

        Food currentFood = foods.get(currentFoodIx);
        int size = gp.getTileSize();
        g2.drawImage(currentFood.img, currentFood.col * size, currentFood.row * size, null);

    }

}
