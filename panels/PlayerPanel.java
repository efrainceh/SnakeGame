package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.*;
import panels.CardPanel.*;
import customComponents.GameButton;

public class PlayerPanel extends GridPanel {

    static final JLabel title = new JLabel("SNAKE AND TACOS");
    static final GameButton onePlayerBtn = new GameButton("ONE PLAYER");
    static final GameButton twoPlayerBtn = new GameButton("TWO PLAYER");
    static final GameButton adventureBtn = new GameButton("ADVENTURE");

    // PLAYER SETTINGS
    static final int onePlayer = 1;
    static final int twoPlayer = 2;

    public PlayerPanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings);
        setGridPanel(0.20, 0.60);
        addTitle(title);
        addToCenterPanel(new GameButton[]{onePlayerBtn, twoPlayerBtn, adventureBtn});

        onePlayerBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setGameSettings(onePlayer, 0, false);
                panelHandler.goLevel();
            }

        });

        twoPlayerBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setGameSettings(twoPlayer, 0, false);
                panelHandler.goLevel();
            }

        });

        adventureBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setGameSettings(onePlayer, 0, true);
                panelHandler.goLevel();
            }

        });

    }

    private void setGameSettings(int numberOfPlayers, int mapIndex, boolean adventure) {

        gameSettings.numberOfPlayers = numberOfPlayers;
        gameSettings.mapIndex = mapIndex;
        gameSettings.adventure = adventure;        

    }

}
