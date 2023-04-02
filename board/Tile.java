package board;


public class Tile extends Cell {

    public Tile(String path, boolean collision) {

        this.collision = collision;
        loadCellImage(path);

    }

    public Tile(String path, boolean collision, int row, int col) {

        this.row = row;
        this.col = col;
        this.collision = collision;
        loadCellImage(path);

    }
    
}
