package handlers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import handlers.FontHandler;

class FontHandlerTest {

	@Test
	void testAddNewFont() {
		
		FontHandler fh = new FontHandler();
		fh.addNewFont("static/fonts/PixelMplus12-Regular.ttf");
		
		assertTrue(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()).contains("PixelMplus12"));
		
	}

}
