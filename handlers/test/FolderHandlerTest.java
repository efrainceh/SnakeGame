package handlers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import handlers.FolderHandler;

class FolderHandlerTest {

	@Test
	void testGetFilePaths() {
		
		ArrayList<String> filePaths = new ArrayList<String>();
		filePaths.add("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testMap/boardMap1.txt");
		filePaths.add("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testMap/boardMap2.txt");
		filePaths.add("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testMap/boardMap3.txt");
		filePaths.add("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testMap/boardMap4.txt");
		filePaths.add("/Users/Jannine/Desktop/payo_programming/Projects/SnakeGame/testFiles/testMap/boardMap5.txt");
		
		FolderHandler fh = new FolderHandler();
		
		assertEquals(filePaths, fh.getFilePaths("testFiles/testMap/", "txt"));
		
	}

}
