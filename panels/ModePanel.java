package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import panels.CardPanel.*;
import customComponents.GameButton;
import game.game.GameMode;
import game.game.GameSettings;
import handlers.SoundManager.Sound;

public class ModePanel extends GridPanel {

    private static final long serialVersionUID = -6833206899584636050L;
	static private final JLabel title = new JLabel("SNAKE AND TACOS");
    static private final GameButton onePlayerBtn = new GameButton("ONE PLAYER");
    static private final GameButton twoPlayerBtn = new GameButton("TWO PLAYER");
    static private final GameButton adventureBtn = new GameButton("ADVENTURE");
    static private final JCheckBox npcCheckBox = new JCheckBox("Add PC Snake");

    // PLAYER SETTINGS
    static private final int onePlayer = 1;
    static private final int twoPlayer = 2;
    
    // GRID SETTNGS
    static private final double titleWeighty = 0.20;
    static private final double centerWeighty = 0.60;

    public ModePanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings, titleWeighty, centerWeighty);
        addTitle(title);
        addToCenterPanel(new GameButton[]{onePlayerBtn, twoPlayerBtn, adventureBtn});
        addToFooterPanel(npcCheckBox);

        onePlayerBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (npcCheckBox.isSelected()) {
            		
            		goToLevelPanel(onePlayer, GameMode.NPC);
            		
            	} else {
            		
            		goToLevelPanel(onePlayer, GameMode.NORMAL);
            		
            	}
                  
            }

        });

        twoPlayerBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	goToLevelPanel(twoPlayer, GameMode.NORMAL);
                
            }

        });

        adventureBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	goToLevelPanel(onePlayer, GameMode.ADVENTURE);
            }

        });
        
        npcCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (npcCheckBox.isSelected()) {
            		
            		twoPlayerBtn.setEnabled(false);
                    adventureBtn.setEnabled(false);
            		
            	} else {
            		
            		twoPlayerBtn.setEnabled(true);
                    adventureBtn.setEnabled(true);
            		
            	}
            	
            }
        });
        
    }
    
    private void goToLevelPanel(int numberOfPlayers, GameMode mode) {
    	
    	soundManager.playSE(Sound.BUTTON);
    	gameSettings.setNumberOfPlayers(numberOfPlayers);
        gameSettings.setGameMode(mode); 
        panelHandler.goLevel();
    	
    }

}
