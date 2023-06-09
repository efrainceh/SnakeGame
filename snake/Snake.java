package snake;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JComponent;

import board.Board;
import main.FolderHandler;
import main.KeyHandler;
import main.KeyHandler.Pressed;
import panels.GamePanel.BoardPanel;
import snake.SnakePart.Direction;
import utilityTool.UtilityTool;



public class Snake {

    // LOADING SNAKE PARTS
    final static HashMap<Direction, SnakePart> headParts = loadDirMap("static/image/snakehead", false);
    final static HashMap<Direction, SnakePart> bodyParts = loadDirMap("static/image/snakebody", true);
    final static HashMap<Direction, SnakePart> tailParts = loadDirMap("static/image/snaketail", true);

    // DEFAULT INITIAL SNAKE SETTINGS
    final static int startSize = 3;
    int startRow = 7;
    int startCol = 7;

    // THIS IS THE JCOMPONENT THAT IS CONTROLLED BY KeyHandle
    JComponent movingRect = new JComponent() {};
    
    final UtilityTool utilityTool = new UtilityTool();
    LinkedList<SnakePart> snake = new LinkedList<SnakePart>();

    //SnakePart head;
    BoardPanel gp;
    Board board;
    KeyHandler keyH;
    boolean isMoving;

    public Snake(BoardPanel gp, int startRow, int startCol) {

        this.startRow = startRow;
        this.startCol = startCol;
        this.gp = gp;
        this.board = gp.getBoard();
        this.isMoving = false;
        this.gp.add(movingRect);
        createSnake();

    }

