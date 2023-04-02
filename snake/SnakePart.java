package snake;

import java.util.ArrayList;
import java.util.HashMap;

import board.Cell;
import main.FolderHandler;

public class SnakePart extends Cell{

    Direction direction;
    boolean hasCorners;
    HashMap<Direction, String> imgPathDirection = new HashMap<Direction, String>();
    HashMap<Corner, String> imgPathCorners = new HashMap<Corner, String>();

    public SnakePart(int row, int col) {

        super(row, col);
        this.direction = Direction.RIGHT;
        this.hasCorners = false;

    }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public void update(SnakePart part) {

        row = part.row;
        col = part.col;
        direction = part.getDirection();

    }

    public void loadImgPaths(String folderPath) {

        // GET EACH IMAGE PATH FROM THE FOLDER
        FolderHandler folderH = new FolderHandler(folderPath);        
        ArrayList<String> imgPaths = folderH.getFilePaths("png");
        
        // FIRST LOAD PATHS TO DIRECTION HASH, THESE ARE IN INDEX 0 - 3
        int index = 0;
        for (Direction dir : Direction.values()) {
            imgPathDirection.put(dir, imgPaths.get(index));
            index++;
        }

        // THEN, IF THEY EXIST, LOAD PATHS TO CORNER HASH, THESE ARE IN INDEX 4 - 11
        if (hasCorners) {
            for (Corner corner : Corner.values()) {
                imgPathCorners.put(corner, imgPaths.get(index));
                index++;
            }
        }
       
    }

    protected String getImgPath() {

        switch (direction) {
            case UP:
                return imgPathDirection.get(Direction.UP);
            case DOWN:
                return imgPathDirection.get(Direction.DOWN);
            case RIGHT:
                return imgPathDirection.get(Direction.RIGHT);
            case LEFT:
                return imgPathDirection.get(Direction.LEFT);
            default:
                return "";
        }

    }

    protected String getCornerPath(SnakePart.Direction previous, SnakePart.Direction next) {
        
        if (previous == Direction.RIGHT && next == Direction.UP) {
            return imgPathCorners.get(Corner.CORNER_1);
        }
        if (previous == Direction.DOWN && next == Direction.RIGHT) {
            return imgPathCorners.get(Corner.CORNER_2);
        }
        if (previous == Direction.UP && next == Direction.RIGHT) {
            return imgPathCorners.get(Corner.CORNER_3);
        }
        if (previous == Direction.RIGHT && next == Direction.DOWN) {
            return imgPathCorners.get(Corner.CORNER_4);
        }
        if (previous == Direction.DOWN && next == Direction.LEFT) {
            return imgPathCorners.get(Corner.CORNER_5);
        }
        if (previous == Direction.LEFT && next == Direction.UP) {
            return imgPathCorners.get(Corner.CORNER_6);
        }
        if (previous == Direction.LEFT && next == Direction.DOWN) {
            return imgPathCorners.get(Corner.CORNER_7);
        }
        if (previous == Direction.UP && next == Direction.LEFT) {
            return imgPathCorners.get(Corner.CORNER_8);
        }
        return "";

    }

    protected static enum Direction {

        UP,
        DOWN,
        RIGHT,
        LEFT

    }

    protected static enum Corner {

        CORNER_1,
        CORNER_2,
        CORNER_3,
        CORNER_4,
        CORNER_5,
        CORNER_6,
        CORNER_7,
        CORNER_8

    }
    
}
