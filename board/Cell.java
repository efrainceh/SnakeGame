package board;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public abstract class Cell {

    public int row, col;
    public boolean collision;
    public BufferedImage img;

    public Cell() {

        this.row = 0;
        this.col = 0;
        this.collision = false;

    }

    public Cell(int row, int col) {

        this.row = row;
        this.col = col;
        this.collision = false;

    }

    public Cell(Cell cell) {

        this.row = cell.row;
        this.col = cell.col;
        this.collision = cell.collision;

    }

    public void loadCellImage(String path) {

        try {
            Path relativePath = Paths.get(path);
            Path absolutePath = relativePath.toAbsolutePath();
            InputStream is = new FileInputStream(absolutePath.toString());
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    // FOR DEBUGGING
    public String toString() {

        return "( " + row + ", " + col + ", " + collision + " )";

    }
 
}
