package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import panels.CardPanel.*;
import customComponents.*;
import game.game.GameMode;
import game.game.GameScores;
import game.game.GameSettings;
import handlers.SoundManager.Sound;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EndPanel extends GridPanel {
    
    private static final long serialVersionUID = 4365760115324893309L;
	static private JLabel runtime = new JLabel();
    static private final GameButton homeBtn = new GameButton("HOME");
    static private final GameButton restartBtn = new GameButton("TRY AGAIN");
    static private final GameButton exitBtn = new GameButton("EXIT");
    
    // GRID SETTNGS
    static private final double titleWeighty = 0.30;
    static private final double centerWeighty = 0.50;

    public EndPanel(PanelHandler panelHandler, GameSettings gameSettings, GameScores gameScores) {

        super(panelHandler, gameSettings, titleWeighty, centerWeighty);
        addScoresToPanel(gameScores);
        addToCenterPanel(new GameButton[]{homeBtn, restartBtn, exitBtn});
       
        homeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	soundManager.playSE(Sound.BUTTON);
            	gameSettings.reset();
            	gameScores.resetTimer();
                panelHandler.goHome();
                
            }

        });

        restartBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	soundManager.playSE(Sound.BUTTON);
                panelHandler.goGame(gameSettings);
                
            }

        });

        exitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	soundManager.playSE(Sound.BUTTON);
                System.exit(0);
                
            }
            
        });

    }
    
    private void addScoresToPanel(GameScores gameScores) {
    	
    	boolean npcMode = false;
    	GameMode gameMode = gameSettings.getGameMode();
    	
    	switch (gameMode) {
    	
			case NORMAL:
				ScoreTable normalTable = fillScoreTable(gameScores, npcMode);
				addToTitlePanel(normalTable.getTableInScrolllPane());
				break;
			
			case ADVENTURE:
				runtime.setText("TIME: " + gameScores.getRuntime());
	            addTitle(runtime);
				break;
			
			case NPC:
				npcMode = true;
				ScoreTable npcTable = fillScoreTable(gameScores, npcMode);
				addToTitlePanel(npcTable.getTableInScrolllPane());
				break;
		
		}
    	
    }
    
    private ScoreTable fillScoreTable(GameScores gameScores, boolean npcMode) {
    	
    	ScoreTable scoreTable = new ScoreTable(gameSettings.getNumberOfPlayers(), npcMode);
    	
    	int scoresCol = 1;
    	
    	ArrayList<Integer> scores = gameScores.getScores();
    	
    	for (int scoreIx = 0; scoreIx < scores.size(); scoreIx++) {
    		
    		scoreTable.setValueAt(Integer.toString(scores.get(scoreIx)), scoreIx, scoresCol);
    	
    	}
    	
    	return scoreTable;
    }

}
