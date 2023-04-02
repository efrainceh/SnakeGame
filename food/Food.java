package food;

import board.Cell;

public class Food extends Cell{

    int points;
    
    public Food(String path, int points) {
        
        this.points = points;
        loadCellImage(path);

    }

    public Food(int row, int col) {
        
        super(row, col);
        this.points = 0;
    }

    public int getPoints() { return points; }

    public void update(String path, int points, int row, int col) {
        
        this.points = points;
        this.row = row;
        this.col = col;
        loadCellImage(path);
        
    }

}
