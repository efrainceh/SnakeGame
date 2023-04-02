package snake;

import java.awt.Graphics2D;
import java.util.LinkedList;
import javax.swing.JComponent;

import board.Board;
import main.KeyHandler;
import main.KeyHandler.Pressed;
import panels.GamePanel.BoardPanel;
import snake.SnakePart.Direction;

public class Snake {
    
    // DEFAULT INITIAL SNAKE SETTINGS
    final static int startSize = 3;
    int startRow = 7;
    int startCol = 7;

    // SNAKE IMAGES
    //final static ArrayList<SnakeHead> headTiles = loadSnakeParts("static/image/snakehead");
    // final static ArrayList<BufferedImage> bodyImages = loadImages("static/image/snakebody");
    // final static ArrayList<BufferedImage> tailImages = loadImages("static/image/snaketail");

    //UtilityTool utilityTool = new UtilityTool();

    LinkedList<SnakePart> snake;
    SnakeHead head;
    BoardPanel gp;
    Board board;
    KeyHandler keyH;
    boolean isMoving;

    public Snake(BoardPanel gp, int startRow, int startCol) {

        this.snake = new LinkedList<SnakePart>();
        this.startRow = startRow;
        this.startCol = startCol;
        this.gp = gp;
        this.board = gp.getBoard();
        this.isMoving = false;
        createSnake();

    }

    public LinkedList<SnakePart> getSnakeParts() { return snake; }
    public int getHeadRow() { return head.row; }
    public int getHeadCol() { return head.col; }
    public int getTailRow() { return snake.getLast().row; }
    public int getTailCol() { return snake.getLast().col; }
    public boolean isMoving() { return isMoving; }

    // private static ArrayList<SnakeHead> loadSnakeParts(String folderPath) {
 
    //     // GET EACH IMAGE PATH FROM THE FOLDER
    //     FolderHandler folderH = new FolderHandler(folderPath);        
    //     ArrayList<String> imgPaths = folderH.getFilePaths("png");

    //     // ADD SNAKEPARTS
    //     ArrayList<SnakeHead> tempParts = new ArrayList<SnakeHead>();
    //     for (String path : imgPaths) {
    //         tempParts.add(new SnakeHead(0, 0));
    //     }

    //     return tempParts;

    // }


    private void createSnake() {
        
        // ADD THE HEAD
        head = new SnakeHead(startRow, startCol);
        //head.img = utilityTool.scaleImage(head.img, gp.getTileSize(), gp.getTileSize());
        snake.add(head);

        // ADD REST OF SNAKE BODY, MOVING TOWARDS THE LEFT
        int col = startCol;
        for (int snakePartIx = 1; snakePartIx < startSize; snakePartIx++) {
            // MOVE ONE TOWARDS THE LEFT 
            col-= 1;
            // ADD THE TAIL
            if (snakePartIx == startSize - 1) {
                SnakeTail tail = new SnakeTail(startRow, col);
                //tail.img = utilityTool.scaleImage(tail.img, gp.getTileSize(), gp.getTileSize());
                snake.add(tail);
            } else {
                // OR ADD TO MIDDLE OF THE BODY
                SnakeBody body = new SnakeBody(startRow, col);
                //body.img = utilityTool.scaleImage(body.img, gp.getTileSize(), gp.getTileSize());
                snake.add(body);
            }
            // MARK THE BOARD POSITIONS AS COLLISION
            board.updateCell(startRow, col, true);
        }

    }