    public LinkedList<SnakePart> getSnakeParts() { return snake; }
    public int getHeadRow() { return snake.getFirst().row; }
    public int getHeadCol() { return snake.getFirst().col; }
    public int getTailRow() { return snake.getLast().row; }
    public int getTailCol() { return snake.getLast().col; }
    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean isMoving) { this.isMoving = isMoving; }


    private static HashMap<Direction, SnakePart> loadDirMap(String folderPath, boolean hasCorners) {

        // GET EACH IMAGE PATH FROM THE FOLDER
        FolderHandler folderH = new FolderHandler(folderPath);        
        ArrayList<String> imgPaths = folderH.getFilePaths("png");

        // LOAD TILES TO HASH
        HashMap<Direction, SnakePart> tempParts = new HashMap<Direction, SnakePart>();
        int index = 0;
        for (Direction direction : Direction.values()) {
            tempParts.put(direction, new SnakePart(imgPaths.get(index), direction));
            index++;
        }

        return tempParts;

    }

    private void createSnake() {
        
        // ADD THE HEAD
        addPart("HEAD", Direction.RIGHT, startRow, startCol);

        // ADD REST OF SNAKE BODY, MOVING TOWARDS THE LEFT
        int col = startCol;
        for (int snakePartIx = 1; snakePartIx < startSize; snakePartIx++) {
            // MOVE ONE TOWARDS THE LEFT 
            col-= 1;
            // ADD THE TAIL
            if (snakePartIx == startSize - 1) {
                addPart("TAIL", Direction.RIGHT, startRow, col);
            } else {
                // OR ADD TO MIDDLE OF THE BODY
                addPart("BODY", Direction.RIGHT, startRow, col);
            }
            // MARK THE BOARD POSITIONS AS COLLISION
            board.updateCell(startRow, col, true);
        }

    }

    protected void addPart(String hbt, Direction direction, int row, int col) {

        SnakePart part = getPart(hbt, direction, row, col);
        snake.add(part);

    }

    protected void setPart(String hbt, Direction direction, int row, int col, int snakeIndex) {

        SnakePart part = getPart(hbt, direction, row, col);
        snake.set(snakeIndex, part);

    }

    protected SnakePart getPart(String hbt, Direction direction, int row, int col) {

        SnakePart part = null;
        switch (hbt){
            case "HEAD":
                part = new SnakePart(headParts.get(direction));
                break;
            case "BODY":
                part = new SnakePart(bodyParts.get(direction));
                break;
            case "TAIL":
                part = new SnakePart(tailParts.get(direction));
                break;
        }
        part.update(row, col);
        return part;

    }

    public void addKeyHandler(String[] keys) {

        this.keyH = new KeyHandler(movingRect, keys);

    }

    public void scaleImages(int width, int height) {
        
        // SCALING THE IMAGES INSIDE EACH TILE
        headParts.forEach((direction, snakePart) -> snakePart.img = utilityTool.scaleImage(snakePart.img, width, height));
        bodyParts.forEach((direction, snakePart) -> snakePart.img = utilityTool.scaleImage(snakePart.img, width, height));
        tailParts.forEach((direction, snakePart) -> snakePart.img = utilityTool.scaleImage(snakePart.img, width, height));

    }

    /* 
        UPDATE METHODS
    */

    public void update(Boolean hasEaten) {

        if (hasEaten) {
            grow();
        }

        // ONLY MOVE THE SNAKE AFTER AN ACCEPTED KEY HAS BEEN PRESSED
        if (keyH.getPressedKey() != Pressed.NULL_PRESSED) {
            isMoving = true;
            move();
        }
        
    }

    protected void grow() {

        // ADD A NEW SNAKEBODY AT THE END OF SNAKE
        SnakePart currentTail = new SnakePart(snake.getLast());
        addNewTail(new SnakePart(currentTail));

        // CHANGE THE PREVIOUS TAIL TO SNAKE BODY PART
        setPart("BODY", currentTail.direction, currentTail.row, currentTail.col, snake.size() - 2);

    }

    private void addNewTail(SnakePart part) {
        
        int row = part.row;
        int col = part.col;
        Direction bodyDirection = part.getDirection();
        
        // ADDING TO THE BACK OF THE TAIL
        if (bodyDirection == Direction.UP || bodyDirection == Direction.UP_RIGHT || bodyDirection == Direction.UP_LEFT) {
            part.row += 1;
        }
        if (bodyDirection == Direction.DOWN || bodyDirection == Direction.DOWN_RIGHT || bodyDirection == Direction.DOWN_LEFT) {
            part.row -= 1;
        }
        if (bodyDirection == Direction.LEFT || bodyDirection == Direction.LEFT_UP || bodyDirection == Direction.LEFT_DOWN) {
            part.col += 1;
        }
        if (bodyDirection == Direction.RIGHT || bodyDirection == Direction.RIGHT_UP || bodyDirection == Direction.RIGHT_DOWN) {
            part.col -= 1;
        }
       
        SnakePart newTail = getPart("TAIL", bodyDirection, part.row, part.col);

        if (validPosition(newTail)) {
            
            snake.add(newTail);
            board.updateCell(newTail, true);
        
        } else {

            // THIS IS A SAFEGUARD AGAINST THE UNCOMMON CASE OF THE SNAKE GROWING
            // WHEN ITS TAIL IS NEXT TO THE BORDER, ANOTHER SNAKE, OR A COLLISION TILE.
            // IF THE SNAKE CAN't GROW TOWARDS ITS "BEHIND" DIRECTION, THEN IT TRIES TO
            // GROW TO THE SIDES. IF IT CAN'T THEN NOTHING HAPPENS.
            
            addPartToTheSide(bodyDirection, row, col);
            
        }

    }

    private boolean validPosition(SnakePart part) {

        // PART IS OUT OF THE BOARD
        if (part.row < 0 || part.row >= gp.getNumberOfRows() || part.col < 0 || part.col >= gp.getNumberOfCols()) {
            return false;
        }

        // PART IS IN AN ALREADY OCCUPIED BOARD, EITHER BY SNAKE OF COLLISION TILE
        if (board.isCollision(part)) {
            return false;
        }

        return true;

    }

    private void addPartToTheSide(Direction direction, int row, int col) {

        // IF SNAKE IS MOVING UP OR DOWN, THEN TRY TO GROW LEFT OR RIGHT
        if (direction == Direction.UP || direction == Direction.DOWN) {
                
            SnakePart rightTail = getPart("HEAD", Direction.RIGHT, row, col + 1);
            SnakePart leftTail = getPart("HEAD", Direction.RIGHT, row, col + 1);
            
            if (validPosition(rightTail)) {
                snake.add(rightTail);
                board.updateCell(rightTail, true);
                return;
            }

            if (validPosition(leftTail)) {
                snake.add(leftTail);
                board.updateCell(leftTail, true);
                return;
            }
            
        }

        //  IF SNAKE IS MOVING LEFT OR RIGHT, THEN TRY TO GROW UP OR DOWN
        if (direction == Direction.RIGHT || direction == Direction.LEFT) {

            SnakePart upTail = getPart("HEAD", Direction.UP, row - 1, col);
            SnakePart downTail = getPart("HEAD", Direction.DOWN, row + 1, col);
            
            if (validPosition(upTail)) {
                snake.add(upTail);
                board.updateCell(upTail, true);
                return;
            }

            if (validPosition(downTail)) {
                snake.add(downTail);
                board.updateCell(downTail, true);
                return;
            }

        }

    }

    private void move() {

        // TAIL NEEDS TO BE CAPTURED BEFORE IT MOVES TO A NEW CELL
        SnakePart tail = new SnakePart(snake.getLast());

        // MOVE THE TAIL
        SnakePart beforeTail = snake.get(snake.size() - 2);
        setPart("TAIL", beforeTail.getDirection(), beforeTail.row, beforeTail.col, snake.size() - 1);
        
        // MOVE THE BODY, EXCEPT FOR THE SNAKEPART RIGHT AFTER THE HEAD
        for (int snakePartIx = snake.size() - 2; snakePartIx > 1; snakePartIx--) {
            board.updateCell(snake.get(snakePartIx), true); 
            SnakePart previousPart = snake.get(snakePartIx - 1);
            setPart("BODY", previousPart.direction, previousPart.row, previousPart.col, snakePartIx);
                  
        }

        // MOVE THE HEAD, CAST TO HEAD IN ORDER TO USE SNAKEHEAD UPDATE METHOD
        SnakePart currentHead = new SnakePart(snake.getFirst());
        board.updateCell(currentHead, false);
        SnakePart newHead = getNewHead(new SnakePart(currentHead));
        snake.set(0, newHead);
        
        // MOVE SNAKEPART RIGHT AFTER HEAD, WHICH MAY BECOME A CORNER
        if (currentHead.getDirection() == newHead.getDirection()) {
            setPart("BODY", currentHead.getDirection(), currentHead.row, currentHead.col, 1);
        } else {
            setCornerPart(currentHead, newHead, 1);
        }
        board.updateCell(currentHead.row, currentHead.col, true);

        // ONLY AFTER MOVING THE SNAKE DO WE UPDATE THE BOARD OLD TAIL TILE
        board.updateCell(tail.row, tail.col, false);
       
    }

    private SnakePart getNewHead(SnakePart head) {

        KeyHandler.Pressed pressed = keyH.getPressedKey();

        switch (pressed) {
            case UP_PRESSED:
                head.row -= 1;
                head.direction = Direction.UP;
                break;
            case DOWN_PRESSED:
                head.row += 1;
                head.direction = Direction.DOWN;
                break;
            case RIGHT_PRESSED:
                head.col += 1;
                head.direction = Direction.RIGHT;
                break;
            case LEFT_PRESSED:
                head.col -= 1;
                head.direction = Direction.LEFT;
                break;
            default:
                break;
        }

        return getPart("HEAD", head.direction, head.row, head.col);

    }

    protected void setCornerPart(SnakePart currentHead, SnakePart newHead, int snakeIndex) {

        Direction dir = null;
        Direction currentDir = currentHead.getDirection();
        Direction newDir = newHead.getDirection();

        if (currentDir == Direction.UP && newDir == Direction.RIGHT) {
            dir = Direction.UP_RIGHT;
        }
        if (currentDir == Direction.UP && newDir == Direction.LEFT) {
            dir = Direction.UP_LEFT;
        }
        if (currentDir == Direction.DOWN && newDir == Direction.RIGHT) {
            dir = Direction.DOWN_RIGHT;
        }
        if (currentDir == Direction.DOWN && newDir == Direction.LEFT) {
            dir = Direction.DOWN_LEFT;
        }
        if (currentDir == Direction.RIGHT && newDir == Direction.UP) {
            dir = Direction.RIGHT_UP;
        }
        if (currentDir == Direction.RIGHT && newDir == Direction.DOWN) {
            dir = Direction.RIGHT_DOWN;
        }
        if (currentDir == Direction.LEFT && newDir == Direction.UP) {
            dir = Direction.LEFT_UP;
        }
        if (currentDir == Direction.LEFT && newDir == Direction.DOWN) {
            dir = Direction.LEFT_DOWN;
        }
        
        SnakePart part = getPart("BODY", dir, currentHead.row, currentHead.col);
        snake.set(snakeIndex, part);

    }

    /* 
        DRAW METHODS
    */

    public void draw(Graphics2D g2) {

        int size = gp.getTileSize();
        for (SnakePart part : snake) {
            g2.drawImage(part.img, part.col * size, part.row * size, null);
        }
        
    }

    /* 
        RUNNING GAME METHODS
    */

    public boolean ate() {

        return board.isFood(snake.getFirst());

    }

    public boolean isOutOfPanel() {

        int row = snake.getFirst().row;
        int col = snake.getFirst().col;
        return (row < 0 || row >= gp.getNumberOfRows() || col < 0 || col >= gp.getNumberOfCols());

    }

    public boolean collided() {

        return board.isCollision(snake.getFirst());

    }

    // DEBUGGING

    public String toString() {

        String s = "";
        for (SnakePart part : snake) {
            s += part.toString();
            s += "\n";
        }
        return s;
    }


}

