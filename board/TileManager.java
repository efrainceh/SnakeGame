package board;

import java.util.ArrayList;

import base.Manager;


public class TileManager extends Manager {
	
    private final ArrayList<Tile> tiles;
    
    public TileManager(String folderPath, String imgSuffix) {
    	
    	tiles = loadTiles(folderPath, imgSuffix);
    	
    }
    
    public Tile getTile(int tileIndex) { return tiles.get(tileIndex); }

    @Override
    public void scaleImages(int width, int height) {
        
        // SCALING THE IMAGES INSIDE EACH TILE
        for (Tile tile : tiles) {
        	
            tile.setImg(scaleImage(tile.getImg(), width, height));
            
        }

    }

    private ArrayList<Tile> loadTiles(String folderPath, String imgSuffix) {
    	
    	// GET EACH IMAGE PATH AND DATA FROM THE FOLDER
    	ArrayList<String> imgPaths = loadFilePaths(folderPath, imgSuffix);
    	ArrayList<String[]> imgData = loadImgData(folderPath, "txt");
    	
        ArrayList<Tile> tempTiles = new ArrayList<Tile>();
        
        for (int tileIx = 0; tileIx < imgPaths.size(); tileIx++) {
        	
        	String[] data = imgData.get(tileIx);
        	int collision = Integer.parseInt(data[1]);
        	tempTiles.add(new Tile(imgPaths.get(tileIx), intToBool(collision)));
        
        }
          
        return tempTiles;

    }
    
    private boolean intToBool(int input) {
    	
    	return input == 1;
    	
    }
   
}
