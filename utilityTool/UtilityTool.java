package utilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
  
    public UtilityTool() { }

    public BufferedImage scaleImage(BufferedImage img, int width, int height) {

        BufferedImage scaledImg = new BufferedImage(width, height, img.getType());
        Graphics2D g2 = scaledImg.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        return scaledImg;

    }

    public void drawWindow(int x, int y, int width, int height, Graphics2D g2) {

        

            // // DISPLAYED RECTANGLE
            // g2.setColor(new Color(255, 255, 255, 150));
            // g2.fillRoundRect(x, y, width, height, 35, 35);

            // // BORDER
            // g2.setColor(Color.RED);
            // g2.setStroke(new BasicStroke(5));
            // g2.drawRoundRect(x, y, width, height, 25, 25);

            // // TEXT
            // int xText = x + (int)(tileSize * 1.2);
            // int yText = y + tileSize/2 + height/2;
            // g2.setColor(Color.RED);
            // g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
            // String message;
            // if (board.isLastMap()) {
            //     message = "FINAL";
            //     g2.drawString(message, xText + 10, y + height/2 - 5);
            //     message = "LEVEL!";
            //     g2.drawString(message, xText + 10, y + height/2 + 50 - 5);
            // } else {
            //     message = "LEVEL " + Integer.toString(board.getMapIndex() + 1);
            //     g2.drawString(message, xText, yText);
            // }

    }



}
