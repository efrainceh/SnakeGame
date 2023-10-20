package food;

import base.Cell;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Food extends Cell{

    private int points = 0;
    
    public Food(String path, int points) {
        
        super(path);
    	this.points = points;

    }


}
