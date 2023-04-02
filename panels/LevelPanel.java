package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.*;
import panels.CardPanel.*;
import customComponents.GameButton;

public class LevelPanel extends GridPanel {
    
    static final JLabel title = new JLabel("CHOOSE SNAKE SPEED");
    static final GameButton slowBtn = new GameButton("TURTLE");
    static final GameButton mediumBtn = new GameButton("SNAKE");
    static final GameButton fastBtn = new GameButton("FALCON");

    // LEVEL SETTINGS
    static final int slowFBS = 4;
    static final int mediumFBS = 6;
    static final int fastFBS = 12;

    public LevelPanel(PanelHandler panelHandler, GameSettings gameSettings) {
        
        super(panelHandler, gameSettings);
        setGridPanel(0.20, 0.60);
        addTitle(title);
        addToCenterPanel(new GameButton[]{slowBtn, mediumBtn, fastBtn});

        slowBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gameSettings.FBS = slowFBS;
                panelHandler.goGame(gameSettings);
            }

        });

        mediumBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gameSettings.FBS = mediumFBS;
                panelHandler.goGame(gameSettings);
            }

        });

        fastBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gameSettings.FBS = fastFBS;
                panelHandler.goGame(gameSettings);
            }

        });

    }

}
