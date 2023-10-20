package panels;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

import game.game.GameScores;
import game.game.GameSettings;


public class CardPanel extends BasePanel {
    
    private static final long serialVersionUID = -6897273846533231083L;
    
	private static CardLayout cardLayout = new CardLayout();
    private static HashMap<String, JPanel> panelMap = new HashMap<String, JPanel>();

    public CardPanel() {
    	
        panelHandler = new PanelHandler();
        setLayout(cardLayout);
        loadInitialPanels();
        
    }

    private void loadInitialPanels() {

        ModePanel modePanel = new ModePanel(panelHandler, gameSettings);
        LevelPanel levelPanel = new LevelPanel(panelHandler, gameSettings);
        GamePanel gamePanel = new GamePanel();
        EndPanel endPanel = new EndPanel();
        add(modePanel, "MODE");
        add(levelPanel, "LEVEL");
        add(gamePanel, "GAME");
        panelMap.put("MODE", modePanel);
        panelMap.put("LEVEL", levelPanel);
        panelMap.put("GAME", gamePanel);
        panelMap.put("END", endPanel);

    }

    public class PanelHandler {

        public void goHome() {

            cardLayout.show(CardPanel.this, "MODE");

        }

        public void goLevel() {

            cardLayout.show(CardPanel.this, "LEVEL");

        }

        public void goGame(GameSettings gameSettings) {

            // CREATE AND LOAD A NEW GAME PANEL
            GamePanel gamePanel = new GamePanel(panelHandler, gameSettings);
            CardPanel.this.add(gamePanel, "GAME");
            panelMap.put("GAME", gamePanel);
            cardLayout.show(CardPanel.this, "GAME");

        }

        public void goEnd(GameSettings gameSettings, GameScores gameScores) {

            // DELETE THE EXISTING END PANEL
            JPanel currentEndPanel = panelMap.get("END");
            CardPanel.this.remove(currentEndPanel);

            // CREATE AND LOAD A NEW END PANEL
            EndPanel endPanel = new EndPanel(this, gameSettings, gameScores);
            CardPanel.this.add(endPanel, "END");
            panelMap.put("END", endPanel);
            cardLayout.show(CardPanel.this, "END");
            
        }

    }

}
