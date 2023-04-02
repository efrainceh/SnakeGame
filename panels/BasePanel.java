package panels;

import java.awt.*;
import javax.swing.*;

import main.GameSettings;
import panels.CardPanel.PanelHandler;
import utilityTool.UtilityTool;

public class BasePanel extends JPanel {

    // GAME PANEL SETTINGS. USED IN BOARDPANEL AND SCOREPANEL
    static final int originalTileSize = 16;
    static final int scale = 2;
    static final int tileSize = originalTileSize * scale;
    static final int tilesPerRow = 16;
    static final int tilesPerCol = 16;
    static final int tilesInScorePanel = 5;
   
    // SCREEN SETTINGS. USED IN ALL PANELS
    static final int screenHeight = tileSize * tilesPerRow;
    static final int screenWidth = tileSize * (tilesPerCol + tilesInScorePanel);
    
    // UTILITY
    UtilityTool utilityTool = new UtilityTool();

    // USED TO HANDLE MOVING BETWEEN PANELS
    PanelHandler panelHandler;

    // USED TO PASS INFORMATION BETWEEN PANELS
    GameSettings gameSettings;

    protected BasePanel() {

        setPreferredSize(new Dimension(screenWidth, screenHeight));

    }

    protected BasePanel(PanelHandler panelHandler, GameSettings gameSettings) {

        this.panelHandler = panelHandler;
        this.gameSettings = gameSettings;
        setPreferredSize(new Dimension(screenWidth, screenHeight));

    }

}
