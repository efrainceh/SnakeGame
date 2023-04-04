package board;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.FolderHandler;
import panels.GamePanel.BoardPanel;

public class Board {

    static final ArrayList<String> mapPaths = loadMapPaths("static/maps");
    TileManager tileManager = new TileManager();

    BoardPanel gp;
    int mapIndex;
    int numberOfRows;
    int numberOfCols;
    int mapTileNum[][];
    Boolean board[][];
    int foodRow;
    int foodCol;

    public Board(BoardPanel gp, int mapIndex, int rows, int cols) {

        this.gp = gp;
        this.mapIndex = mapIndex;
        this.numberOfRows = rows;
        this.numberOfCols = cols;
        this.mapTileNum = new int[gp.getNumberOfCols()][gp.getNumberOfRows()];
        this.board = new Boolean[gp.getNumberOfCols()][gp.getNumberOfRows()];
        tileManager.scaleImages(gp.getTileSize(), gp.getTileSize());
        loadMap(mapIndex);
        loadBoard();

    }

    public int getNumberOfMaps() { return mapPaths.size(); }
    public int getMapIndex() { return mapIndex; }
    public boolean isLastMap() { return mapIndex == mapPaths.size() - 1; }

    private static ArrayList<String> loadMapPaths(String folderPath) {

        FolderHandler folderH = new FolderHandler(folderPath);
        return folderH.getFilePaths("txt");
    
    }

    private void loadMap(int mapIndex) {

        try {
            // OPEN MAP FILE
            String mapPath = mapPaths.get(mapIndex);
            InputStream is = new FileInputStream(mapPath);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < numberOfRows; row++) {
                // PARSE ONE LINE OF NUMBERS
                String line = fileReader.readLine();
                addLineToMapTile(line, row);
            }

            fileReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLineToMapTile(String line, int row) {

        for (int col = 0; col < numberOfCols; col++) {
            String stringNumbers[] = line.split(" ");
            int numbers = Integer.parseInt(stringNumbers[col]);
            mapTileNum[col][row] = numbers;
        }

    }

    private void loadBoard() {

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                int tileNum = mapTileNum[col][row];
                board[row][col] = tileManager.getTile(tileNum).collision;
            }
        }

    }

    /* 
        UPDATE METHODS
    */

    public boolean isCollision(Cell cell) {

        return board[cell.row][cell.col];

    }

    public boolean isCollision(int row, int col) {

        return board[row][col];
    
    }

    public void updateCell(Cell cell, boolean collision) {

        board[cell.row][cell.col] = collision;

    }

    public void updateCell(int row, int col, boolean collision) {

        board[row][col] = collision;

    }

    /* 
        DRAW METHODS
    */

    public void draw(Graphics2D g2) {

        for (int row = 0; row < numberOfRows; row++) {
            drawLineOfTiles(g2, row);
        }
        
    }

    private void drawLineOfTiles(Graphics2D g2, int row) {

        for (int col = 0; col < numberOfCols; col++) {
            int tileNum = mapTileNum[col][row];
            Tile tile = tileManager.getTile(tileNum);
            int size = gp.getTileSize();
            g2.drawImage(tile.img, col * size, row * size, null);
        }

    }

    /* 
        RUNNING GAME METHODS
    */

    public void addFood(Cell cell) {

        foodRow = cell.row;
        foodCol = cell.col;

    }

    public boolean isFood(Cell cell) {

        return foodRow == cell.row && foodCol == cell.col;

    }

    // DEBBUGING

    public void printBoard() {
        for (int row = 0; row < numberOfRows; row++) {
            System.out.print("( ");
            for (int col = 0; col < numberOfCols; col++) {
                System.out.print((board[row][col]));
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
