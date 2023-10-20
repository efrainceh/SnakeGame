package snake;

import java.util.ArrayList;
import java.util.HashMap;

import base.Manager;
import lombok.Getter;

@Getter
public class SnakePartManager extends Manager {
	
    private final HashMap<Direction, SnakePart> headParts;
    private final HashMap<Direction, SnakePart> bodyParts;
    private final HashMap<Direction, SnakePart> tailParts;
    
    private boolean scaled = false;
    
    public SnakePartManager(String folderPath, String imgSuffix) {
    	
    	headParts = loadDirMap(folderPath + "/snakehead", imgSuffix);
    	bodyParts = loadDirMap(folderPath + "/snakebody", imgSuffix);
    	tailParts = loadDirMap(folderPath + "/snaketail", imgSuffix);
    	
    }
    
    public SnakePart getPart(BodyPart HBT, Direction direction, int row, int col) {
		 
		SnakePart part = null;
		
	    switch (HBT){
	    
	    	case HEAD:
	    		part = new SnakePart(headParts.get(direction));
	            break;
	        case BODY:
	            part = new SnakePart(bodyParts.get(direction));
	            break;
	        case TAIL:
	            part = new SnakePart(tailParts.get(direction));
	            break;
	            
	    }

	    part.setRow(row);
	    part.setCol(col);
	    
	    return part;
	   
	}
    
    @Override
    public void scaleImages(int width, int height) {
		 
		if (!scaled) {
			 
			// SCALING THE IMAGES INSIDE EACH TILE
			headParts.forEach((direction, snakePart) -> snakePart.setImg(scaleImage(snakePart.getImg(), width, height)));
			bodyParts.forEach((direction, snakePart) -> snakePart.setImg(scaleImage(snakePart.getImg(), width, height)));
			tailParts.forEach((direction, snakePart) -> snakePart.setImg(scaleImage(snakePart.getImg(), width, height)));
			scaled = true;
		        
		}  

	}

	protected HashMap<Direction, SnakePart> loadDirMap(String folderPath, String imgSuffix) {

	    // GET EACH IMAGE PATH AND DATA FROM THE FOLDER
		ArrayList<String> imgPaths = loadFilePaths(folderPath, imgSuffix);
    	ArrayList<String[]> imgData = loadImgData(folderPath, "txt");
	
	    // LOAD PARTS TO HASH
	    HashMap<Direction, SnakePart> tempParts = new HashMap<Direction, SnakePart>();
	    
	    for (int partIx = 0; partIx < imgPaths.size(); partIx++) {
	    	
	    	String[] data = imgData.get(partIx);
        	Direction direction = Direction.valueOf(data[1]);
        	SnakePart part = new SnakePart(imgPaths.get(partIx), direction);
	        tempParts.put(direction, part);
	        
	    }
	
	    return tempParts;

	}

}
