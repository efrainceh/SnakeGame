package panels;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.*;

import customComponents.*;
import game.game.Game;
import game.game.GameComponents;
import game.game.GameMode;
import game.game.GameScores;
import game.game.GameSettings;
import game.game.TextWindow;
import lombok.NoArgsConstructor;
import snake.*;
import panels.CardPanel.PanelHandler;

@NoArgsConstructor
public class GamePanel extends BasePanel implements ComponentListener {

    private static final long serialVersionUID = 5623648405381760609L;
    
	// PANEL SETTINGS
    private static final int scorePanelWidth = tileSize * tilesInScorePanel;
    private static final int boardPanelWidth = tileSize * tilesPerCol;

    private Game game;
    private ScorePanel scorePanel;
    private BoardPanel boardPanel;

    public GamePanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings);
        game = new Game(this, gameSettings);
        loadPanels();
        
    }
    
    private void loadPanels() {

    	setLayout(new BorderLayout());
        scorePanel = new ScorePanel(gameSettings);
        scorePanel.loadGameScores();
        boardPanel = new BoardPanel(gameSettings);
        boardPanel.loadGameComponents();
        boardPanel.loadTextWindow();
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

        game.start();
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Unimplemented method 'componentHidden'");
    }
    
    public void updateNormal(GameScores gameScores, GameComponents components) {
    	
    	scorePanel.updateNormal(gameScores);
    	boardPanel.update(components);
    	
    }
    
    public void updateAdventure(GameScores gameScores, GameComponents components) {
    	
    	scorePanel.updateAdventure(gameScores);
    	boardPanel.update(components);
    	
    }
    
    public void updateNpc(GameScores gameScores, GameComponents components) {
    	
    	scorePanel.updateNpc(gameScores);
    	boardPanel.update(components);
    	
    }
    
    public void nextMap() {
    
        panelHandler.goGame(gameSettings);
    	
    }
    
    public void endGame(GameScores gameScores) {
    		
        panelHandler.goEnd(gameSettings, gameScores);
    	
    }

    
    
    public class BoardPanel extends JPanel {
        
        private static final long serialVersionUID = -1048222002826346147L;
        
        GameComponents gameComponents;
        TextWindow window;

        public BoardPanel(GameSettings gameSettings) {

            setPreferredSize(new Dimension(boardPanelWidth, screenHeight));
            setDoubleBuffered(true);
            setFocusable(true);
            
        }
        
        public void update(GameComponents components) {
        	
        	this.gameComponents = components;
        	repaint();
        	
        }
        
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            gameComponents.draw(g2);
            window.draw(g2);
            g2.dispose();

        }
        
        private void loadGameComponents() {
        	
        	this.gameComponents = game.getGameComponents();
        	
        	for (BaseSnake snake : gameComponents.getSnakes()) {
        		
        		snake.addToPanel(this);
        		
        	}
        	
        }     
        
        private void loadTextWindow() {
        	
        	this.window = game.getWindow();
        	
        }
        
    }
    
    

    public class ScorePanel extends JPanel {

        private static final long serialVersionUID = 7143935136454070146L;

		// BACKGROUND IMAGE
        private final Image img = loadImage("static/image/components/snake_hat_medium.png");

        GameScores gameScores;
        
        private ScoreTable scoreTable;
        private JLabel levelLabel;
        private JLabel tacoLabel;
        private JLabel timeLabel;
        
        public ScorePanel(GameSettings gameSettings) {

            setPreferredSize(new Dimension(scorePanelWidth, screenHeight));

        }
        
        private void loadGameScores() {
        	
        	this.gameScores = game.getGameScores();
        	String level = "LEVEL " + Integer.toString(gameSettings.getMapIndex() + 1);
        	String tacoCount = String.format("%s / %s", 0, gameSettings.getAdventureGoal());
        	String time = gameScores.getRuntime();
        	
        	boolean npcMode = false;
        	GameMode gameMode = gameSettings.getGameMode();
        	
        	switch (gameMode) {
        	
				case NORMAL:
					scoreTable = new ScoreTable(gameSettings.getNumberOfPlayers(), npcMode);
					add(scoreTable.getTableInScrolllPane());
					break;
				
				case ADVENTURE:
					setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	                levelLabel = new JLabel(level);
	                tacoLabel = new JLabel(tacoCount);
	                timeLabel = new JLabel(time);
	                levelLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	                tacoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	                timeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	                add(levelLabel);
	                add(tacoLabel);
	                add(timeLabel);
					break;
				
				case NPC:
					npcMode = true;
					scoreTable = new ScoreTable(gameSettings.getNumberOfPlayers(), npcMode);
					add(scoreTable.getTableInScrolllPane());
					break;
			
			}
             	
        }

        public void updateNormal(GameScores gameScores) {
        	
        	int scoresCol = 1;
        	
        	ArrayList<Integer> scores = gameScores.getScores();
        	
        	for (int scoreIx = 0; scoreIx < scores.size(); scoreIx++) {
        		
        		scoreTable.setValueAt(Integer.toString(scores.get(scoreIx)), scoreIx, scoresCol);
        	
        	}

        }
        
        public void updateAdventure(GameScores gameScores) {
        	
        	ArrayList<Integer> scores = gameScores.getScores();

        	timeLabel.setText(gameScores.getRuntime());
        	tacoLabel.setText(String.format("%s / %s", scores.get(0), gameSettings.getAdventureGoal()));
        	
        }
        
        public void updateNpc(GameScores gameScores) {
        	
        	updateNormal(gameScores);
        	
        }

        public void paintComponent(Graphics g) {
            
            g.drawImage(img,(int)(scorePanelWidth * 0.05), (int)(screenHeight * 0.30), null);
            
            if (gameSettings.getNumberOfPlayers() == 2) {
            	
                g.drawImage(img,(int)(scorePanelWidth * 0.05), (int)(screenHeight * 0.65), null);
                
            }
            
        }
        
       

    }
    
}

