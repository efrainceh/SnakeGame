package snake;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import base.Cell;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SnakePart extends Cell{

    private Direction direction;

    public SnakePart(String path, Direction direction) {

    	super(path);
        this.direction = direction;

    }

    public SnakePart(SnakePart part) {

        this.row = part.row;
        this.col = part.col;
        this.collision = part.collision;
        this.direction = part.direction;
        this.img = part.img;

    } 

}