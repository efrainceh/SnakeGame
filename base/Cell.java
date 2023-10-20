package base;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public abstract class Cell {

    protected int row = 0;
    protected int col = 0;
    protected boolean collision = false;
    protected BufferedImage img;
    
    public Cell(String path) {

        this.img = loadCellImage(path);

    }

    public Cell(int row, int col) {

        this.row = row;
        this.col = col;

    }

    public Cell(Cell cell) {

        this.row = cell.row;
        this.col = cell.col;
        this.collision = cell.collision;

    }

    private BufferedImage loadCellImage(String path) {

    	BufferedImage tempImg = null;
    	
        try {
        	
            Path relativePath = Paths.get(path);
            Path absolutePath = relativePath.toAbsolutePath();
            InputStream is = new FileInputStream(absolutePath.toString());
            tempImg = ImageIO.read(is);
            return tempImg;
            
        } catch (IOException e) {
        	
            e.printStackTrace();
            
        }
        
        return tempImg;
        
    }
 
}
