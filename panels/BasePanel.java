package panels;

import java.awt.*;
import javax.swing.*;

import main.GameSettings;
import main.SoundManager;
import main.SoundManager.Sound;
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
    
    // TOOLS
    UtilityTool utilityTool = new UtilityTool();
    SoundManager soundManager = new SoundManager();

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

    protected void playMusic(Sound sound) {

        soundManager.setFile(sound, true);
        soundManager.playLoop();

    }

    protected void playSE(Sound sound) {

        soundManager.setFile(sound, false);
        soundManager.playSE();

    }

    protected void stopMusic() {

        soundManager.stopLoop();

    }

}
