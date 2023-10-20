package base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import handlers.FolderHandler;

public abstract class Manager {
	
	public abstract void scaleImages(int width, int height);
	
	protected final static ArrayList<String> loadFilePaths(String folderPath, String suffix) {

        FolderHandler folderH = new FolderHandler();        
        ArrayList<String> tempFilePaths = folderH.getFilePaths(folderPath, suffix);

        return tempFilePaths;
        
    }
	
	protected final BufferedImage scaleImage(BufferedImage img, int width, int height) {

        BufferedImage scaledImg = new BufferedImage(width, height, img.getType());
        Graphics2D g2 = scaledImg.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        
        return scaledImg;

    }
	
	protected final ArrayList<String[]> loadImgData(String folderPath, String suffix) {
    	
    	ArrayList<String> imgPaths = loadFilePaths(folderPath, suffix);
    	
    	
    	ArrayList<String[]> tempImgData = new ArrayList<String[]>();
    	for (String path : imgPaths) {
    		
    		tempImgData.add(readFile(path));
    		
    	}

       return tempImgData;
		
    }
    
    private String[] readFile(String filePath) {
    	
    	String[] data = null;
    	
    	 try {
         	
             // OPEN FILE
             InputStream is = new FileInputStream(filePath);
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(is));
             
             // FIRST AND ONLY LINE CONTAINS THE IMAGE DATA
             String line = fileReader.readLine();
             data = line.split(" ");

             fileReader.close();

         } catch (Exception e) {
         	
             e.printStackTrace();
             
         }
         
 		return data;
    	
    }

}
