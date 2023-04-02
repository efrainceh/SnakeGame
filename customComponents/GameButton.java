package customComponents;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GameButton extends JButton{

    static final Border border = BorderFactory.createDashedBorder(Color.BLACK, 5, 2,2,false);
    
    ImageIcon icon;

    public GameButton(String label) {

        super(label);
        setPreferredSize(new Dimension(150, 40));
        setBorder(border);
    }

    public void addIcon(String iconPath) {
        icon = new ImageIcon(getClass().getResource(iconPath));
    }
    
}
