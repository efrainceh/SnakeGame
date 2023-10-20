package board;

import base.Cell;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tile extends Cell {

    public Tile(String path, boolean collision) {

    	super(path);
        this.collision = collision;

    }
    
}
