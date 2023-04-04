package snake;

import board.Cell;

public class SnakePart extends Cell{

    Direction direction;
    boolean isCorner;

    public SnakePart() { }

    public SnakePart(String path, Direction direction) {

        super();
        this.direction = direction;
        this.isCorner = false;
        loadCellImage(path);

    }

    public SnakePart(SnakePart part) {

        this.row = part.row;
        this.col = part.col;
        this.collision = part.collision;
        this.direction = part.direction;
        this.isCorner = part.isCorner;
        this.img = part.img;

    }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public void update(int row, int col) {

        this.row = row;
        this.col = col;

    }

    public void update(SnakePart part) {

        row = part.row;
        col = part.col;
        direction = part.getDirection();

    }

    protected static enum Direction {

        UP,
        DOWN,
        RIGHT,
        LEFT,
        UP_RIGHT,
        UP_LEFT,
        DOWN_RIGHT,
        DOWN_LEFT,
        RIGHT_UP,
        RIGHT_DOWN,
        LEFT_UP,
        LEFT_DOWN

    }
    
    // FOR DEBUGGING
    public String toString() {

        return "( " + row + ", " + col + ", " + collision + ", " + direction + " )";

    }

}
