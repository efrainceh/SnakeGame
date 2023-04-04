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
    static final int gameOver = 2;
    static final int nextLevel = 3;
    static final int finished = 4;
    int gameStatus = 0;

    static BoardStatus boardStatus;
    static GameTimer timer;

    ScorePanel scorePanel;
    BoardPanel boardPanel;
    Thread gameThread;

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
        playMusic(0);
        
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

                // ADD WAIT TIME TO DISPLAY THE LEVEL WINDOW AT THE BEGGINING OF EACH ADVENTURE MAP
                if (gameStatus == hold) {
                    hold(2);
                    gameStatus = running;
                }

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

    private void hold(int seconds) {

        try {

            Thread.sleep(3000);

        } catch(InterruptedException e) {

            e.printStackTrace();

        }

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

        gameStatus = gameOver;
        gameThread = null;
        gameSettings.scoreTable = scorePanel.getScoreTable();
        hold(2);
        stopMusic();
        panelHandler.goEnd(gameSettings);
        
    }

    private void nextMap() {

        gameThread = null;
        gameSettings.scoreTable = scorePanel.getScoreTable();

        // GO TO NEXT MAP, OR THE END IF IT WAS THE LAST MAP
        int numberOfMaps = boardPanel.getNumberOfMaps();
        if (gameSettings.mapIndex < numberOfMaps - 1) {
            gameSettings.mapIndex++;
            gameStatus = nextLevel;
            hold(2);
            stopMusic();
            panelHandler.goGame(gameSettings);
        } else {
            gameStatus = finished;
            hold(2);
            stopMusic();
            panelHandler.goEnd(gameSettings);
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

    private void playMusic(int soundIx) {

        soundHandler.setFile(soundIx);
        soundHandler.play();
        soundHandler.loop();

    }

    private void stopMusic() {

        soundHandler.stop();

    }

   

    public class ScorePanel extends JPanel {

        // BACKGROUND IMAGE
        final Image img = utilityTool.loadImage("static/image/components/snake_hat_medium.png");

        // NORMAL GAME MODE SCORING
        ArrayList<Integer> scores;
        ScoreTable scoreTable;

        // ADVENTURE GAME MODE SCORING
        static final int tacoGoal = 20;
        int tacoCount = 0;
        JLabel levelLabel;
        JLabel tacoLabel;
        JLabel timeLabel;

        int numberOfSnakes;
        
        public ScorePanel(int numberOfSnakes) {

            setPreferredSize(new Dimension(scorePanelWidth, screenHeight));

            if (gameSettings.adventure) {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                levelLabel = new JLabel("LEVEL " + Integer.toString(gameSettings.mapIndex + 1));
                tacoLabel = new JLabel(String.format("%s / %s", tacoCount, tacoGoal));
                timeLabel = new JLabel(timer.getRuntime());
                levelLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                tacoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                timeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                add(levelLabel);
                add(tacoLabel);
                add(timeLabel);
            } else {
                scoreTable = new ScoreTable(numberOfSnakes);
                add(scoreTable.getTableInScrolllPane());
                scores = new ArrayList<Integer>(Collections.nCopies(numberOfSnakes, 0));
            }

            this.numberOfSnakes = numberOfSnakes;
        
        }

        public ScoreTable getScoreTable() { return scoreTable; }

        public boolean isGameOver() {

            // THIS FUNCTION IS ONLY CALLED WHEN THE GAME IS IN ADVENTURE MODE
            return tacoCount == tacoGoal;

        }

        public void update() {

            // IF IN ADVENTURE MODE, ALWAYS UPDATE THE TIMER
            if (gameSettings.adventure) {
                
                timeLabel.setText(timer.getRuntime());

                if (boardStatus.hasEaten) {
                    tacoCount++;
                    tacoLabel.setText(String.format("%s / %s", tacoCount, tacoGoal));
                    // RESET FOOD STATUS TO FALSE  
                    boardStatus.snakeFoodStatus.set(0, false);
                }
            
            } else if (boardStatus.hasEaten) {
                // UPDATE SCORES WHEN AT LEAST ONE SNAKE HAS EATEN AND NOT IN ADVENTURE MODE    
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

            // ADD FOOD TO BOARD.
            addFood();

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
            // ORDER OF KEYS: UP, DOWN, RIGHT, LEFT
            Snake snake = new Snake(this, startRow, startCol);
            snake.addKeyHandler(keys);    
            snakes.add(snake);
            snakeFoodStatus.add(false);

            // UPDATE BOARD TILES AS COLLISION. SNAKE'S HEAD TILE POSITION ON THE BOARD
            // REMAINS SET AS COLLISION FALSE
            LinkedList<SnakePart> snakeParts = snake.getSnakeParts();
            for (int partIx = 1; partIx < snakeParts.size(); partIx++) {
                board.updateCell(snakeParts.get(partIx), true);
            }

        }

        private void addFood() {

            // foodUpdate PLACES FOOD IN A RANDOM NO COLLISION TILE. HOWEVER, OCCASIONALLY FOOD IS
            // PLACED IN THE CURRENT HEAD SNAKE POSITION (SINCE HEAD COLLISION IS SET TO FALSE). THIS
            // WHILE LOOP PREVENTS THAT

            food = foodManager.updateFood();
            while (foodIsInSnakeHeadTile()) {
                food = foodManager.updateFood();
            }
            board.addFood(food);

        }

        /* 
            UPDATE METHODS
        */

        private void update() {

            boolean haveEaten = snakesHaveEaten();
            if (haveEaten) {
                addFood();
            }
            snakesUpdate();
            boardStatus.update(haveEaten, snakeFoodStatus, food.getPoints());       // NEED TO CHECK THIS FUNCTION
        }

        private boolean snakesHaveEaten() {

            // RETURNS TRUE IF AT LEAST ONE SNAKE HAS EATEN THE FOOD
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

            // DRAW BEGGINING AND END OF GAME WINDOWS
            if (gameSettings.adventure && gameStatus == hold) {
                String text = "LEVEL " + Integer.toString(board.getMapIndex() + 1);
                drawTextWindow(g2, text);
            }
            drawEndWindow(g2);
           
            // long e = System.nanoTime();
            // System.out.println(e-s);

            g2.dispose();

        }

        private void drawSnakes(Graphics2D g2) {

            for (Snake snake : snakes) {
                snake.draw(g2);
            }

        }

        private void drawEndWindow(Graphics2D g2) {

            switch (gameStatus) {
                case gameOver:
                    drawTextWindow(g2, "GAME OVER");
                    break;
                case nextLevel:
                    drawTextWindow(g2, "NEXT LEVEL");
                    break;
                case finished:
                    drawTextWindow(g2, "YOU WIN!");
                    break;
                default:
                    // Nothing is drawn
                    break;
            }

        }

        private void drawTextWindow(Graphics2D g2, String text) {

            // RECTANGLE POSITION AND SIZE
            int x = (int) (boardPanelWidth * 0.25);
            int y = (int) (screenHeight * 0.35);
            int width = (int) (boardPanelWidth * 0.5);
            int height = (int) (screenHeight * 0.3);
            utilityTool.drawRect(g2, x, y, width, height, text);

        }
    
        /* 
            RUNNING GAME METHODS
        */

        public boolean gameStarted() {

            for (Snake snake : snakes) {
                if (snake.isMoving())
                    return true;
                }
            return false;
        
        }

        private Snake isGameOver() {

            // IN A MULTIPLAYER GAME, I WANT TO KNOW WHICH SNAKE HAD A COLLISION SO THAT THE PLAYER
            // CAN BE PENALIZED IN THE FUTURE
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
