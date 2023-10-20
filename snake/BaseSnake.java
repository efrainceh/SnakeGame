package snake;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import lombok.Getter;
import lombok.Setter;
import panels.GamePanel.BoardPanel;
import board.Board;

@Getter
@Setter
public abstract class BaseSnake {
	
	private final int UPDATED = 0;
	private final int FAILED = -1;
	private final int startSize = 3;
    private LinkedList<SnakePart> snake = new LinkedList<SnakePart>();
    protected SnakePartManager snakeManager = new SnakePartManager("static/image/snakeparts", "png");
    private Board board;
    private SnakePart previousTail;

    public BaseSnake(Board board, int row, int col) {
    	
    	this.board = board;
        snakeManager.scaleImages(board.getTileSize(), board.getTileSize());
        createSnake(row, col);

    }
    
    // ABSTRACT METHODS
    
    public abstract boolean isMoving();
    
    public abstract void addToPanel(BoardPanel panel);
    
    protected abstract SnakePart getNewHead(SnakePart head);
    
    // --------------------------------------------------------------- 
   
    public int getHeadRow() { return snake.getFirst().getRow(); }
    
    public int getHeadCol() { return snake.getFirst().getCol(); }
    
    public boolean isOutOfPanel() {

    	SnakePart head = snake.getFirst();
        return partOutOfBounds(head);

    }

    public boolean collided() {

    	SnakePart head = snake.getFirst();
        return partCollided(head);

    }
    
    public int update(Boolean hasEaten) {
    	
    	int updateSuccess = FAILED;

        if (hasEaten) {
        	
        	updateSuccess = grow();
            
        }
        
        // CAPTURE CURRENT TAIL BEFORE IT MOVES SO THAT THIS POSITION CAN 
        // BE UPDATED TO COLLISION FALSE IN THE BOARD
        previousTail = new SnakePart(snake.getLast());
        
        move();
       
        return updateSuccess;
        
    }
    
    public void draw(Graphics2D g2) {

        int tileSize = board.getTileSize();
        
        for (SnakePart part : snake) {
        	
            g2.drawImage(part.getImg(), part.getCol() * tileSize, part.getRow() * tileSize, null);
            
        }
        
    }

    private void createSnake(int row, int col) {
        
        // ADD THE HEAD
        addPart(BodyPart.HEAD, Direction.RIGHT, row, col);

        // ADD REST OF SNAKE BODY, MOVING TOWARDS THE LEFT (SNAKE IS FACING RIGHT)
        int newCol = col;
        for (int snakePartIx = 1; snakePartIx < startSize; snakePartIx++) {
        	
            // MOVE ONE COLUMN TO THE LEFT 
            newCol-= 1;
            
            if (snakePartIx == startSize - 1) {
            	
            	// ADD THE TAIL
                addPart(BodyPart.TAIL, Direction.RIGHT, row, newCol);
                
            } else {
            	
                // OR ADD TO MIDDLE OF THE BODY
                addPart(BodyPart.BODY, Direction.RIGHT, row, newCol);
                
            }
            
        }

    }

    private void addPart(BodyPart HBT, Direction direction, int row, int col) {

        SnakePart part = snakeManager.getPart(HBT, direction, row, col);
        snake.add(part);

    }

    private int grow() {
    	
    	 // CHANGE THE TAIL TO SNAKE BODY PART
    	SnakePart tail = snake.getLast();
        changePart(BodyPart.BODY, tail, snake.size() - 1);
    	
    	// ADD A NEW TAIL AT THE END OF SNAKE. THE RETURN INDICATES WHETHER THE SNAKE WAS ABLE TO GROW OR NOT
        return addNewTail(tail);
        
    }
    
    private void changePart(BodyPart HBT, SnakePart oldPart, int snakeIndex) {

        SnakePart part = snakeManager.getPart(HBT, oldPart.getDirection(), oldPart.getRow(), oldPart.getCol());
        snake.set(snakeIndex, part);

    }

    private int addNewTail(SnakePart currentTail) { 
        
        int row = currentTail.getRow();
        int col = currentTail.getCol();
        Direction direction = currentTail.getDirection();
        
        // ADDING TO THE END OF SNAKE, DEPENDING ON THE DIRECTION THE SNAKE IS MOVING
        if (goingUp(direction)) {
        	
        	row++;
            
        }
        
        if (goingDown(direction)) {
        	
        	row--;
            
        }
        
        if (goingLeft(direction)) {
        	
        	col++;
            
        }
        
        if (goingRight(direction)) {
        	
        	col--;
            
        }
       
        SnakePart newTail = snakeManager.getPart(BodyPart.TAIL, direction, row, col);
        
        // THIS IS A SAFEGUARD AGAINST THE UNCOMMON CASE OF THE SNAKE GROWING
        // WHEN ITS TAIL IS NEXT TO THE BORDER, ANOTHER SNAKE, OR A COLLISION TILE.
        // IF THE SNAKE CAN'T GROW TOWARDS ITS "BACK" DIRECTION, THEN IT TRIES TO
        // GROW TO THE SIDES. IF IT CAN'T THEN IT RETURNS FAILED.
        if (validPosition(newTail)) {
            
            snake.add(newTail);
            return UPDATED;
        
        } 
        
        newTail = getNewPartToTheSide(BodyPart.TAIL, direction, row, col);
        
        if (newTail != null) {
        	
        	snake.add(newTail);
        	return UPDATED;
            
        }
        
        return FAILED;

    }
    
    private boolean goingUp(Direction direction) {
    	
    	return direction == Direction.UP || direction == Direction.UP_RIGHT || direction == Direction.UP_LEFT;
    			
    }
    
