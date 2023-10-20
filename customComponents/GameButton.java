package customComponents;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GameButton extends JButton{

    private static final long serialVersionUID = -7201349261826339618L;

    static private final int btnWidth = 150;
	static private final int btnHeight = 40;
	static private final int tickness = 5;
	static private final int length = 2;
	static private final int spacing = 2;
	static private final boolean rounded = false;
	static private final Border border = BorderFactory.createDashedBorder(Color.BLACK, tickness, length, spacing, rounded);

    public GameButton(String label) {

        super(label);
        setPreferredSize(new Dimension(btnWidth, btnHeight));
        setBorder(border);
        
    }

}
