package snake.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import snake.BodyPart;
import snake.Direction;
import snake.SnakePart;
import snake.SnakePartManager;

@TestInstance(Lifecycle.PER_CLASS)
class SnakePartManagerTest {
	
	SnakePartManager partManager;
	
	@BeforeAll
	void setup() {
		
		String testFolder = "testFiles/testPart";
		String suffix = "png";
		partManager = new SnakePartManager(testFolder, suffix);
		
	}

	@Test
	void testScaleImages() {
		
		int width = 32;
		int height = 64;
		partManager.scaleImages(width, height);
		
		Direction direction = Direction.UP;
		
		HashMap<Direction, SnakePart> headParts = partManager.getHeadParts();
		SnakePart head = new SnakePart(headParts.get(direction));
		
		assertEquals(width, head.getImg().getWidth());
		assertEquals(height, head.getImg().getHeight());
		
		HashMap<Direction, SnakePart> bodyParts = partManager.getBodyParts();
		SnakePart body = new SnakePart(bodyParts.get(direction));
		
		assertEquals(width, body.getImg().getWidth());
		assertEquals(height, body.getImg().getHeight());
		
		HashMap<Direction, SnakePart> tailParts = partManager.getTailParts();
		SnakePart tail = new SnakePart(tailParts.get(direction));
		
		assertEquals(width, tail.getImg().getWidth());
		assertEquals(height, tail.getImg().getHeight());
			
	}

	@Test
	void testGetPart() {
		
		BodyPart part = BodyPart.HEAD;
		Direction direction = Direction.UP;
		int row = 2;
		int col = 5;
		
		SnakePart headFromMap = partManager.getHeadParts().get(direction);
		SnakePart head = partManager.getPart(part, direction, row, col);
		
		assertEquals(headFromMap.getImg(), head.getImg());
		assertEquals(direction, head.getDirection());
		assertEquals(row, head.getRow());
		assertEquals(col, head.getCol());
		
		part = BodyPart.BODY;
		SnakePart bodyFromMap = partManager.getBodyParts().get(direction);
		SnakePart body = partManager.getPart(part, direction, row, col);
		
		assertEquals(bodyFromMap.getImg(), body.getImg());
		assertEquals(direction, body.getDirection());
		assertEquals(row, body.getRow());
		assertEquals(col, body.getCol());
		
		part = BodyPart.TAIL;
		SnakePart tailFromMap = partManager.getTailParts().get(direction);
		SnakePart tail = partManager.getPart(part, direction, row, col);
		
		assertEquals(tailFromMap.getImg(), tail.getImg());
		assertEquals(direction, tail.getDirection());
		assertEquals(row, tail.getRow());
		assertEquals(col, tail.getCol());
		
	}

}
