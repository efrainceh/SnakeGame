package handlers;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyHandler {

    // DEFAULT DIRECTION KEYS
    static private final String UP = "UP";
    static private final String DOWN = "DOWN";
    static private final String RIGHT = "RIGHT";
    static private final String LEFT = "LEFT";

    private JComponent box;
    private HashMap<Pressed, Boolean> pressedHash = new HashMap<Pressed, Boolean>(); 

    public KeyHandler(JComponent box, String[] keys) {

        this.box = box;
        fillInputMap(keys);
        fillActionMap();
        fillPressedHash();

    }
    
    public Pressed getPressedKey() {

        // RETURNS THE CURRENTLY PRESSED KEY. RETURN NULL_PRESSED IF NO VALID KEY HAS BEEN PRESSED YET
        for (Pressed pressed : Pressed.values()) {
        	
            if (pressedHash.get(pressed)) {
            	
                return pressed;
                
            }
        
        }
        
        return Pressed.NULL_PRESSED;

    }

    public enum Pressed {

        UP_PRESSED,
        DOWN_PRESSED,
        RIGHT_PRESSED,
        LEFT_PRESSED,
        NULL_PRESSED
        
    }

    private void fillInputMap(String[] keys) {

        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keys[0]), UP);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keys[1]), DOWN);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keys[2]), RIGHT);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keys[3]), LEFT);

    }

    private void fillActionMap() {

        box.getActionMap().put(UP, new upAction());
        box.getActionMap().put(DOWN, new downAction());
        box.getActionMap().put(RIGHT, new rightAction());
        box.getActionMap().put(LEFT, new leftAction());

    }

    private void fillPressedHash() {

        for (Pressed pressed : Pressed.values()) {
        	
            pressedHash.put(pressed, false);
            
        }

    }

    private void setPressedKey(Pressed lastPressed, Pressed currentlyPressed) {

        // CHECKS WHETHER THE PRESSED KEY IS NOT THE OPPOSITE OF THE LAST PRESSED KEY.
        // STOPS THE USER FROM PRESSING "DOWN" IF THE LAST PRESSED KEY WAS "UP", OR "LEFT"
        // IF IT WAS "RIGHT", AND SO ON. 
        if (getPressedKey() != lastPressed) {
        	
            setTrue(currentlyPressed);
        
        }

    }
    
    private void setTrue(Pressed key) {

        // SETS THE VALUE OF key TO TRUE IN THE HASHMAP, CHANGES ALL OTHERS TO FALSE
        for (Pressed pressed : Pressed.values()) {
        	
            if (pressed == key) {
            	
                pressedHash.replace(pressed, true);
                
            } else {
            	
                pressedHash.replace(pressed, false);
            
            }
            
        }

    }

    class upAction extends AbstractAction {

        private static final long serialVersionUID = 7145272634410313218L;

		public void actionPerformed(ActionEvent e) {
			
            setPressedKey(Pressed.DOWN_PRESSED, Pressed.UP_PRESSED);
            
        }

    }

    class downAction extends AbstractAction {

        private static final long serialVersionUID = -8389110421212774995L;

		public void actionPerformed(ActionEvent e) {
			
            setPressedKey(Pressed.UP_PRESSED, Pressed.DOWN_PRESSED);
            
        }

    }

    class rightAction extends AbstractAction {

        private static final long serialVersionUID = 7443742371399078557L;

		public void actionPerformed(ActionEvent e) {
			
            setPressedKey(Pressed.LEFT_PRESSED, Pressed.RIGHT_PRESSED);
            
        }

    }

    class leftAction extends AbstractAction {

        private static final long serialVersionUID = 7210511523392745279L;

		public void actionPerformed(ActionEvent e) {
			
            setPressedKey(Pressed.RIGHT_PRESSED, Pressed.LEFT_PRESSED);
            
        }
        
    }

}
