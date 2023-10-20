package panels;

import java.awt.*;
import javax.swing.*;
import lombok.NoArgsConstructor;

import game.game.GameSettings;
import panels.CardPanel.PanelHandler;


@NoArgsConstructor
public abstract class GridPanel extends BasePanel {

    private static final long serialVersionUID = 3541508285677812951L;

	// BACKGROUND IMAGE
    private final Image img = loadImage("static/image/components/snake_hat_large.png");
 
    // SCREEN LAYOUT. ONLY USED WHEN setGridPanel IS CALLED.
    private JPanel titlePanel;
    private JPanel centerPanel;
    private JPanel footPanel;
    private GridBagConstraints gbc;

    protected GridPanel(PanelHandler panelHandler, GameSettings gameSettings, double titleWeighty, double centerWeighty) {

        super(panelHandler, gameSettings);
        setGrid(titleWeighty, centerWeighty);
        
    }

    private void setGrid(double titleWeighty, double centerWeighty) {

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

    protected final void addTitle(JLabel title) {

        title.setFont(new Font("PixelMplus12", Font.BOLD, 40));
        title.setForeground(Color.GREEN);
        titlePanel.add(title);

    }
    
    protected final void addToTitlePanel(JComponent component) {
        
        titlePanel.add(component);

    }

    protected final void addToCenterPanel(JComponent[] componentArray) {
        
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

    protected final void addToFooterPanel(JComponent component) {

        footPanel.add(component);

    }

    public void paintComponent(Graphics g) {
    	
        g.drawImage(img,(int)(screenWidth * 0.6), (int)(screenHeight * 0.30), null);
        g.drawImage(img,(int)(screenWidth * 0.0), (int)(screenHeight * 0.30), null);
        
    }

    
}
