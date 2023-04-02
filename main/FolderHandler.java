package main;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;

public class FolderHandler {
    
    Path folderPath;

    public FolderHandler(String textFolderPath) {

        Path relativePath = Paths.get(textFolderPath);
        this.folderPath = relativePath.toAbsolutePath();

    }

    public ArrayList<String> getFilePaths(String suffix) {

        // SUFFIX  NOT INCLUDING THE DOT: "png", "txt"
        ArrayList<String> filePaths = new ArrayList<String>();
        String reg = "*." + suffix;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, reg)) {
            for (Path entry : stream) {
                filePaths.add(entry.toString());
            }
        }
        catch (IOException | DirectoryIteratorException e) {
            e.printStackTrace();
        }

        // RETURNING THE PATHS IN ALPHABETICAL ORDER
        Collections.sort(filePaths);
        return filePaths;
    }

}
