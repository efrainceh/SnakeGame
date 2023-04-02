package main;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyHandler {

    // DEFAULT DIRECTION KEYS
    final static String UP = "UP";
    final static String DOWN = "DOWN";
    final static String RIGHT = "RIGHT";
    final static String LEFT = "LEFT";

    JComponent box;   
    String upKey;
    String downKey;
    String rightKey;
    String leftKey;
    public HashMap<Pressed, Boolean> pressedHash = new HashMap<Pressed, Boolean>(); 
    
    public KeyHandler(JComponent box) {

        this.box = box;
        setKeys(new String[] {UP, DOWN, RIGHT, LEFT});
        fillInputMap();
        fillActionMap();
        fillPressedHash();

    }

    public KeyHandler(JComponent box, String[] keys) {

        this.box = box;
        setKeys(keys);
        fillInputMap();
        fillActionMap();
        fillPressedHash();

    }

    public enum Pressed {

        UP_PRESSED,
        DOWN_PRESSED,
        RIGHT_PRESSED,
        LEFT_PRESSED,
        NULL_PRESSED
        
    }

    private void setKeys(String[] keys) {

        upKey = keys[0];
        downKey = keys[1];
        rightKey = keys[2];
        leftKey = keys[3];

    }

    private void fillInputMap() {

        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(upKey), UP);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(downKey), DOWN);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(rightKey), RIGHT);
        box.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(leftKey), LEFT);

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

    public Pressed getPressedKey() {

        // RETURNS THE CURRENTLY PRESSED KEY. RETURN NULL_PRESSED IF NO VALID KEY HAS BEEN PRESSED YET
        for (Pressed pressed : Pressed.values()) {
            if (pressedHash.get(pressed)) {
                return pressed;
            }
        }
        return Pressed.NULL_PRESSED;

    }

    private void setPressedKey(Pressed lastPressed, Pressed currentlyPressed) {

        // CHECKS WHETHER THE PRESSED KEY IS NOT THE OPPOSITE OF THE LAST PRESSED KEY.
        // STOPS THE USER FROM PRESSING "DOWN" IF THE LAST PRESSED KEY WAS "UP", OR "LEFT"
        // IF IT WAS "RIGHT", AND SO ON. 
        if (getPressedKey() != lastPressed) {
            setTrue(currentlyPressed);
        }

    }

    class upAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            setPressedKey(Pressed.DOWN_PRESSED, Pressed.UP_PRESSED);
        }

    }

    class downAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            setPressedKey(Pressed.UP_PRESSED, Pressed.DOWN_PRESSED);
        }

    }

    class rightAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            setPressedKey(Pressed.LEFT_PRESSED, Pressed.RIGHT_PRESSED);
        }

    }

    class leftAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            setPressedKey(Pressed.RIGHT_PRESSED, Pressed.LEFT_PRESSED);
        }
        
    }

}

