package panels;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;
import javax.swing.*;

import board.*;
import customComponents.*;
import food.*;
import main.*;
import snake.*;
import panels.CardPanel.PanelHandler;


public class GamePanel extends BasePanel implements Runnable, ComponentListener {

    static BoardStatus boardStatus;
    static GameTimer timer;

    ScorePanel scorePanel;
    BoardPanel boardPanel;
    Thread gameThread;

    // PANEL SETTINGS
    static final int scorePanelWidth = tileSize * tilesInScorePanel;
    static final int boardPanelWidth = tileSize * tilesPerCol;

    // RUN SETTINGS 
    double drawInterval = 1000000000/gameSettings.FBS;
    double delta = 0;
    long lastTime = 0;
    long currentTime = 0;

    // GAME STATUS
    static final int hold = 0;
    static final int running = 1;
    int gameStatus = 0;

    public GamePanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings);
        setLayout(new BorderLayout());
        loadPanel();

    }
    
    private void loadPanel() {

        scorePanel = new ScorePanel(gameSettings.numberOfPlayers);
        boardPanel = new BoardPanel(gameSettings.mapIndex, gameSettings.numberOfPlayers);
        boardStatus = new BoardStatus();
        timer = gameSettings.timer;
        add(scorePanel, BorderLayout.EAST);
        add(boardPanel, BorderLayout.WEST);

        // ADD LISTENER SO THE GAME RUNS WHEN THIS PANEL IS SHOWN
        addComponentListener(this);

    }

    @Override
    public void componentResized(ComponentEvent e) {
        //throw new UnsupportedOperationException("Unimplemented method 'componentResized'");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //throw new UnsupportedOperationException("Unimplemented method 'componentMoved'");
    }

    @Override
    public void componentShown(ComponentEvent e) {

        startGameThread();
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Unimplemented method 'componentHidden'");
    }

    private void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
        
    }

    @Override
    public void run() {

        setRunInitialSettings();
        
        // GAME LOOP
        while (gameThread != null) {
            
            // ADVENTURE MODE EVALUATION
            if (gameSettings.adventure) {

                // START THE TIMER ONLY ONCE, AFTER A VALID KEY HAS BEEN PRESSED
                if (boardPanel.gameStarted() && timer.hasNotStarted()) {
                    timer.start();
                }

                // ADVENTURE FINISHES EITHER BY REACHING THE POINT GOAL OR PLAYER FAILED
                // nextMap HANDLES WHERE TO GO BASED ON HOW THE ADVENTURE FINISHED
                if (isAdventureOver()) {
                    timer.stop();
                    nextMap();
                    break;
                }

            }

            // IN NORMAL MODE, IF GAME ENDED BECAUSE PLAYER FAILED
            if (isGameOver()) {
                setEndGame();
                break;
            }
            
            // CONTINUE GAME
            updateRunSettings();
            updateGame();

        }

    }

    private void setRunInitialSettings() {

        delta = 0;
        lastTime = System.nanoTime();

    }

    private void updateRunSettings() {

        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        lastTime = currentTime;

    }

    private boolean isGameOver() {

        return boardPanel.isGameOver() != null;

    }

    private boolean isAdventureOver() {

        return scorePanel.isGameOver();

    }

    private void setEndGame() {
        
        try {

            Thread.sleep(1000);             // WAIT A SECOND SO THAT YOU CAN SEE HOW THE GAME ENDED
            gameThread = null;
            gameSettings.scoreTable = scorePanel.getScoreTable();
            panelHandler.goEnd(gameSettings);
        
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    private void nextMap() {

        try {

            Thread.sleep(1000);             // WAIT A SECOND SO THAT YOU CAN SEE HOW THE GAME ENDED
            gameThread = null;
            gameSettings.scoreTable = scorePanel.getScoreTable();

            // GO TO NEXT MAP, OR THE END IF IT WAS THE LAST MAP
            int numberOfMaps = boardPanel.getNumberOfMaps();
            if (gameSettings.mapIndex < numberOfMaps - 1) {
                gameSettings.mapIndex++;
                panelHandler.goGame(gameSettings);
            } else {
                panelHandler.goEnd(gameSettings);
            }
        
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void updateGame() {

        if (delta >= 1) {
            // THESE UPDATES ARE DEPENDENT ON THE VALUES IN boardStatus
            boardPanel.update();
            scorePanel.update();
            boardPanel.repaint();
            delta--;
        }

    }

    public class ScorePanel extends JPanel {

        // BACKGROUND IMAGE
        final Image img = utilityTool.loadImage("static/image/components/snake_hat_medium.png");

        // ADVENTURE LIMITS
        static final int maxAdventureScore = 200;

        ArrayList<Integer> scores;
        ScoreTable scoreTable;
        int numberOfSnakes;
        JLabel timeLabel;
        
        public ScorePanel(int numberOfSnakes) {

            setPreferredSize(new Dimension(scorePanelWidth, screenHeight));

            // ADD TABLE TO PANEL
            scoreTable = new ScoreTable(numberOfSnakes);
            add(scoreTable.getTableInScrolllPane());

            // ADD TIMER
            if (gameSettings.adventure) {
                timeLabel = new JLabel(timer.getRuntime());
                add(timeLabel);
            }

            scores = new ArrayList<Integer>(Collections.nCopies(numberOfSnakes, 0));
            this.numberOfSnakes = numberOfSnakes;
        
        }

        public ScoreTable getScoreTable() { return scoreTable; }

        public boolean isGameOver() {
            // GAME ENDS IF A SNAKE HAS REACHED THE MAX SCORE. THIS FUNCTION IS ONLY CALLED
            // WHEN THE GAME IS IN ADVENTURE MODE
            return scores.contains(maxAdventureScore);

        }

        public void update() {

            // IF IN ADVENTURE MODE, UPDATE THE TIMER
            if (gameSettings.adventure) {
                timeLabel.setText(timer.getRuntime());
            }

            // UPDATE SCORES WHEN AT LEAST ONE SNAKE HAS EATEN
            if (boardStatus.hasEaten) {
                updateScores();
            }

        }

        private void updateScores() {

            ArrayList<Boolean> snakeFull = boardStatus.snakeFoodStatus;
            for (int snakeIx = 0; snakeIx < snakeFull.size(); snakeIx++) {
                if (snakeFull.get(snakeIx)) {
                    int currentScore = scores.get(snakeIx);
                    int newScore = currentScore + boardStatus.points;

                    // UPDATE SCORE ARRAY
                    scores.set(snakeIx, newScore);

                    // UPDATE SCORE TABLE
                    scoreTable.setValueAt(Integer.toString(newScore), snakeIx, 1);

                    // RESET FOOD STATUS TO FALSE  
                    boardStatus.snakeFoodStatus.set(snakeIx, false);
                }
            }

        }

        public void paintComponent(Graphics g) {
            
            g.drawImage(img,(int)(scorePanelWidth * 0.05), (int)(screenHeight * 0.30), null);
            if (numberOfSnakes == 2) {
                g.drawImage(img,(int)(scorePanelWidth * 0.05), (int)(screenHeight * 0.65), null);
            }
            
        }

    }

    public class BoardPanel extends JPanel {
        
        Board board;
        Food food;
        FoodManager foodManager;
        ArrayList<Snake> snakes;
        ArrayList<Boolean> snakeFoodStatus;

        // LEVEL WINDOW COUNTER
        int counter = 0;

        public BoardPanel(int mapIndex, int numberOfPlayers) {

            setPreferredSize(new Dimension(boardPanelWidth, screenHeight));
            setDoubleBuffered(true);
            setFocusable(true);

            board = new Board(this, mapIndex, tilesPerRow, tilesPerCol);
            foodManager = new FoodManager(this);
            foodManager.scaleImages(tileSize, tileSize);
            snakes = new ArrayList<Snake>();
            snakeFoodStatus = new ArrayList<Boolean>();

            // ADD SNAKES TO BOARD.
            addSnake(3, 8, new String[] {"UP", "DOWN", "RIGHT", "LEFT"});
            if (numberOfPlayers == 2) {
                addSnake(13, 8, new String[] {"W", "S", "D", "A"});
            }

            // ADD FOOD TO BOARD. foodUpdate PLACES FOOD IN A RANDOM NO COLLISION TILE. HOWEVER, OCCASIONALLY
            // FOOD IS PLACED IN THE CURRENT HEAD SNAKE POSITION (SINCE HEAD COLLISION IS FALSE). THIS
            // WHILE LOOP PREVENTS THAT
            food = foodManager.updateFood();
            while (foodIsInSnakeHeadTile()) {
                food = foodManager.updateFood();
            }
            board.addFood(food);

        }

        public boolean foodIsInSnakeHeadTile() {
            for (Snake snake : snakes) {
                if (food.row == snake.getHeadRow() && food.col == snake.getHeadCol()) {
                    return true;
                }
            }
            return false;
        }

        public int getNumberOfRows() { return tilesPerCol; }
        public int getNumberOfCols() { return tilesPerRow; }
        public int getTileSize() { return tileSize; }
        public int getNumberOfMaps() { return board.getNumberOfMaps(); }
        public Board getBoard() { return board; }

        public void addSnake(int startRow, int startCol, String[] keys) {

            // Add snake to boardpanel
            // Order of keys: up, down, right, left
            Snake snake = new Snake(this, startRow, startCol);
            snake.addKeyHandler(keys);    
            snakes.add(snake);
            snakeFoodStatus.add(false);

            // Update board tiles as Collision. Snake's head tile position on the board remains as false collision
            LinkedList<SnakePart> snakeParts = snake.getSnakeParts();
            for (int partIx = 1; partIx < snakeParts.size(); partIx++) {
                board.updateCell(snakeParts.get(partIx), true);
            }

        }

        /* 
            UPDATE METHODS
        */

        private void update() {

            boolean haveEaten = snakesHaveEaten();
            if (haveEaten) {
                food = foodManager.updateFood();
                board.addFood(food);         // New food tile is labeled FOOD
            }
            snakesUpdate();
            boardStatus.update(haveEaten, snakeFoodStatus, food.getPoints());       // NEED TO CHECK THIS FUNCTION
        }

        private boolean snakesHaveEaten() {

            boolean oneSnakeAte = false;
            for (int snakeIx = 0; snakeIx < snakes.size(); snakeIx++) {
                if (snakes.get(snakeIx).ate()) {
                    oneSnakeAte = true;
                    snakeFoodStatus.set(snakeIx, true);
                }
            }
            return oneSnakeAte;

        }

        private void snakesUpdate() {

            for (int snakeIx = 0; snakeIx < snakes.size(); snakeIx++) {
                boolean hasEaten = snakeFoodStatus.get(snakeIx);
                snakes.get(snakeIx).update(hasEaten);
            }

        }

        /* 
            DRAW METHODS
        */

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            
            // long s = System.nanoTime();

            board.draw(g2);
            foodManager.draw(g2);
            drawSnakes(g2);
            drawLevelWindow(g2);

            // long e = System.nanoTime();
            // System.out.println(e-s);

            g2.dispose();

        }

        private void drawLevelWindow(Graphics2D g2) {
            if (gameSettings.adventure && gameStatus == hold) {
                // int x = (int) (boardPanelWidth * 0.25);
                // int y = (int) (screenHeight * 0.35);
                // int width = (int) (boardPanelWidth * 0.5);
                // int height = (int) (screenHeight * 0.3);
                //utilityTool.drawWindow(x, y, width, height, g2);

                // THIS DRAWS THE WINDOW FOR APPROXIMATELY 3 SECONDS AFTER THE PANEL IS SHOWN
                counter++;
                double ratio = counter / (double)gameSettings.FBS;
                if (ratio == 1.5) {
                    gameStatus = running;
                }
                
            }
        }

        // private void drawWindow(Graphics2D g2) {

        //     int x = (int) (boardPanelWidth * 0.25);
        //     int y = (int) (screenHeight * 0.35);
        //     int width = (int) (boardPanelWidth * 0.5);
        //     int height = (int) (screenHeight * 0.3);

        //     // DISPLAYED RECTANGLE
        //     g2.setColor(new Color(255, 255, 255, 150));
        //     g2.fillRoundRect(x, y, width, height, 35, 35);

        //     // BORDER
        //     g2.setColor(Color.RED);
        //     g2.setStroke(new BasicStroke(5));
        //     g2.drawRoundRect(x, y, width, height, 25, 25);

        //     // TEXT
        //     int xText = x + (int)(tileSize * 1.2);
        //     int yText = y + tileSize/2 + height/2;
        //     g2.setColor(Color.RED);
        //     g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
        //     String message;
        //     if (board.isLastMap()) {
        //         message = "FINAL";
        //         g2.drawString(message, xText + 10, y + height/2 - 5);
        //         message = "LEVEL!";
        //         g2.drawString(message, xText + 10, y + height/2 + 50 - 5);
        //     } else {
        //         message = "LEVEL " + Integer.toString(board.getMapIndex() + 1);
        //         g2.drawString(message, xText, yText);
        //     }

        // }

        private void drawSnakes(Graphics2D g2) {

            for (Snake snake : snakes) {
                snake.draw(g2);
            }

        }
    
        /* 
            RUNNING GAME METHODS
        */

        public boolean gameStarted() {

            return snakes.get(0).isMoving();
        
        }

        private Snake isGameOver() {
            // In a multiplayer game, I want to know which snake had a collision in order to deduct points in the future
            for (Snake snake : snakes) {
                if (snake.isOutOfPanel() || snake.collided()) {
                    return snake;
                }
            }
            return null;
        }

    }

    public class BoardStatus {

        public boolean hasEaten = false;
        public ArrayList<Boolean> snakeFoodStatus = new ArrayList<Boolean>();
        public int points = 0;

        public void update(boolean hasEaten, ArrayList<Boolean> snakeFoodStatus, int points) {

            this.hasEaten = hasEaten;
            this.snakeFoodStatus = snakeFoodStatus;
            this.points = points;

        }

    }

}
