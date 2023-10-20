package snake;

import javax.swing.JComponent;

import board.Board;
import handlers.KeyHandler;
import handlers.KeyHandler.Pressed;
import panels.GamePanel.BoardPanel;

public class Snake extends BaseSnake{
	
	private KeyHandler keyH;
	// THIS IS THE JCOMPONENT THAT IS CONTROLLED BY KeyHandle
    private JComponent movingRect = new JComponent() { private static final long serialVersionUID = 844601306975731214L; };
    
	public Snake(Board board, int row, int col, String[] keys) {
		
    	super(board, row, col);
        this.keyH = new KeyHandler(movingRect, keys);

    }
	
	@Override
	public void addToPanel(BoardPanel panel) {
    	
    	panel.add(movingRect);
    	
    }
	
	@Override
	public boolean isMoving() {
		
		return keyH.getPressedKey() != Pressed.NULL_PRESSED;
		
	}
	
	@Override
	protected SnakePart getNewHead(SnakePart head) {
    	
    	int row = head.getRow();
        int col = head.getCol();
        KeyHandler.Pressed keyPressed = keyH.getPressedKey();

        switch (keyPressed) {
        
            case UP_PRESSED:
                head.setRow(row - 1);
                head.setDirection(Direction.UP);
                break;
            case DOWN_PRESSED:
                head.setRow(row + 1);
                head.setDirection(Direction.DOWN);
                break;
            case RIGHT_PRESSED:
                head.setCol(col + 1);
                head.setDirection(Direction.RIGHT);
                break;
            case LEFT_PRESSED:
                head.setCol(col - 1);
                head.setDirection(Direction.LEFT);
                break;
            default:
                break;
                
        }

        return snakeManager.getPart(BodyPart.HEAD, head.getDirection(), head.getRow(), head.getCol());

    }
	
}