    public void addKeyHandler(String[] keys) {

        this.keyH = new KeyHandler(head.movingRect, keys);

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

    private void grow() {

        // ADD A NEW SNAKEBODY AT THE END OF SNAKE
        SnakePart tail = snake.getLast();
        SnakeBody newBody = makeNewBody(tail);
        snake.add(newBody);
        // SWAP THE PARTS SO THAT TAIL IS AT THE END. THIS IS BECAUSE newBody DOES
        // NOT HAVE THE IMAGES TO DRAW A TAIL
        swap(tail, newBody);

    }

    private SnakeBody makeNewBody(SnakePart part) {
        
        Direction bodyDirection = part.getDirection();
        int bodyRow = part.row;
        int bodyCol = part.col;
        switch (bodyDirection) {
            case UP:
                bodyRow += 1;
                break;
            case DOWN:
                bodyRow -= 1;
                break;
            case LEFT:
                bodyCol += 1;
                break;
            case RIGHT:
                bodyCol -= 1;
                break;
            default:
                break;
        }
        SnakeBody body = new SnakeBody(bodyRow, bodyCol);
        body.setDirection(bodyDirection);
        //body.img = utilityTool.scaleImage(body.img, gp.getTileSize(), gp.getTileSize());
        return body;

    }

    private void swap(SnakePart tail, SnakePart body) {

        int lastIndex = snake.size() - 1;
        snake.set(lastIndex, tail);
        snake.set(lastIndex - 1, body);

    }

    private void move() {

        // ROW AND COL NEED TO BE CAPTURED BEFORE TAIL MOVES TO A NEW CELL
        SnakePart tail = snake.getLast();
        int row = tail.row;
        int col = tail.col;
        
        for (int snakePartIx = snake.size() - 1; snakePartIx >= 0; snakePartIx--) {
            SnakePart part = snake.get(snakePartIx);
            if (snakePartIx == 0) {
                // CAST TO HEAD IN ORDER TO USE SNAKEHEAD UPDATE METHOD
                board.updateCell(head, false);
                head = (SnakeHead)part;
                head.update();
            } else {
                this.board.updateCell(part, true);
                SnakePart previousPart = snake.get(snakePartIx - 1);
                part.update(previousPart);
            }         
        }
        //Mark the now empty snake tail cell as EMPTY
        board.updateCell(row, col, false);
       
    }

    /* 
        DRAW METHODS
    */

    public void draw(Graphics2D g2) {

        // DRAW THE TAIL AND BODY
        for (int snakePartIx = snake.size() - 1; snakePartIx > 0 ; snakePartIx--) {
            if (snakePartIsCorner(snakePartIx)) {
                drawCorner(g2, snakePartIx);
            } else {
                drawPart(g2, snakePartIx);
            }   
        }

        // DRAW THE HEAD
        drawPart(g2, 0);
        
    }

    private boolean snakePartIsCorner(int position) {

        SnakePart previousPart = snake.get(position - 1);
        SnakePart.Direction previousDirection = previousPart.getDirection();
        SnakePart currentPart = snake.get(position);
        SnakePart.Direction currentDirection = currentPart.getDirection();
        return previousDirection != currentDirection;

    }

    private void drawCorner(Graphics2D g2, int position) {

        SnakePart part = snake.get(position);
        SnakePart previousPart = snake.get(position - 1);
        String imgPath = part.getCornerPath(previousPart.getDirection(), part.getDirection());
        part.loadCellImage(imgPath);
        int size = gp.getTileSize();
        g2.drawImage(part.img, part.col * size, part.row * size, size, size, null);
        //g2.drawImage(part.img, part.col * size, part.row * size, null);

    }

    private void drawPart(Graphics2D g2, int position) {

        SnakePart part = snake.get(position);
        String imgPath = part.getImgPath();
        part.loadCellImage(imgPath);
        int size = gp.getTileSize();
        g2.drawImage(part.img, part.col * size, part.row * size, size, size, null);
        //g2.drawImage(part.img, part.col * size, part.row * size, null);

    }


    /* 
        RUNNING GAME METHODS
    */

    public boolean ate() {

        return board.isFood(head);

    }

    public boolean isOutOfPanel() {

        int row = head.row;
        int col = head.col;
        return (row < 0 || row >= gp.getNumberOfRows() || col < 0 || col >= gp.getNumberOfCols());

    }

    public boolean collided() {

        return board.isCollision(head);

    }



    public class SnakeHead extends SnakePart{

        // THIS IS THE JCOMPONENT THAT IS CONTROLLED BY KeyHandle
        JComponent movingRect = new JComponent() {};

        public SnakeHead(int row, int col) {
            
            super(row, col);
            gp.add(movingRect);
            loadImgPaths("static/image/snakehead");
            
        }

        public void update() {

            KeyHandler.Pressed pressed = keyH.getPressedKey();

            switch (pressed) {
                case UP_PRESSED:
                    row -= 1;
                    direction = Direction.UP;
                    break;
                case DOWN_PRESSED:
                    row += 1;
                    direction = Direction.DOWN;
                    break;
                case RIGHT_PRESSED:
                    col += 1;
                    direction = Direction.RIGHT;
                    break;
                case LEFT_PRESSED:
                    col -= 1;
                    direction = Direction.LEFT;
                    break;
                default:
                    break;
            }

        }

    }

    public class SnakeBody extends SnakePart {

        public SnakeBody(int row, int col) {

            super(row, col);
            hasCorners = true;
            loadImgPaths("static/image/snakebody");
            
        }
    
    }

    public class SnakeTail extends SnakePart {

        public SnakeTail(int row, int col) {

            super(row, col);
            hasCorners = true;
            loadImgPaths("static/image/snaketail");

        }

    }

}

