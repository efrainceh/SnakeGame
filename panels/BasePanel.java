package panels;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

import game.game.GameSettings;
import handlers.SoundManager;
import panels.CardPanel.PanelHandler;


public abstract class BasePanel extends JPanel {

    private static final long serialVersionUID = 1L;
	
	// GAME PANEL SETTINGS. USED IN BOARDPANEL AND SCOREPANEL
    private static final int originalTileSize = 16;
    private static final int scale = 2;
    protected static final int tileSize = originalTileSize * scale;
    protected static final int tilesPerRow = 16;
    protected static final int tilesPerCol = 16;
    protected static final int tilesInScorePanel = 5;
    
   
    // SCREEN SETTINGS. USED IN ALL PANELS
    protected static final int screenHeight = tileSize * tilesPerRow;
    protected static final int screenWidth = tileSize * (tilesPerCol + tilesInScorePanel);
    
    // TOOLS
    protected SoundManager soundManager = new SoundManager();
    
    // USED TO PASS INFORMATION BETWEEN PANELS
    protected GameSettings gameSettings = new GameSettings(tileSize, tilesPerRow, tilesPerCol);

    // USED TO MOVE BETWEEN PANELS
    protected PanelHandler panelHandler;

    protected BasePanel() {

        setPreferredSize(new Dimension(screenWidth, screenHeight));

    }

    protected BasePanel(PanelHandler panelHandler, GameSettings gameSettings) {

        this.panelHandler = panelHandler;
        this.gameSettings = gameSettings;
        setPreferredSize(new Dimension(screenWidth, screenHeight));

    }
    
    protected Image loadImage(String imgPath) {
  
    	Path relativePath = Paths.get(imgPath);
    	Path absolutePath = relativePath.toAbsolutePath();
    	
    	return new ImageIcon(absolutePath.toString()).getImage();

    }

}
