package board.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import board.Tile;
import board.TileManager;

@TestInstance(Lifecycle.PER_CLASS)
class TileManagerTest {
	
	TileManager tileManager;
	
	@BeforeAll
	void setup() {
		
		String testFolder = "testFiles/testTile";
		String suffix = "png";
		tileManager = new TileManager(testFolder, suffix);
		
	}

	@Test
	void testScaleImages() {
		
		int width = 32;
		int height = 64;
		tileManager.scaleImages(width, height);
		
		Tile tile = tileManager.getTile(0);
		
		assertEquals(width, tile.getImg().getWidth());
		assertEquals(height, tile.getImg().getHeight());
		
	}

	@Test
	void testGetTile() {
		
		Tile test_grass = new Tile("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testTile/10_test_grass.png", false);
		Tile test_water = new Tile("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testTile/13_test_water.png", true);
		
		assertEquals(test_grass, tileManager.getTile(0));
		assertEquals(test_water, tileManager.getTile(3));
		
	}

}
