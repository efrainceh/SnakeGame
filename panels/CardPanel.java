package panels;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

import main.GameSettings;

public class CardPanel extends BasePanel {
    
    static CardLayout cardLayout = new CardLayout();
    static HashMap<String, JPanel> panelMap = new HashMap<String, JPanel>();

    public CardPanel() {

        // INITIALIZE BASE PANEL panelHandler and gameSettings
        panelHandler = new PanelHandler();
        gameSettings = new GameSettings();
        
        setLayout(cardLayout);
        loadInitialPanels();
        
    }

    private void loadInitialPanels() {

        PlayerPanel playerPanel = new PlayerPanel(panelHandler, gameSettings);
        LevelPanel levelPanel = new LevelPanel(panelHandler, gameSettings);
        GamePanel gamePanel = new GamePanel(panelHandler, gameSettings);
        EndPanel endPanel = new EndPanel(panelHandler, gameSettings);
        add(playerPanel, "PLAYER");
        add(levelPanel, "LEVEL");
        add(gamePanel, "GAME");
        panelMap.put("PLAYER", playerPanel);
        panelMap.put("LEVEL", levelPanel);
        panelMap.put("GAME", gamePanel);
        panelMap.put("END", endPanel);

    }

    public class PanelHandler {

        public void goHome() {

            gameSettings.timer.reset();
            cardLayout.show(CardPanel.this, "PLAYER");

        }

        public void goLevel() {

            // THIS IS NEEDED BECAUSE THE CHECKBOX IN LEVEL PANEL SHOULD
            // ONLY APPEAR WHEN THERE IS ONLY ONE PLAYER

            // DELETE THE EXISTING LEVEL PANEL.
            JPanel currentLevelPanel = panelMap.get("LEVEL");
            CardPanel.this.remove(currentLevelPanel);

            // CREATE AND LOAD A NEW LEVEL PANEL
            LevelPanel levelPanel = new LevelPanel(panelHandler, gameSettings);
            CardPanel.this.add(levelPanel, "LEVEL");
            panelMap.put("LEVEL", levelPanel);
            cardLayout.show(CardPanel.this, "LEVEL");

        }

        public void goGame(GameSettings gameSettings) {

            // DELETE THE EXISTING GAME PANEL
            JPanel currentGamePanel = panelMap.get("GAME");
            CardPanel.this.remove(currentGamePanel);

            // CREATE AND LOAD A NEW GAME PANEL
            GamePanel gamePanel = new GamePanel(panelHandler, gameSettings);
            CardPanel.this.add(gamePanel, "GAME");
            panelMap.put("GAME", gamePanel);
            cardLayout.show(CardPanel.this, "GAME");

        }

        public void goEnd(GameSettings gameSettings) {

            // DELETE THE EXISTING END PANEL
            JPanel currentEndPanel = panelMap.get("END");
            CardPanel.this.remove(currentEndPanel);

            // CREATE AND LOAD A NEW END PANEL
            EndPanel endPanel = new EndPanel(this, gameSettings);
            CardPanel.this.add(endPanel, "END");
            panelMap.put("END", endPanel);
            cardLayout.show(CardPanel.this, "END");
            
        }

    }

}
