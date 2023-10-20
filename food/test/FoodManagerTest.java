package food.test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import food.Food;
import food.FoodManager;


@TestInstance(Lifecycle.PER_CLASS)
class FoodManagerTest {
	
	FoodManager foodManager;
	
	@BeforeAll
	void setup() {
		
		String testFolder = "testFiles/testFood";
		String suffix = "png";
		foodManager = new FoodManager(testFolder, suffix);
		
	}
	
	@Test
	void testGetFood() {
		
		String imagePath = "/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testFood/test_food_1.png";
		int points = 20;
		Food food = new Food(imagePath, points);
		
		Food newFood = foodManager.getFood(0);

        assertNotNull(newFood);
        assertEquals(food, newFood);
        
	}

	@Test
	void testNumberOfFoods() {
		
		assertEquals(3, foodManager.numberOfFoods());
		
	}
	
	@Test
	void testScaleImages() {
		
		int width = 32;
		int height = 64;
		foodManager.scaleImages(width, height);
		
		Food scaledFood = foodManager.getFood(0);
		BufferedImage scaledImg = scaledFood.getImg();
		
		assertNotNull(scaledImg);
		assertEquals(width, scaledImg.getWidth());
		assertEquals(height, scaledImg.getHeight());
		
	}

}

