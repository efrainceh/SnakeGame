package utilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;

public class UtilityTool {

    public UtilityTool() { }

    public BufferedImage scaleImage(BufferedImage img, int width, int height) {

        BufferedImage scaledImg = new BufferedImage(width, height, img.getType());
        Graphics2D g3 = scaledImg.createGraphics();
        g3.drawImage(img, 0, 0, width, height, null);
        return scaledImg;

    }

    public Image loadImage(String imgPath) {
        
        Path relativePath = Paths.get(imgPath);
        Path absolutePath = relativePath.toAbsolutePath();
        return new ImageIcon(absolutePath.toString()).getImage();
    
    }

    public void drawRect(Graphics2D g2, int x, int y, int width, int height, String text) {

        // DISPLAY RECTANGLE
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        // BORDER
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 25, 25);

        // TEXT
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45));
        FontMetrics fm = g2.getFontMetrics();
        int xText = x + ((width - fm.stringWidth(text)) / 2);
        int yText = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, xText, yText);

    }

}
