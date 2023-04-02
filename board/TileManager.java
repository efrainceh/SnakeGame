package board;

import java.util.ArrayList;

import main.FolderHandler;
import utilityTool.UtilityTool;

public class TileManager {
    
    static ArrayList<String> imgPaths = loadImgPaths();
    static ArrayList<Tile> tiles = loadTiles();
    UtilityTool utilityTool = new UtilityTool();

    public Tile getTile(int tileIndex) { return tiles.get(tileIndex); }

    private static ArrayList<String> loadImgPaths() {

        FolderHandler folderH = new FolderHandler("static/image/tiles");        
        ArrayList<String> tempImgPaths = folderH.getFilePaths("png");

        return tempImgPaths;
        
    }

    private static ArrayList<Tile> loadTiles() {

        ArrayList<Tile> tempTiles = new ArrayList<Tile>();
            
            // FIRST 10 TILES ARE JUST EMPTY FILLLERS SO MAPS CAN START FROM NUMBER 10
            for (int i = 0; i < 10; i++) {
                tempTiles.add(new Tile(imgPaths.get(i), false));        // EMPTY TILE
            }
            
            // ADD ACTUAL TILES
            tempTiles.add(new Tile(imgPaths.get(10), false));        // GRASS
            tempTiles.add(new Tile(imgPaths.get(11), false));        // GRASS
            tempTiles.add(new Tile(imgPaths.get(12), false));        // TREE
            tempTiles.add(new Tile(imgPaths.get(13), true));        // WATER
            tempTiles.add(new Tile(imgPaths.get(14), true));        // WATER_TOP_EDGE
            tempTiles.add(new Tile(imgPaths.get(15), true));        // WATER_BOTTOM_EDGE
            tempTiles.add(new Tile(imgPaths.get(16), true));        // WATER_RIGHT_EDGE
            tempTiles.add(new Tile(imgPaths.get(17), true));        // WATER_LEFT_EDGE
            tempTiles.add(new Tile(imgPaths.get(18), true));        // WATER_BL_CORNER
            tempTiles.add(new Tile(imgPaths.get(19), true));        // WATER_BR_CORNER
            tempTiles.add(new Tile(imgPaths.get(20), true));        // WATER_TL_CORNER
            tempTiles.add(new Tile(imgPaths.get(21), true));        // WATER_TR_CORNER
            tempTiles.add(new Tile(imgPaths.get(22), true));        // WATER_CORNER_EDGE
            tempTiles.add(new Tile(imgPaths.get(23), true));        // WATER_CORNER_EDGE
          
        return tempTiles;

    }

    public void scaleImages(int width, int height) {
        
        // SCALING THE IMAGES INSIDE EACH TILE
        for (Tile tile : tiles) {
            tile.img = utilityTool.scaleImage(tile.img, width, height);
        }

    }
   
}
