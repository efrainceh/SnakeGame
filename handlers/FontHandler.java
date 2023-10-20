package handlers;

import java.awt.*;
import java.io.*;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FontHandler {

    public void addNewFont(String filePath) {

        try {
        	
            // ADD FONT TO THE ENVIRONMENT
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream myStream = new BufferedInputStream(new FileInputStream(filePath));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, myStream));
            
        } catch (IOException | FontFormatException e) {
        	
            e.printStackTrace();
            
        }

    }
 
    public void setFont(String fontName, int style, int size) {

        // SETS THE FONT FOR ALL JCOMPONENTS
        Font font = new Font(fontName, style, size);               // Should check that font exists
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        
        while (keys.hasMoreElements()) {
        	
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            
            if (value instanceof FontUIResource) {
            	
                UIManager.put(key, new FontUIResource(font));
                
            }
            
        }
        
    }

}

