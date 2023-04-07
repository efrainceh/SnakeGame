package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.*;
import main.SoundManager.Sound;
import panels.CardPanel.*;
import customComponents.*;

public class EndPanel extends GridPanel {
    
    static JLabel runtime = new JLabel();
    static final GameButton homeBtn = new GameButton("HOME");
    static final GameButton restartBtn = new GameButton("TRY AGAIN");
    static final GameButton exitBtn = new GameButton("EXIT");

    public EndPanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings);
        setGridPanel(0.30, 0.50);

        if (gameSettings.adventure) {
            runtime.setText("TIME: " + gameSettings.timer.getRuntime());
            addTitle(runtime);
        } else {
            addToTitlePanel(gameSettings.scoreTable.getTableInScrolllPane());
        }

        addToCenterPanel(new GameButton[]{homeBtn, restartBtn, exitBtn});
       
        homeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                playSE(Sound.BUTTON);
                panelHandler.goHome();
            }

        });

        restartBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                playSE(Sound.BUTTON);
                panelHandler.goGame(gameSettings);
            }

        });

        exitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                playSE(Sound.BUTTON);
                System.exit(0);
            }
            
        });

    }

}
