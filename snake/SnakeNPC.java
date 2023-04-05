package snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import panels.GamePanel.BoardPanel;
import snake.SnakePart.Direction;

public class SnakeNPC extends Snake {

    //ArrayList<ArrayList<Integer>> pathToFood;
    LinkedList<ArrayList<Integer>> pathToFood;

    public SnakeNPC(BoardPanel gp, int startRow, int startCol) {

        super(gp, startRow, startCol);
        //getPath(3, 3);

    }

    public void loadPath(LinkedList<ArrayList<Integer>> path) {

        pathToFood = path;

    }

    public void getPath(int destRow, int destCol) {

        
        pathToFood = new LinkedList<ArrayList<Integer>>();


        int row = snake.getFirst().row;
        int col = snake.getFirst().col;
        ArrayList<Integer> coordinates = new ArrayList<Integer>(Arrays.asList(row, col + 1));
        pathToFood.add(coordinates);
        coordinates = new ArrayList<Integer>(Arrays.asList(row, col + 2));
        pathToFood.add(coordinates);
        coordinates = new ArrayList<Integer>(Arrays.asList(row, col + 3));
        pathToFood.add(coordinates);
        coordinates = new ArrayList<Integer>(Arrays.asList(row, col + 4));
        pathToFood.add(coordinates);
        coordinates = new ArrayList<Integer>(Arrays.asList(row + 1, col + 4));
        pathToFood.add(coordinates);
        coordinates = new ArrayList<Integer>(Arrays.asList(row + 2, col + 4));
        pathToFood.add(coordinates);

        System.out.println(pathToFood);

    }

    @Override
    public void update(Boolean hasEaten) {

        if (hasEaten) {
            grow();
        }

        if (isMoving) {
            move();
        }
        
    }

    
    protected void move() {

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

        if (pathToFood.isEmpty()) {
            System.out.println("empty: ");
            System.out.println(head.direction);
           // CLOCKWISE
            Direction nextDir = null;
            if (head.col + 1 < gp.getNumberOfCols()) {
                nextDir = Direction.RIGHT;
                return getPart("HEAD", nextDir, head.row, head.col + 1);
            }
            if (head.row + 1 < gp.getNumberOfRows()) {
                nextDir = Direction.DOWN;
                return getPart("HEAD", nextDir, head.row + 1, head.col);
            }
            if (head.col - 1 >= 0) {
                // System.out.println(head.direction);
                // System.out.println(head.row);
                // System.out.println(head.col);
                nextDir = Direction.LEFT;
                return getPart("HEAD", nextDir, head.row, head.col - 1);
            }
            if (head.row - 1 >= 0) {
                nextDir = Direction.UP;
                return getPart("HEAD", nextDir, head.row - 1, head.col);
            }

            

        }

        ArrayList<Integer> nextTile = pathToFood.remove();
        int nextRow = nextTile.get(0);
        int nextCol = nextTile.get(1);
        Direction nextDir = null;
        if (nextCol - head.col < 0) {
            nextDir = Direction.LEFT;
        }
        if (nextCol - head.col > 0) {
            nextDir = Direction.RIGHT;
        }
        if (nextRow  - head.row < 0) {
            nextDir = Direction.UP;
        }
        if (nextRow  - head.row > 0) {
            nextDir = Direction.DOWN;
        }

        return getPart("HEAD", nextDir, nextRow, nextCol);

    }
    
}
