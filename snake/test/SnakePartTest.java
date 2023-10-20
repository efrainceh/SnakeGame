package snake.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import snake.Direction;
import snake.SnakePart;

class SnakePartTest {

	@Test
	void testSnakePartStringDirection() {
		
		String imagePath = "/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testPart/snakebody/snakebody_01_up.png";
		Direction direction = Direction.UP;
		
		SnakePart part = new SnakePart(imagePath, direction);
		
		assertEquals(direction, part.getDirection());
		assertEquals(0, part.getRow());
		assertEquals(0, part.getCol());
		assertFalse(part.isCollision());
		
	}

	@Test
	void testSnakePartSnakePart() {
		
		String imagePath = "/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testPart/snakebody/snakebody_01_up.png";
		Direction direction = Direction.UP;
		
		SnakePart part = new SnakePart(imagePath, direction);
		SnakePart partCopy = new SnakePart(part);
		
		assertEquals(part.getDirection(), partCopy.getDirection());
		assertEquals(part.getRow(), partCopy.getRow());
		assertEquals(part.getCol(), partCopy.getCol());
		assertEquals(part.isCollision(), partCopy.isCollision());
		assertEquals(part.getImg(), partCopy.getImg());
		
	}

}
