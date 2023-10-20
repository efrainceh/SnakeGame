package snake;

import java.util.ArrayList;
import java.util.LinkedList;

import board.Board;
import lombok.Getter;
import lombok.Setter;
import panels.GamePanel.BoardPanel;

public class SnakeNPC extends BaseSnake {

    @Getter @Setter private LinkedList<ArrayList<Integer>> pathToFood;

    public SnakeNPC(Board board, int row, int col) {

        super(board, row, col);
        
    }
    
    @Override
	public void addToPanel(BoardPanel panel) { }
    
    @Override
    public boolean isMoving() {
    	
    	return false;
    	
    }

    @Override
    protected SnakePart getNewHead(SnakePart head) {
    	
        // GET THE NEXT TILE FOR THE HEAD TO GO TO
        ArrayList<Integer> nextTile = pathToFood.remove();
        int nextRow = nextTile.get(0);
        int nextCol = nextTile.get(1);
        
        // CHOOSE THE NEXT DIRECTION OF THE HEAD
        Direction nextDir = null;
        if (nextCol - head.getCol() < 0) {
        	
            nextDir = Direction.LEFT;
        
        }
        
        if (nextCol - head.getCol() > 0) {
            
        	nextDir = Direction.RIGHT;
        	
        }
        
        if (nextRow  - head.getRow() < 0) {
        	
            nextDir = Direction.UP;
            
        }
        
        if (nextRow  - head.getRow() > 0) {
        	
            nextDir = Direction.DOWN;
            
        }

        return snakeManager.getPart(BodyPart.HEAD, nextDir, nextRow, nextCol);

    }
    
}
