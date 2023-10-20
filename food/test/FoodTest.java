package food.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import food.Food;

class FoodTest {

	@Test
	void testFoodStringInt() {
		
		String imagePath = "/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testFood/test_food_1.png";
		int points = 20;
		
		Food food = new Food(imagePath, points);
		
		assertEquals(points, food.getPoints());
		assertEquals(0, food.getRow());
		assertEquals(0, food.getCol());
		assertFalse(food.isCollision());
		assertNotNull(food.getImg());
	
	}

	@Test
	void testFood() {
		
		Food food = new Food();
		
		assertEquals(0, food.getPoints());
		assertEquals(0, food.getRow());
		assertEquals(0, food.getCol());
		assertFalse(food.isCollision());
		
	}

}

