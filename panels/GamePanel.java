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
import main.SoundManager.Sound;
import snake.*;
import panels.CardPanel.PanelHandler;


public class GamePanel extends BasePanel implements Runnable, ComponentListener {

    // PANEL SETTINGS
    static final int scorePanelWidth = tileSize * tilesInScorePanel;
    static final int boardPanelWidth = tileSize * tilesPerCol;

    // RUN SETTINGS 
    double drawInterval = 1000000000/gameSettings.FBS;
    double delta = 0;
    double deltaNPC = 0;
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

        playMusic(Sound.BACKGROUND);
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

            // NPC SNAKE ONLY STARTS MOVING AFTER THE PLAYER HAS STARTED MOVING
            // isMoving IS SET TO TRUE SO THAT NPC MOVES AND ONLY ONE CALL IS MADE
            // TO newNPCPath();
            if (gameSettings.npc && boardPanel.gameStarted() && !boardPanel.npc.isMoving()) {
                boardPanel.newNPCPath();
                boardPanel.npc.setMoving(true);
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
        deltaNPC += (currentTime - lastTime) / drawInterval;
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
        stopMusic();
        playSE(Sound.GAME_OVER);
        hold(2);
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
            stopMusic();
            playSE(Sound.NEXT_LEVEL);
            hold(2);
            panelHandler.goGame(gameSettings);
        } else {
            gameStatus = finished;
            stopMusic();
            playSE(Sound.NEXT_LEVEL);
            hold(2);
            panelHandler.goEnd(gameSettings);
        }

    }

    private void updateGame() {

        // npcUpdate IS CONTROLLED BY A BIGGER DELTA IN ORDER TO SLOW DOWN 
        // IT'S MOVEMENT, OTHERWISE THE NPC IS TOO HARD TO BEAT
        if (gameSettings.npc && deltaNPC >= 1.5) {

            boardPanel.npcUpdate();
            deltaNPC -= 1.5;

        }

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

        // NORMAL GAME MODE SCORING
        ArrayList<Integer> scores;
        ScoreTable scoreTable;
        int npcScore;

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
                scoreTable = new ScoreTable(numberOfSnakes, gameSettings.npc);
                add(scoreTable.getTableInScrolllPane());
                scores = new ArrayList<Integer>(Collections.nCopies(numberOfSnakes, 0));
                npcScore = 0;
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
                updateSnakeScores();
                updateNPCScores();
            
            }

        }

        private void updateSnakeScores() {

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

        private void updateNPCScores() {

            if (boardStatus.npcFoodStatus) {

                // UPDATE SCORE
                npcScore += boardStatus.points;

                // UPDATE SCORE TABLE, NPC IS ALWAYS LAST ROW
                scoreTable.setValueAt(Integer.toString(npcScore), 1, 1);

                // RESET FOOD STATUS TO FALSE  
                boardStatus.npcFoodStatus = false;


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
        
        // FOR NPC SNAKE
        PathFinder pathFinder;
        SnakeNPC npc;
        boolean npcFoodStatus;

        public BoardPanel(int mapIndex, int numberOfPlayers) {

            setPreferredSize(new Dimension(boardPanelWidth, screenHeight));
            setDoubleBuffered(true);
            setFocusable(true);

            board = new Board(this, mapIndex, tilesPerRow, tilesPerCol);
            foodManager = new FoodManager(this);
            foodManager.scaleImages(tileSize, tileSize);
            pathFinder = new PathFinder();
            snakes = new ArrayList<Snake>();
            snakeFoodStatus = new ArrayList<Boolean>();

            // ADD USER SNAKES TO BOARD.
            addSnake(3, 8, new String[] {"UP", "DOWN", "RIGHT", "LEFT"});
            if (numberOfPlayers == 2) {
                addSnake(13, 8, new String[] {"W", "S", "D", "A"});
            }

            // ADD SNAKE NPC
            if (gameSettings.npc) {
                addNPC(13, 8);
            }

            // ADD FOOD TO BOARD. SNAKES NEED TO BE ADDED
            // BEFORE FOOD.
            addFood();

        }

        public int getNumberOfRows() { return tilesPerCol; }
        public int getNumberOfCols() { return tilesPerRow; }
        public int getTileSize() { return tileSize; }
        public int getNumberOfMaps() { return board.getNumberOfMaps(); }
        public Board getBoard() { return board; }

        private void addSnake(int startRow, int startCol, String[] keys) {

            Snake snake = new Snake(this, startRow, startCol);
            // ORDER OF KEYS: UP, DOWN, RIGHT, LEFT
            snake.addKeyHandler(keys);    
            snake.scaleImages(tileSize, tileSize);
            snakes.add(snake);
            snakeFoodStatus.add(false);
            updateBoard(snake);
            
        }

        private void addNPC(int startRow, int startCol) {

            npc = new SnakeNPC(this, startRow, startCol);
            npc.scaleImages(tileSize, tileSize);
            npcFoodStatus = false;
            updateBoard(npc);

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

        public boolean foodIsInSnakeHeadTile() {

            // CHECK PLAYER SNAKES
            for (Snake snake : snakes) {
                if (food.row == snake.getHeadRow() && food.col == snake.getHeadCol()) {
                    return true;
                }
            }

            // CHECK NPC SNAKE
            if (gameSettings.npc && food.row == npc.getHeadRow() && food.col == npc.getHeadCol()) {
                return true;
            }

            return false;

        }

        private void updateBoard(Snake snake) {

            // UPDATE BOARD TILES AS COLLISION. SNAKE'S HEAD TILE POSITION ON THE BOARD
            // REMAINS SET AS COLLISION FALSE  
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
                
                playSE(Sound.EAT);
                addFood();

                // CALCULATE A PATH FOR NPC TO THE NEW FOOD POSITION
                if (gameSettings.npc) {
                    newNPCPath();
                }
               
            }
            snakesUpdate();
            boardStatus.update(haveEaten, snakeFoodStatus, food.getPoints());       // NEED TO CHECK THIS FUNCTION
        }

        private boolean snakesHaveEaten() {

            // RETURNS TRUE IF AT LEAST ONE SNAKE HAS EATEN THE FOOD,
            // INCLUDING NPC SNAKE
            boolean oneSnakeAte = false;
            for (int snakeIx = 0; snakeIx < snakes.size(); snakeIx++) {
                if (snakes.get(snakeIx).ate()) {
                    oneSnakeAte = true;
                    snakeFoodStatus.set(snakeIx, true);
                }
            }

            if (gameSettings.npc && npc.ate()) {
                oneSnakeAte = true;
                npcFoodStatus = true;
                boardStatus.npcFoodStatus = true;
            }

            return oneSnakeAte;

        }

        private void snakesUpdate() {

            // UPDATE ONLY PLAYERS SNAKES
            for (int snakeIx = 0; snakeIx < snakes.size(); snakeIx++) {
                boolean hasEaten = snakeFoodStatus.get(snakeIx);
                snakes.get(snakeIx).update(hasEaten);
            }

        }

        public void npcUpdate() {

            npc.update(npcFoodStatus);
            npcFoodStatus = false;

        }

        /* 
            DRAW METHODS
        */

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            board.draw(g2);
            foodManager.draw(g2);
            drawSnakes(g2);
            if (gameSettings.npc) {
                npc.draw(g2);
            }
            
            // DRAW BEGGINING AND END OF GAME WINDOWS
            if (gameSettings.adventure && gameStatus == hold) {
                String text = "LEVEL " + Integer.toString(board.getMapIndex() + 1);
                drawTextWindow(g2, text);
            }
            drawEndWindow(g2);

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

        private void newNPCPath() {

            // COPY THE CURRENT STATE OF THE BOARD
            boolean[][] boardCopy = Arrays.stream(board.getBoard()).map(boolean[]::clone).toArray(boolean[][]::new);

            // FIND THE SHORTEST PATH TO food
            pathFinder.loadMatrix(boardCopy);
            LinkedList<ArrayList<Integer>> pathToFood = pathFinder.findPath(npc.getHeadRow(), npc.getHeadCol(), food.row, food.col);
            
            // DISCARD THE FIRST TILE POSITION, IT REPRESENTS THE CURRENT HEAD TILE
            pathToFood.remove();

            // LOAD THE PATH FOR THE NPC TO FOLLOW
            npc.loadPath(pathToFood);

        }

    }

    public class BoardStatus {

        public boolean hasEaten = false;
        public ArrayList<Boolean> snakeFoodStatus = new ArrayList<Boolean>();
        public boolean npcFoodStatus = false;
        public int points = 0;

        public void update(boolean hasEaten, ArrayList<Boolean> snakeFoodStatus, int points) {

            this.hasEaten = hasEaten;
            this.snakeFoodStatus = snakeFoodStatus;
            this.points = points;

        }

    }

}
