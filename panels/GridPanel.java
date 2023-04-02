package panels;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

import main.GameSettings;
import panels.CardPanel.PanelHandler;

public class GridPanel extends BasePanel {

    // BACKGROUND IMAGE
    static final String imgPath = "static/image/components/snake_hat_large.png";
    static Path relativePath = Paths.get(imgPath);
    static Path absolutePath = relativePath.toAbsolutePath();
    static final Image img = new ImageIcon(absolutePath.toString()).getImage();
 
    // SCREEN LAYOUT. ONLY USED WHEN setGridPanel IS CALLED.
    JPanel titlePanel;
    JPanel centerPanel;
    JPanel footPanel;
    GridBagConstraints gbc;

    protected GridPanel() {

    };

    protected GridPanel(PanelHandler panelHandler, GameSettings gameSettings) {

        super(panelHandler, gameSettings);
        
    }

    public void setGridPanel(double titleWeighty, double centerWeighty) {

        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // ADD TITLE PANEL
        titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setPreferredSize(new Dimension(screenWidth, (int)(titleWeighty * screenHeight)));
        titlePanel.setOpaque(false);
        gbc.weighty = titleWeighty;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titlePanel, gbc);

        // ADD CENTER PANEL
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setPreferredSize(new Dimension(screenWidth, (int)(centerWeighty * screenHeight)));
        centerPanel.setOpaque(false);
        gbc.weighty = centerWeighty;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(centerPanel, gbc);

        // ADD FOOT PANEL. FOOT PANEL IS THERE MOSTLY AS FILLER
        footPanel = new JPanel();
        double footWeighty = 1 - titleWeighty - centerWeighty;
        footPanel.setPreferredSize(new Dimension(screenWidth, (int)(footWeighty * screenHeight)));
        footPanel.setOpaque(false);
        gbc.weighty = footWeighty;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(footPanel, gbc);

    }

    public void addTitle(JLabel title) {

        title.setFont(new Font("PixelMplus12", Font.BOLD, 40));
        title.setForeground(Color.GREEN);
        addToTitlePanel(title);

    }

    public void addToTitlePanel(JComponent component) {
        
        if (titlePanel == null) {
            return;                  // NEED TO HANDLE THROW HERE
        }
        titlePanel.add(component);
    }

    public void addToCenterPanel(JComponent[] componentArray) {
        
        if (centerPanel == null) {
            return;                  // NEED TO HANDLE THROW HERE
        }

        GridBagConstraints panelConstraints = new GridBagConstraints();
        float weight = 1 / componentArray.length;
        Insets space = new Insets(15, 0, 15, 0);
        for (int i = 0; i < componentArray.length; i++) {
            panelConstraints.gridx = 0;
            panelConstraints.gridy = i;
            panelConstraints.weighty = weight;
            panelConstraints.ipady = 15;
            panelConstraints.insets = space; 
            centerPanel.add(componentArray[i], panelConstraints);
            panelConstraints.insets = space; 
        }

    }

    public void paintComponent(Graphics g) {
        g.drawImage(img,(int)(screenWidth * 0.6), (int)(screenHeight * 0.30), null);
        g.drawImage(img,(int)(screenWidth * 0.0), (int)(screenHeight * 0.30), null);
    }

    
}