    private boolean goingDown(Direction direction) {
    	
    	return direction == Direction.DOWN || direction == Direction.DOWN_RIGHT || direction == Direction.DOWN_LEFT;
    			
    }
    
    private boolean goingRight(Direction direction) {
    	
    	return direction == Direction.RIGHT || direction == Direction.RIGHT_UP || direction == Direction.RIGHT_DOWN;
    			
    }
    
    private boolean goingLeft(Direction direction) {
    	
    	return direction == Direction.LEFT || direction == Direction.LEFT_UP || direction == Direction.LEFT_DOWN;
    			
    }
    
    private boolean validPosition(SnakePart part) {

        return !partOutOfBounds(part) || !partCollided(part);

    }
    
    private boolean partOutOfBounds(SnakePart part) {
    	
    	return part.getRow() < 0 || part.getRow() >= board.getNumberOfRows() || part.getCol() < 0 || part.getCol() >= board.getNumberOfCols();
    	
    }
    
    private boolean partCollided(SnakePart part) {
    	
    	return board.isCollision(part.getRow(), part.getCol());
    	
    }
    
    protected SnakePart getNewPartToTheSide(BodyPart HBT, Direction direction, int row, int col) {
    	
    	ArrayList<SnakePart> possibleParts = new ArrayList<SnakePart>();
    	possibleParts.add(snakeManager.getPart(HBT, Direction.RIGHT, row, col - 1));	// LEFT
    	possibleParts.add(snakeManager.getPart(HBT, Direction.RIGHT, row, col + 1));	// RIGHT
    	possibleParts.add(snakeManager.getPart(HBT, Direction.RIGHT, row - 1, col));	// UP
    	possibleParts.add(snakeManager.getPart(HBT, Direction.RIGHT, row + 1, col));	// DOWN

    	for (SnakePart part : possibleParts) {
    		
    		if (validPosition(part)) {
            	
                return part;
               
            }
    		
    	}
        
    	return null;

    }

    private void move() {
    	
    	// MOVE THE HEAD. THE NEW HEAD IS DEFINED BY THE CHILD CLASS OF BASESNAKE
        SnakePart currentHead = new SnakePart(snake.getFirst());
        SnakePart newHead = getNewHead(new SnakePart(currentHead));
        snake.set(0, newHead);
        
        // EXIT IF THE SNAKE IS NOT MOVING (IN THE CASE OF MORE THAN ONE SNAKE IT
        // MEANS NO KEY HAS BEEN PRESSED FOR THE SECOND SNAKE, SO WE DON'T WANT THAT
        // SNAKE TO MOVE)
        if (currentHead.equals(newHead)) {
        	
        	return;
        	
        }
        
        // CAPTURE THE SNAKEPART RIGHT AFTER HEAD SO THAT WE KNOW WHERE TO MOVE
        // THE REST OF THE SNAKE, THEN MOVE THIS PART. THIS PART BECOMES A CORNER
        // WHEN THE NEW AND PREVIOUS DIRECTIONS ARE DIFFERENT, SO IT'S NOT PART 
        // OF THE FOR LOOP THAT MOVES THE BODY OF THE SNAKE
        SnakePart previous = new SnakePart(snake.get(1));
        
        if (currentHead.getDirection() == newHead.getDirection()) {
        	
            changePart(BodyPart.BODY, currentHead, 1);
            
        } else {
        	
            setCornerPart(currentHead, newHead, 1);
            
        }
        
        // MOVE THE BODY BETWEEN THE SECOND SNAKEPART (AFTER THE HEAD) AND THE TAIL
        for (int snakePartIx = 2; snakePartIx < snake.size() - 1; snakePartIx++) {
        	
        	SnakePart next = new SnakePart(snake.get(snakePartIx));
            changePart(BodyPart.BODY, previous, snakePartIx);
            previous = new SnakePart(next);
              
        }
  
        // MOVE THE TAIL
        changePart(BodyPart.TAIL, previous, snake.size() - 1);
     
   }

   private void setCornerPart(SnakePart currentHead, SnakePart newHead, int snakeIx) {

	   Direction cornerDir = null;
	   Direction currentDir = currentHead.getDirection();
	   Direction newDir = newHead.getDirection();

        if (currentDir == Direction.UP && newDir == Direction.RIGHT) {
        	
            cornerDir = Direction.UP_RIGHT;
            
        }
        
        if (currentDir == Direction.UP && newDir == Direction.LEFT) {
        	
        	cornerDir = Direction.UP_LEFT;
        	
        }
        
        if (currentDir == Direction.DOWN && newDir == Direction.RIGHT) {
        	
        	cornerDir = Direction.DOWN_RIGHT;
        	
        }
        
        if (currentDir == Direction.DOWN && newDir == Direction.LEFT) {
        	
        	cornerDir = Direction.DOWN_LEFT;
            
        }
        
        if (currentDir == Direction.RIGHT && newDir == Direction.UP) {
        	
        	cornerDir = Direction.RIGHT_UP;
        	
        }
        
        if (currentDir == Direction.RIGHT && newDir == Direction.DOWN) {
        	
        	cornerDir = Direction.RIGHT_DOWN;
            
        }
        
        if (currentDir == Direction.LEFT && newDir == Direction.UP) {
        	
        	cornerDir = Direction.LEFT_UP;
            
        }
        
        if (currentDir == Direction.LEFT && newDir == Direction.DOWN) {
        	
        	cornerDir = Direction.LEFT_DOWN;
            
        }
        
        SnakePart part = snakeManager.getPart(BodyPart.BODY, cornerDir, currentHead.getRow(), currentHead.getCol());
        snake.set(snakeIx, part);

    }

}

