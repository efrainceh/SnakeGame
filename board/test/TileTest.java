package board.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import board.Tile;

class TileTest {

	@Test
	void testTileStringBoolean() {
		
		String imagePath = "/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testTile/10_test_grass.png";
		boolean collision = true;
		
		Tile tile = new Tile(imagePath, collision);
		
		assertEquals(0, tile.getRow());
		assertEquals(0, tile.getCol());
		assertTrue(tile.isCollision());
	}

	@Test
	void testTile() {
		
		Tile tile = new Tile();
		
		assertEquals(0, tile.getRow());
		assertEquals(0, tile.getCol());
		assertFalse(tile.isCollision());
		
	}

}
