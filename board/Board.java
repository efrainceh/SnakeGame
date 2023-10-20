package board;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import lombok.Getter;

import handlers.FolderHandler;

@Getter
public class Board {

	 
    private TileManager tileManager = new TileManager("static/image/tiles", "png");

    // BOARD VARIABLES
    private int mapIndex;
    private final int numberOfRows;
    private final int numberOfCols;
    private final int tileSize;
    private int[][] mapTileNum;
    private boolean[][] board;
    private final ArrayList<String> mapPaths;

    public Board(int rows, int cols, int tileSize, String folderMap) {

        this.numberOfRows = rows;
        this.numberOfCols = cols;
        this.tileSize = tileSize;
        tileManager.scaleImages(tileSize, tileSize);
        mapPaths = loadMapPaths(folderMap);
        
    }
    
    public int getNumberOfMaps() { return mapPaths.size(); }
    
    public boolean isLastMap() { return mapIndex == mapPaths.size() - 1; }
    
    public boolean isCollision(int row, int col) { return board[row][col]; }
    
    public void updateCell(int row, int col, boolean collision) { board[row][col] = collision; }

    public void loadMap(int mapIndex) {

        this.mapIndex = mapIndex;
        mapTileNum = loadMapTile(this.mapIndex);
        board = loadBoard();

    }
    
    public void draw(Graphics2D g2) {

        for (int row = 0; row < numberOfRows; row++) {
        	
            drawRowOfTiles(g2, row);
            
        }
        
    }
    
    private ArrayList<String> loadMapPaths(String folderPath) {

        FolderHandler folderH = new FolderHandler();
        return folderH.getFilePaths(folderPath, "txt");
    
    }

    private int[][] loadMapTile(int mapIndex) {
    	
    	int[][] tempMapTile = new int[numberOfRows][numberOfCols];

        try {
        	
            // OPEN MAP FILE
            String mapPath = mapPaths.get(mapIndex);
            InputStream is = new FileInputStream(mapPath);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < numberOfRows; row++) {
            	
                // PARSE ONE LINE OF NUMBERS
                String line = fileReader.readLine();
                tempMapTile[row] = convertToInt(line);
                
            }

            fileReader.close();

        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
        
		return tempMapTile;
		
    }
    
    private int[] convertToInt(String line) {
    	
    	String stringNumbers[] = line.split(" ");
    	int[] numLine = new int[numberOfCols];

        for (int col = 0; col < numberOfCols; col++) {
        	
        	numLine[col] = Integer.parseInt(stringNumbers[col]);
            
        }
        
        return numLine;

    }

    private boolean[][] loadBoard() {
    	
    	boolean[][] tempBoard = new boolean[numberOfRows][numberOfCols];

        for (int row = 0; row < numberOfRows; row++) {
        	
            for (int col = 0; col < numberOfCols; col++) {
            	
                int tileIx = mapTileNum[row][col];
                tempBoard[row][col] = tileManager.getTile(tileIx).isCollision();
                
            }
            
        }
        
        return tempBoard;

    }

    private void drawRowOfTiles(Graphics2D g2, int row) {

        for (int col = 0; col < numberOfCols; col++) {
        	
            int tileNum = mapTileNum[row][col];
            Tile tile = tileManager.getTile(tileNum);
            g2.drawImage(tile.getImg(), col * tileSize, row * tileSize, null);
            
        }

    }

    /* 
    	DEBUGGING
    */

    public void printBoard() {
        for (int row = 0; row < numberOfRows; row++) {
            System.out.print("( ");
            for (int col = 0; col < numberOfCols; col++) {
            	
            	System.out.print(board[row][col]);
                
                if (col != numberOfCols - 1) {
                    
                    System.out.print(" , ");
                
                } 
            }
            System.out.print(" )");
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------");
    }
    
    public void printMapTile() {
        for (int row = 0; row < numberOfRows; row++) {
            System.out.print("( ");
            for (int col = 0; col < numberOfCols; col++) {
            	
            	System.out.print(mapTileNum[row][col]);
                
                if (col != numberOfCols - 1) {
                    
                    System.out.print(" , ");
                
                } 
            }
            System.out.print(" )");
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------");
    }
    
}
