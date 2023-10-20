package board.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import board.Board;


@TestInstance(Lifecycle.PER_CLASS)
class BoardTest {
	
	Board board;
	final String testFolder = "testFiles/testMap";
	final int numberOfRows = 16;
	final int numberOfCols = 16;
	final int tileSize = 32;
	final int lastMapIx = 4;
	
	@BeforeAll
	void setup() {
		
		board = new Board(numberOfRows, numberOfCols, tileSize, testFolder);
		board.loadMap(lastMapIx);
		
	}

	@Test
	void testGetNumberOfMaps() {
		
		int numberOfTestMaps = 5;
		assertEquals(numberOfTestMaps, board.getNumberOfMaps());
		
	}

	@Test
	void testIsLastMap() {
	
		Board newBoard = new Board(numberOfRows, numberOfCols, tileSize, testFolder);		
		assertFalse(newBoard.isLastMap());
		
		newBoard.loadMap(lastMapIx);
		assertTrue(newBoard.isLastMap());
		
	}

	@Test
	void testIsCollision() {
		
		
		assertTrue(board.isCollision(1, 10));
		assertFalse(board.isCollision(0, 10));	
		
	}

	@Test
	void testUpdateCell() {
		
		board.loadMap(4);
		board.updateCell(0, 10, true);
		boolean[][] b = board.getBoard();
		
		assertTrue(b[0][10]);
		
		board.updateCell(0, 10, false);
		b = board.getBoard();
		
		assertFalse(b[0][10]);
		
	}

	@Test
	void testLoadMap() {
		
		board.loadMap(4);
		
		int[][] mapTile = {
				
			{ 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 11 , 11 , 10 },
			{ 10 , 11 , 11 , 10 , 21 , 14 , 14 , 14 , 14 , 14 , 14 , 20 , 10 , 10 , 11 , 10 },
			{ 10 , 11 , 11 , 10 , 17 , 22 , 15 , 15 , 15 , 15 , 15 , 19 , 10 , 10 , 10 , 10 },
			{ 10 , 10 , 10 , 11 , 17 , 16 , 11 , 11 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 11 },
			{ 10 , 10 , 10 , 10 , 17 , 16 , 10 , 11 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 },
			{ 10 , 10 , 11 , 10 , 17 , 16 , 10 , 11 , 10 , 10 , 10 , 11 , 11 , 10 , 10 , 10 },
			{ 10 , 10 , 10 , 10 , 17 , 16 , 11 , 10 , 10 , 11 , 10 , 10 , 11 , 10 , 10 , 11 },
			{ 10 , 11 , 10 , 10 , 18 , 19 , 12 , 12 , 12 , 12 , 21 , 20 , 11 , 10 , 10 , 10 },
			{ 11 , 10 , 10 , 10 , 10 , 11 , 10 , 11 , 10 , 10 , 17 , 16 , 10 , 10 , 10 , 10 },
			{ 10 , 10 , 10 , 10 , 11 , 10 , 10 , 10 , 10 , 10 , 17 , 16 , 10 , 10 , 11 , 11 },
			{ 10 , 10 , 10 , 11 , 11 , 10 , 10 , 10 , 10 , 10 , 17 , 16 , 10 , 11 , 11 , 10 },
			{ 10 , 10 , 10 , 10 , 11 , 11 , 10 , 10 , 10 , 10 , 17 , 16 , 11 , 11 , 10 , 10 },
			{ 11 , 11 , 10 , 10 , 21 , 14 , 14 , 14 , 14 , 14 , 23 , 16 , 11 , 11 , 10 , 10 },
			{ 11 , 10 , 10 , 10 , 18 , 15 , 15 , 15 , 15 , 15 , 15 , 19 , 10 , 10 , 10 , 10 },
			{ 10 , 10 , 10 , 10 , 10 , 10 , 11 , 10 , 10 , 10 , 11 , 10 , 10 , 10 , 11 , 10 },
			{ 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 10 , 11 , 10 , 10 , 11 , 10 , 10 }
		
		};
		
		boolean[][] b = {
				
			{ false , false , false , false , false , false , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , true , true , true , true , true , true , true , true , false , false , false , false },
			{ false , false , false , false , true , true , true , true , true , true , true , true , false , false , false , false },
			{ false , false , false , false , true , true , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , true , true , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , true , true , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , true , true , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , true , true , true , true , true , true , true , true , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , true , true , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , true , true , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , true , true , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , true , true , false , false , false , false },
			{ false , false , false , false , true , true , true , true , true , true , true , true , false , false , false , false },
			{ false , false , false , false , true , true , true , true , true , true , true , true , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , false , false , false , false , false , false },
			{ false , false , false , false , false , false , false , false , false , false , false , false , false , false , false , false }
				
		};
		
		assertArrayEquals(board.getMapTileNum(), mapTile);
		assertArrayEquals(board.getBoard(), b);
		
	}
	
	
//	@Test
//	void testBoard() {
//		
//		Board board = new Board(5, 10, 8);
//		
//		assertEquals(5, board.getNumberOfRows());
//		assertEquals(10, board.getNumberOfCols());
//		assertEquals(8, board.getTileSize());
//		
//	}

}
