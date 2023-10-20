package food;

import java.util.ArrayList;

import base.Manager;


public class FoodManager extends Manager {

    private final ArrayList<Food> foods;
    
    public FoodManager(String folderPath, String imgSuffix) {
    	
    	foods = loadFoods(folderPath, imgSuffix);
    	
    }
    
    public Food getFood(int foodIx) { return foods.get(foodIx); }
    
    public int numberOfFoods() { return foods.size(); }
    
    @Override
    public void scaleImages(int width, int height) {
        
        // SCALING THE IMAGES INSIDE EACH FOOD
        for (Food food : foods) {
        	
            food.setImg(scaleImage(food.getImg(), width, height));
            
        }

    }
    
    private ArrayList<Food> loadFoods(String folderPath, String imgSuffix) {
    	
    	// GET EACH IMAGE PATH AND DATA FROM THE FOLDER
    	ArrayList<String> imgPaths = loadFilePaths(folderPath, imgSuffix);
    	ArrayList<String[]> imgData = loadImgData(folderPath, "txt");

        ArrayList<Food> tempFoods = new ArrayList<Food>();
        
        for (int foodIx = 0; foodIx < imgPaths.size(); foodIx++) {
        	
        	String[] data = imgData.get(foodIx);
        	int points = Integer.parseInt(data[1]);
        	tempFoods.add(new Food(imgPaths.get(foodIx), points));
        
        }
        
        return tempFoods;

    }
    
    
    
}
