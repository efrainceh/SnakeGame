package game.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import board.Board;
import lombok.Setter;

public class TextWindow {
	
	int boardWidth;
	int boardHeight;
	
	// RECTANGLE POSITION AND SIZE
	int xWindow;
	int yWindow;
	int windowWidth;
	int windowHeight;
	
	@Setter String text = "";
	@Setter boolean active = true;
	
	public TextWindow(Board board) {
		
		boardWidth = board.getNumberOfCols() * board.getTileSize();
		boardHeight = board.getNumberOfRows() * board.getTileSize();
		xWindow = (int) (boardWidth * 0.25);
		yWindow = (int) (boardHeight * 0.35);
		windowWidth = (int) (boardWidth * 0.5);
		windowHeight = (int) (boardHeight * 0.3);
		
	}
	
	public void inactivate() {
		
		active = false;
		
	}
	
	public void activate(String text) {
		
		this.text = text;
		active = true;
		
	}
	
	public void draw(Graphics2D g2) {
		
		if (active) {
	 
			// DISPLAY RECTANGLE
			g2.setColor(new Color(255, 255, 255, 150));
			g2.fillRoundRect(xWindow, yWindow, windowWidth, windowHeight, 35, 35);
	
			// BORDER
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(5));
			g2.drawRoundRect(xWindow, yWindow, windowWidth, windowHeight, 25, 25);
	
			// TEXT
			g2.setColor(Color.RED);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45));
			FontMetrics fm = g2.getFontMetrics();
			int xText = xWindow + ((windowWidth - fm.stringWidth(text)) / 2);
			int yText = yWindow + ((windowHeight - fm.getHeight()) / 2) + fm.getAscent();
			g2.drawString(text, xText, yText);
		
		}

	 }

}
