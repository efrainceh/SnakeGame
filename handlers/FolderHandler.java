package handlers;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class FolderHandler {

    public ArrayList<String> getFilePaths(String textFolderPath, String suffix) {

    	Path folderPath = convertToAbsolute(textFolderPath);
        String reg = "*." + suffix;						// SUFFIX  SHOULD NOT INCLUDE THE DOT: "png", "txt"
        ArrayList<String> filePaths = new ArrayList<String>();
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, reg)) {
        	
            for (Path entry : stream) {
            	
                filePaths.add(entry.toString());
                
            }
            
        } catch (IOException | DirectoryIteratorException e) {
        	
        	System.out.println(String.format("ERROR, Folder not found: %s", folderPath));
            e.printStackTrace();
            
        }

        // RETURNING THE PATHS IN ALPHABETICAL ORDER
        Collections.sort(filePaths);
        
        return filePaths;
        
    }
    
    private Path convertToAbsolute(String textFolderPath) {
    	
    	 Path relativePath = Paths.get(textFolderPath);
         return relativePath.toAbsolutePath();
         
    }

}
