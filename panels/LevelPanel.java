package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import panels.CardPanel.*;
import customComponents.GameButton;
import game.game.GameSettings;
import handlers.SoundManager.Sound;

public class LevelPanel extends GridPanel {
    
    private static final long serialVersionUID = -8577658725404872186L;
	static private final JLabel title = new JLabel("CHOOSE SNAKE SPEED");
    static private final GameButton slowBtn = new GameButton("TURTLE");
    static private final GameButton mediumBtn = new GameButton("SNAKE");
    static private final GameButton fastBtn = new GameButton("FALCON");

    // LEVEL SETTINGS
    static private final int slowFBS = 4;
    static private final int mediumFBS = 6;
    static private final int fastFBS = 12;
    
    // GRID SETTNGS
    static private final double titleWeighty = 0.20;
    static private final double centerWeighty = 0.60;

    public LevelPanel(PanelHandler panelHandler, GameSettings gameSettings) {
        
        super(panelHandler, gameSettings, titleWeighty, centerWeighty);
        addTitle(title);
        addToCenterPanel(new GameButton[]{slowBtn, mediumBtn, fastBtn});
        
        slowBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	goToGamePanel(slowFBS);
            	
            }

        });

        mediumBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	goToGamePanel(mediumFBS);
            	
            }

        });

        fastBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	goToGamePanel(fastFBS);
            	
            }

        });

    }
    
    private void goToGamePanel(int FBS) {
	 
    	soundManager.playSE(Sound.BUTTON);
    	gameSettings.setFBS(FBS);
    	panelHandler.goGame(gameSettings);
    	
    }

}
